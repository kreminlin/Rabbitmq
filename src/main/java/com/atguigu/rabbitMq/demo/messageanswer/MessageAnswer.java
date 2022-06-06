package com.atguigu.rabbitMq.demo.messageanswer;

import com.atguigu.rabbitMq.constant.CommonUtils;
import com.atguigu.rabbitMq.utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;


/**
 * 消息应答练习
 *
 * @author Hanser
 */
public class MessageAnswer {

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        channel.queueDeclare(CommonUtils.TASK_QUEUE_NAME, false, false, false, null);
        System.out.println("C1等待接受消息......");
        DeliverCallback deliverCallback = ((s, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println("接收到的消息为： " + message);
            /**
             * 1. 消息标记tag
             * 2. false代表只应答接收到的那个消息，true代表应答所有消息
             */
            channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
        });
        //手动应答
        boolean autoAck = false;
        channel.basicConsume(CommonUtils.TASK_QUEUE_NAME, autoAck, deliverCallback, consumerTag -> {
        });
    }
}
