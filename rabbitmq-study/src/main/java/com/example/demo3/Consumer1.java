package com.example.demo3;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;

/**
 * Consumer1.
 *
 * @author Thou
 * @date 2022/10/25
 */
public class Consumer1 {

    public static Logger logger = Logger.getLogger(Consumer1.class.getName());

    public static String QUEUE1_NAME = "demo3_broadcast_1";
    public static String EXANME = "fanoutExchange";

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
            channel.exchangeDeclare(EXANME, BuiltinExchangeType.FANOUT);
            channel.queueDeclare(QUEUE1_NAME, false, false, true, null);
            channel.queueBind(QUEUE1_NAME, EXANME, "");

            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String msg = new String(delivery.getBody(), StandardCharsets.UTF_8);
                logger.info("消费者[" + Consumer1.class.getName() + "], 消息主题[" + msg + "]");
            };
            CancelCallback cancelCallback = (consumerTag) -> {
                logger.info("消息被取消...");
            };
            channel.basicConsume(QUEUE1_NAME, true, deliverCallback, cancelCallback);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
