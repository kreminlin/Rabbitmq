package com.atguigu.rabbitMq.demo.confirmMessage;

import com.atguigu.rabbitMq.constant.CommonUtils;
import com.atguigu.rabbitMq.utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmCallback;
import com.rabbitmq.client.ConfirmListener;

import java.util.UUID;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * 消息发布确认
 * 模式有三种
 * 1. 单个确认模式
 * 2. 批量确认模式
 * 3. 异步确认模式
 *
 * @author Hanser
 */
public class ConfirmMessage {

    public static void main(String[] args) throws Exception {
        /**
         * 1. 单个确认模式
         * 2. 批量确认模式
         * 3. 异步确认模式
         */
//        ConfirmMessage.publishMessageIndividually();
//        ConfirmMessage.publishMessageBatch();
        ConfirmMessage.publishMessageAsync();
    }

    public static void publishMessageIndividually() throws Exception {
        try (Channel channel = RabbitMqUtils.getChannel()) {
            String queueName = UUID.randomUUID().toString();
            channel.queueDeclare(queueName, false, false, false, null);
            //开启发布确认模式
            channel.confirmSelect();
            long begin = System.currentTimeMillis();
            for (int i = 1; i <= CommonUtils.MESSAGE_COUTN; i++) {
                String message = i + " ";
                channel.basicPublish("", queueName, null, message.getBytes());
                //服务端返回 false 或超时时间内未返回，生产者可以消息重发
                boolean flag = channel.waitForConfirms();
                if (flag) {
                    System.out.println("消息发送成功");
                }
            }
            long end = System.currentTimeMillis();
            System.out.println("发布" + CommonUtils.MESSAGE_COUTN + "个单独确认消息,耗时" + (end - begin) +
                    "ms");
        }
    }

    public static void publishMessageBatch() throws Exception {
        try (Channel channel = RabbitMqUtils.getChannel()) {
            String queueName = UUID.randomUUID().toString();
            channel.queueDeclare(queueName, false, false, false, null);
            //开启发布确认模式
            channel.confirmSelect();
            long begin = System.currentTimeMillis();
            //批量确认消息大小
            int batchSize = 100;
            //未确认消息个数
            int outstandingMessageCount = 0;
            for (int i = 1; i <= CommonUtils.MESSAGE_COUTN; i++) {
                String message = i + "";
                channel.basicPublish("", queueName, null, message.getBytes());
                outstandingMessageCount++;
                if (outstandingMessageCount == batchSize) {
                    channel.waitForConfirms();
                    outstandingMessageCount = 0;
                }
            }
            //为了确保还有剩余没有确认消息 再次确认
            if (outstandingMessageCount > 0) {
                channel.waitForConfirms();
            }
            long end = System.currentTimeMillis();
            System.out.println("发布" + CommonUtils.MESSAGE_COUTN + "个单独确认消息,耗时" + (end - begin) +
                    "ms");
        }
    }

    public static void publishMessageAsync() throws Exception {
        try (Channel channel = RabbitMqUtils.getChannel()) {
            String queueName = UUID.randomUUID().toString();
            channel.queueDeclare(queueName, false, false, false, null);
            //开启发布确认
            channel.confirmSelect();
            /**
             * 线程安全有序的一个哈希表，适用于高并发的情况
             * 1.轻松的将序号与消息进行关联
             * 2.轻松批量删除条目 只要给到序列号
             * 3.支持并发访问
             */
            ConcurrentSkipListMap<Long, String> concurrentSkipListMap = new ConcurrentSkipListMap<>();

            //消息成功回调函数
            //multiple：是否批量确认
            ConfirmCallback confirmCallbackSuccess = (deliverTag, multiple) -> {
                if (multiple) {
                    //返回的是小于等于当前序列号的未确认消息 是一个 map
                    ConcurrentNavigableMap<Long, String> concurrentSkipListMap1 = concurrentSkipListMap.headMap(deliverTag, true);
                    //清除该部分未确认消息
                    concurrentSkipListMap.clear();
                } else {
                    //只清除当前序列号的消息
                    concurrentSkipListMap.remove(deliverTag);
                }

            };
            //消息失败回调函数
            ConfirmCallback confirmListenerFail = (deliverTag, multiple) -> {
                String message = concurrentSkipListMap.get(deliverTag);
                System.out.println("发布的消息: " + message + "未被确认，序列号: " + deliverTag);
            };
            /**
             * 添加一个异步确认的监听器
             * 1.确认收到消息的回调
             * 2.未收到消息的回调
             */
            channel.addConfirmListener(confirmCallbackSuccess, confirmListenerFail);
            long begin = System.currentTimeMillis();
            for (int i = 1; i <= CommonUtils.MESSAGE_COUTN; i++) {
                String message = "消息: " + i;
                /**
                 * channel.getNextPublishSeqNo()获取下一个消息的序列号
                 * 通过序列号与消息体进行一个关联
                 * 全部都是未确认的消息体
                 */
                concurrentSkipListMap.put(channel.getNextPublishSeqNo(), message);
                channel.basicPublish("", queueName, null, message.getBytes());
            }
            long end = System.currentTimeMillis();
            System.out.println("发布" + CommonUtils.MESSAGE_COUTN + "个异步确认消息,耗时" + (end - begin) +
                    "ms");
        }
    }

}
