package com.example.demo5;

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
public class Consumer2 {

    public static Logger logger = Logger.getLogger(Consumer2.class.getName());

    public static String EXANME = "topic";
    public static String QUEUE2 = "queue2";

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
            channel.exchangeDeclare(EXANME, BuiltinExchangeType.TOPIC);

            channel.queueDeclare(QUEUE2, false, false, true, null);
            channel.queueBind(QUEUE2, EXANME, "cn.*");

            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String msg = new String(delivery.getBody(), StandardCharsets.UTF_8);
                logger.info("消费者[" + Consumer2.class.getName() + " cn.*], 消息主题[" + msg + "]");
            };
            CancelCallback cancelCallback = (consumerTag) -> {
                logger.info("消息被取消...");
            };
            channel.basicConsume(QUEUE2, true, deliverCallback, cancelCallback);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
