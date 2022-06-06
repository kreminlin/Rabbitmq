package com.atguigu.rabbitMq.demo.messageanswer;

import com.atguigu.rabbitMq.constant.CommonUtils;
import com.atguigu.rabbitMq.utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;

import java.util.Scanner;

/**
 * 消息生产者
 *
 * @author Hanser
 */
public class Task02 {
    public static void main(String[] argv) throws Exception {
        try (Channel channel = RabbitMqUtils.getChannel()) {
            /**
             * 队列持久化
             */
            boolean durable = true;
            channel.queueDeclare(CommonUtils.TASK_QUEUE_NAME, true, false, false, null);
            Scanner sc = new Scanner(System.in);
            System.out.println("请输入信息");
            while (sc.hasNext()) {
                String message = sc.nextLine();
                /**
                 * 要想让消息实现持久化需要在消息生产者修改代码，MessageProperties.PERSISTENT_TEXT_PLAIN 添加这个属性
                 * 保存到磁盘上，不写保存到内存中
                 */
                channel.basicPublish("", CommonUtils.TASK_QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes("UTF-8"));
                System.out.println("生产者发出消息" + message);
            }
        }
    }
}
