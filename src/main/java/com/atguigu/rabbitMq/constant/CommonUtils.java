package com.atguigu.rabbitMq.constant;

/**
 * 静态常量类
 *
 * @author Hanser
 */
public class CommonUtils {
    /**
     * 消息队列名称
     */
    public static final String QUEUE_NAME = "HELLO";

    /**
     * 消息应答队列名称
     */
    public static final String TASK_QUEUE_NAME = "ack_queue";

    /**
     * 消息应答队列名称
     */
    public static final String ACK_QUEUE_NAME = "ack_queue";
    public static final String NORMAL_EXCHANGE_QUEUE = "normal_queue";
    public static final String DEAD_EXCHANGE_QUEUE = "dead_queue";

    /**
     * 批量发消息的数量
     */
    public static final Integer MESSAGE_COUTN = 1000;

    /**
     * 声明一个交换机名称
     */
    public static final String EXCHANGE_NAME = "logs";
    public static final String DIRECT_EXCHANGE_NAME = "direct_logs";
    public static final String TOPIC_EXCHANGE_NAME = "topic_logs";
    public static final String NORMAL_EXCHANGE_NAME = "normal_exchange";
    public static final String DEAD_EXCHANGE_NAME = "dead_exchange";
}
