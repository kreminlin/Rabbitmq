package com.atguigu.rabbitMq.demo.workqueue;

import com.atguigu.rabbitMq.constant.CommonUtils;
import com.atguigu.rabbitMq.utils.RabbitMqUtils;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

/**
 * 这是一个工作线程，相当于一个消费者
 */
public class Worker01 {

    public static void main(String[] args) throws Exception {
        for (int i = 0; i < 2; i++) {
            WorkThread workThread = new WorkThread();
            Thread thread = new Thread(workThread, String.valueOf(i));
            thread.start();
        }
    }
}

class WorkThread implements Runnable {

    @Override
    public void run() {
        Channel channel = null;
        try {
            channel = RabbitMqUtils.getChannel();
            DeliverCallback deliverCallback = (consumerTage, delivery) -> {
                String receivedMessage = new String(delivery.getBody());
                //轮询方式
                /**
                 * Thread Name: 0 is working..... receiving message.....
                 * Thread Name: 1 is working..... receiving message.....
                 * Thread Name: pool-1-thread-4 is receiving message.....
                 * 接收到消息:1
                 * Thread Name: pool-2-thread-4 is receiving message.....
                 * 接收到消息:2
                 * Thread Name: pool-1-thread-5 is receiving message.....
                 * 接收到消息:3
                 * Thread Name: pool-2-thread-5 is receiving message.....
                 * 接收到消息:4
                 */
                System.out.println("Thread Name: " + Thread.currentThread().getName() + " is receiving message.....");
                System.out.println("接收到消息:" + receivedMessage);
            };
            CancelCallback cancelCallback = (consumerTag) -> {
                System.out.println(consumerTag + "消费者取消消费接口回调逻辑");
            };
            System.out.println("Thread Name: " + Thread.currentThread().getName() + " is working..... receiving message.....");
            channel.basicConsume(CommonUtils.QUEUE_NAME, true, deliverCallback, cancelCallback);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
