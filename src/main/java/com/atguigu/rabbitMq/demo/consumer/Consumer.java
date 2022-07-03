package com.atguigu.rabbitMq.demo.consumer;

import com.atguigu.rabbitMq.config.ConfirmConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * 接收消息
 */
@Component
@Slf4j
public class Consumer {

    @RabbitListener(queues = ConfirmConfig.CONFIRM_QUEUE_NAME)
    public void receiveMessage(Message message) {
        System.out.println(message.getBody());
        log.info("接收到的消息: {}", message.getBody());
    }

}
