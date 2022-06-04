package com.atguigu.rabbitMq.demo.workqueue;

import com.atguigu.rabbitMq.constant.CommonUtils;
import com.atguigu.rabbitMq.utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;

import java.util.Scanner;

/**
 * 启动一个发送线程
 */
public class Task01 {

    public static void main(String[] args) throws Exception {
        try (Channel channel = RabbitMqUtils.getChannel();) {
            /**
             * 创建一个队列
             * 1. 名称
             * 2. 是否持久化（存储在磁盘中,true），默认情况(false)消息储存在内存中
             * 3. 该队列是否进行消息共享（true，可以提供多个消费者消费），默认为false
             * 4. 是否自动删除（最后一个消费者断开链接后是否自动删除）
             * 5. 其他参数
             */
            channel.queueDeclare(CommonUtils.QUEUE_NAME, false, false, false, null);
            //从控制台当中接受信息
            Scanner scanner = new Scanner(System.in);
            while (scanner.hasNext()) {
                String message = scanner.next();
                /**
                 * 发送一个消息
                 * 1.发送到那个交换机
                 * 2.路由的 key 是哪个
                 * 3.其他的参数信息
                 * 4.发送消息的消息体
                 */
                channel.basicPublish("", CommonUtils.QUEUE_NAME, null, message.getBytes());
                System.out.println("发送消息完成:" + message);
            }
        }
    }
}
