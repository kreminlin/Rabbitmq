package com.atguigu.rabbitMq.demo.messageanswer;

/**
 * 消息应答练习
 *  当有其中一个消费者应答队列挂掉时，会消息重新入队列，由空余消费者处理。
 * @author Hanser
 */
public class MessageReply {

    public static void main(String[] args) {
        Work03 work03 = new Work03();
        Work04 work04 = new Work04();
        Thread thread1 = new Thread(work03);
        Thread thread2 = new Thread(work04);
        thread1.start();
        thread2.start();
    }
}
