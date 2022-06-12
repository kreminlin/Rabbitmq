package com.atguigu.rabbitMq.demo.exchangeModel;

import com.atguigu.rabbitMq.constant.CommonUtils;
import com.atguigu.rabbitMq.utils.RabbitMqUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;

import java.util.Scanner;

/**
 * 生产者
 *
 * @author Hanser
 */
public class DirectLogs {
    public static void main(String[] argv) throws Exception {
        try (Channel channel = RabbitMqUtils.getChannel()) {
            /**
             * 声明一个 exchange
             * 1.exchange 的名称
             * 2.exchange 的类型
             */
//            channel.exchangeDeclare("1", "fanout");
            channel.exchangeDeclare(CommonUtils.DIRECT_EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
            Scanner sc = new Scanner(System.in);
            System.out.println("请输入信息");
            while (sc.hasNext()) {
                String message = sc.nextLine();
                channel.basicPublish(CommonUtils.DIRECT_EXCHANGE_NAME, "error", null, message.getBytes("UTF-8"));
                System.out.println("生产者发出消息" + message);
            }
        }
    }
}
