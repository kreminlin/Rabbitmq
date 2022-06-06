package com.atguigu.rabbitMq.demo.messageanswer;

import com.atguigu.rabbitMq.constant.CommonUtils;
import com.atguigu.rabbitMq.utils.RabbitMqUtils;
import com.atguigu.rabbitMq.utils.SleepUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

/**
 * 消费者02
 *
 * @author Hanser
 */
public class Work04 implements Runnable {

    public static void main(String[] args) {
        try {
            Channel channel = RabbitMqUtils.getChannel();
            System.out.println("C2 等待接收消息处理时间较长");
            //消息消费的时候如何处理消息
            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                SleepUtils.sleep(30);
                String message = new String(delivery.getBody());
                System.out.println("接收到消息:" + message);
                /**
                 * 1.消息标记 tag
                 * 2.是否批量应答未应答消息
                 */
                channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
            };
            //采用手动应答
            boolean autoAck = false;
            /**
             * 不设置 basicQos的话是一次性平均分发给所有的队列，设置之后限制了一次分发消息的数量，在设置手动确认机制，
             * 这样在你没有提交已经处理好的消息的时候是不会给你分发消息的。实现的不公平分发。
             *
             * 如果我们将qos的值设为1，那么你想一想会出现什么情况呢？信道中只允许传输一条消息，
             * 那么当这条消息处理完后，队列会立马发送下一条消息，所以这个时候快的不断处理，
             * 慢的等待当前处理完再处理下一条。这样就实现了能者多劳。
             */
            //这个是设置信道容量的大小，采用轮询的方式往信道放消息，信道满了就跳过！！！！
            channel.basicQos(1);
            channel.basicConsume(CommonUtils.ACK_QUEUE_NAME, autoAck, deliverCallback, (consumerTag) -> {
                System.out.println(consumerTag + "消费者取消消费接口回调逻辑");
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            Channel channel = RabbitMqUtils.getChannel();
            System.out.println("C2 等待接收消息处理时间较长");
            //消息消费的时候如何处理消息
            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                SleepUtils.sleep(15);
                String message = new String(delivery.getBody());
                System.out.println("接收到消息:" + message);
                /**
                 * 1.消息标记 tag
                 * 2.是否批量应答未应答消息
                 */
                channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
            };
            //采用手动应答
            boolean autoAck = false;
            channel.basicConsume(CommonUtils.ACK_QUEUE_NAME, autoAck, deliverCallback, (consumerTag) -> {
                System.out.println(consumerTag + "消费者取消消费接口回调逻辑");
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
