package com.atguigu.rabbitMq.demo.helloworld;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

/**
 * 生产者
 * hello world模式
 */
public class Producer {

    //队列名称
    public static final String QUEUE_NAME = "hello";
    //发消息
    public static void main(String[] args) {
        //创建一个队列工厂
        ConnectionFactory factory = new ConnectionFactory();
        //设置工厂ID
        factory.setHost("127.0.0.1");
        factory.setUsername("guest");
        factory.setPassword("guest");

        try {
            //创建链接
            Connection connection = factory.newConnection();
            //获取信道
            Channel channel = connection.createChannel();
            /**
             * 创建一个队列
             * 1. 名称
             * 2. 是否持久化（存储在磁盘中,true），默认情况(false)消息储存在内存中
             * 3. 该队列是否进行消息共享（true，可以提供多个消费者消费），默认为false
             * 4. 是否自动删除（最后一个消费者断开链接后是否自动删除）
             * 5. 其他参数
             */
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            //发消息,初次使用
            String message = "hello world";
            /**
             * 发送一个消息
             * 1.发送到那个交换机
             * 2.路由的 key 是哪个
             * 3.其他的参数信息
             * 4.发送消息的消息体
             */
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes(StandardCharsets.UTF_8));
            //消息发送
            System.out.println("消息发送完毕......");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }
}
