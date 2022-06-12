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
public class ReceiveDirectLogs01 {

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        //声明一个直接交换机
        channel.exchangeDeclare(CommonUtils.DIRECT_EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        //声明一个队列
        channel.queueDeclare("console", false, false, false, null);
        channel.queueBind("console", CommonUtils.DIRECT_EXCHANGE_NAME, "info");
        channel.queueBind("console", CommonUtils.DIRECT_EXCHANGE_NAME, "warning");
        System.out.println("ReceiveDirectLogs01等待接收的消息..........");
        DeliverCallback deliverCallbackack = ((s, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println("ReceiveDirectLogs01控制台打印的接收的消息： " + message);
        });
        CancelCallback cancelCallback = ((s) -> {
            System.out.println("ReceiveDirectLogs01控制台打印的接收失败的消息： " + s.getBytes(StandardCharsets.UTF_8));
        });
        //消费者消费
        channel.basicConsume("console", true, deliverCallbackack, cancelCallback);
    }
}
