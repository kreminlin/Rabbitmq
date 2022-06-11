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

    /**
     * 批量发消息的数量
     */
    public static final Integer MESSAGE_COUTN = 1000;
}
