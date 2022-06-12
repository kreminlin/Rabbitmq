package com.atguigu.rabbitMq.demo.exchangeModel;

import com.atguigu.rabbitMq.constant.CommonUtils;
import com.atguigu.rabbitMq.utils.RabbitMqUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.nio.charset.StandardCharsets;

/**
 * 交换机类型演示
 *
 * @author Hanser
 */
public class ReceiveTopicLogs02 {

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        //声明一个直接交换机
        channel.exchangeDeclare(CommonUtils.TOPIC_EXCHANGE_NAME, BuiltinExchangeType.TOPIC);
        //声明一个队列
        String queue01 = "Q2";
        channel.queueDeclare(queue01, false, false, false, null);
        channel.queueBind(queue01, CommonUtils.TOPIC_EXCHANGE_NAME, "*.*.rabbit");
        channel.queueBind(queue01, CommonUtils.TOPIC_EXCHANGE_NAME, "lazy.#");
        System.out.println("ReceiveTopicLogs02等待接收的消息..........");
        DeliverCallback deliverCallbackack = ((s, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println(" 接收队列 :" + s + " 绑 定键:" + delivery.getEnvelope().getRoutingKey() + ", 消息:" + message);
        });
        CancelCallback cancelCallback = ((s) -> {
            System.out.println("ReceiveTopicLogs02控制台打印的接收失败的消息： " + s.getBytes(StandardCharsets.UTF_8));
        });
        //消费者消费
        channel.basicConsume(queue01, true, deliverCallbackack, cancelCallback);
    }
}
