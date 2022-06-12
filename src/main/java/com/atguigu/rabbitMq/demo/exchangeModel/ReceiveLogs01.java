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
public class ReceiveLogs01 {

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        //声明一个交换机
        channel.exchangeDeclare(CommonUtils.EXCHANGE_NAME, "fanout");
//        channel.exchangeDeclare("2", "fanout");
        //声明一个临时队列,链接断开后队列消失
        String tempQueue = channel.queueDeclare().getQueue();
        /**
         * 绑定交换机和队列
         */
        channel.queueBind(tempQueue, CommonUtils.EXCHANGE_NAME, "");
        System.out.println("ReceiveLogs01等待接收的消息..........");
        DeliverCallback deliverCallbackack = ((s, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println("ReceiveLogs01控制台打印的接收的消息： " + message);
        });
        CancelCallback cancelCallback = ((s) -> {
            System.out.println("ReceiveLogs01控制台打印的接收失败的消息： " + s.getBytes(StandardCharsets.UTF_8));
        });
        //消费者消费
        channel.basicConsume(tempQueue, true, deliverCallbackack, cancelCallback);
    }
}
