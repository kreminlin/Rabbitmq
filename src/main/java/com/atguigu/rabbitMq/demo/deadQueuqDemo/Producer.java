package com.atguigu.rabbitMq.demo.deadQueuqDemo;

import com.atguigu.rabbitMq.constant.CommonUtils;
import com.atguigu.rabbitMq.utils.RabbitMqUtils;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;

/**
 * 私信队里生产者
 *
 * @author Hanser
 */
public class Producer {
    public static void main(String[] argv) throws Exception {
        try (Channel channel = RabbitMqUtils.getChannel()) {
            channel.exchangeDeclare(CommonUtils.NORMAL_EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
            //设置消息的 TTL 时间
//            AMQP.BasicProperties properties = new AMQP.BasicProperties().builder().expiration("10000").build();
            //该信息是用作演示队列个数限制
            for (int i = 1; i < 11; i++) {
                String message = "info" + i;
                channel.basicPublish(CommonUtils.NORMAL_EXCHANGE_NAME, "zhangsan", null,
                        message.getBytes());
                System.out.println("生产者发送消息:" + message);
            }
        }
    }
}
