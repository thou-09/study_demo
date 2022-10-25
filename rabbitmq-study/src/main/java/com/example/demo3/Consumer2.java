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
 * Consumer2.
 *
 * @author Thou
 * @date 2022/10/25
 */
public class Consumer2 {

    public static Logger logger = Logger.getLogger(Consumer2.class.getName());

    public static String QUEUE2_NAME = "demo3_broadcast_2";
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
            channel.queueDeclare(QUEUE2_NAME, false, false, true, null);
            channel.queueBind(QUEUE2_NAME, EXANME, "");

            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String msg = new String(delivery.getBody(), StandardCharsets.UTF_8);
                logger.info("消费者[" + Consumer2.class.getName() + "], 消息主题[" + msg + "]");
            };
            CancelCallback cancelCallback = (consumerTag) -> {
                logger.info("消息被取消...");
            };
            channel.basicConsume(QUEUE2_NAME, true, deliverCallback, cancelCallback);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
