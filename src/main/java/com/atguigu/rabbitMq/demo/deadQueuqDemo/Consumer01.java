package com.atguigu.rabbitMq.demo.deadQueuqDemo;

import com.atguigu.rabbitMq.constant.CommonUtils;
import com.atguigu.rabbitMq.utils.RabbitMqUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.util.Currency;
import java.util.HashMap;
import java.util.Map;

/**
 * 消费者01，内置处理死信队列
 *
 * @author Hanser
 */
public class Consumer01 {

    public static void main(String[] argv) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        //声明死信和普通交换机 类型为 direct
        channel.exchangeDeclare(CommonUtils.NORMAL_EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        channel.exchangeDeclare(CommonUtils.DEAD_EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        //声明死信队列
        channel.queueDeclare(CommonUtils.DEAD_EXCHANGE_QUEUE, false, false, false, null);
        //死信队列绑定死信交换机与 routingkey
        channel.queueBind(CommonUtils.DEAD_EXCHANGE_QUEUE, CommonUtils.DEAD_EXCHANGE_NAME, "lisi");
        //正常队列绑定死信队列信息
        Map<String, Object> params = new HashMap<>();
        //设置过期时间 10s
//        params.put("x-message-ttl", 100000);
        //正常队列设置死信交换机 参数 key 是固定值
        params.put("x-dead-letter-exchange", CommonUtils.DEAD_EXCHANGE_NAME);
        //正常队列设置死信 routing-key 参数 key 是固定值
        params.put("x-dead-letter-routing-key", "lisi");
        //设置死信队列最大长度
//        params.put("x-max-length", 6);
        channel.queueDeclare(CommonUtils.NORMAL_EXCHANGE_QUEUE, false, false, false, params);
        channel.queueBind(CommonUtils.NORMAL_EXCHANGE_QUEUE, CommonUtils.NORMAL_EXCHANGE_NAME, "zhangsan");
        System.out.println("等待接收消息.....");
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            if (message.equals("info5")) {
                System.out.println("Consumer01 接收到消息" + message + "并拒绝签收该消息");
                //requeue 设置为 false 代表拒绝重新入队 该队列如果配置了死信交换机将发送到死信队列中
                channel.basicReject(delivery.getEnvelope().getDeliveryTag(), false);
            } else {
                System.out.println("Consumer01 接收到消息" + message);
                channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
            }
            System.out.println("Consumer01 接收到消息" + message);
        };
        channel.basicConsume(CommonUtils.NORMAL_EXCHANGE_QUEUE, false, deliverCallback, consumerTag -> {
        });
    }
}
