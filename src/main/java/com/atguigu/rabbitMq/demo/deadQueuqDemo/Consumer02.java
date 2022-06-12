package com.atguigu.rabbitMq.demo.deadQueuqDemo;

import com.atguigu.rabbitMq.constant.CommonUtils;
import com.atguigu.rabbitMq.utils.RabbitMqUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.util.HashMap;
import java.util.Map;

/**
 * 消费者01，内置处理死信队列
 *
 * @author Hanser
 */
public class Consumer02 {

    public static void main(String[] argv) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        //声明死信和普通交换机 类型为 direct
        channel.exchangeDeclare(CommonUtils.DEAD_EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        //声明死信队列
        channel.queueDeclare(CommonUtils.DEAD_EXCHANGE_QUEUE, false, false, false, null);
        //死信队列绑定死信交换机与 routingkey
        channel.queueBind(CommonUtils.DEAD_EXCHANGE_QUEUE, CommonUtils.DEAD_EXCHANGE_NAME, "lisi");
        System.out.println("等待接收消息.....");
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println("Consumer02 接收到消息" + message);
        };
        channel.basicConsume(CommonUtils.DEAD_EXCHANGE_QUEUE, true, deliverCallback, consumerTag -> {
        });
    }
}
