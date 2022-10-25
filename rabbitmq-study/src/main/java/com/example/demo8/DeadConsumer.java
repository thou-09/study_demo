package com.example.demo8;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * DeadConsumer.
 *
 * @author Thou
 * @date 2022/10/25
 */
public class DeadConsumer {

    public static Logger logger = Logger.getLogger(DeadConsumer.class.getName());

    // 死信交换机
    public static String DEAD_EXCHANGE = "dead_ex";
    // 死信路由
    public static String DEAD_ROUTINGKEY = "dead";
    // 死信队列
    public static String DEAD_QUEUE = "dead_queue";

    public static void main(String[] args) {
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("127.0.0.1");
            factory.setUsername("guest");
            factory.setPassword("guest");
            factory.setPort(5672);
            factory.setVirtualHost("/");
            Connection conn = factory.newConnection();
            Channel channel = conn.createChannel();

            // 声明死信队列
            channel.queueDeclare(DEAD_QUEUE, false, false, false, null);
            // 绑定死信交换机和死信队列
            channel.queueBind(DEAD_QUEUE, DEAD_EXCHANGE, DEAD_ROUTINGKEY);

            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String msg = new String(delivery.getBody(), StandardCharsets.UTF_8);
                logger.info("消费者[" + DeadConsumer.class.getName() + "], 消息主题[" + msg + "]");
            };
            CancelCallback cancelCallback = (consumerTag) -> {
                logger.info("消息被取消...");
            };
            channel.basicConsume(DEAD_QUEUE, true, deliverCallback, cancelCallback);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
