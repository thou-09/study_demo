package com.example.demo6;

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
 * Consumer1.
 *
 * @author Thou
 * @date 2022/10/25
 */
public class Consumer1 {

    public static Logger logger = Logger.getLogger(Consumer1.class.getName());

    public static String EXANME = "header";
    public static String QUEUE1 = "queue1";

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
            channel.exchangeDeclare(EXANME, BuiltinExchangeType.HEADERS);

            channel.queueDeclare(QUEUE1, false, false, true, null);
            Map<String, Object> map = new HashMap<>(4);
            map.put("x-match", "any");
            map.put("key1", "value1");
            channel.queueBind(QUEUE1, EXANME, "", map);

            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String msg = new String(delivery.getBody(), StandardCharsets.UTF_8);
                logger.info("消费者[" + Consumer1.class.getName() + " x-match=any], 消息主题[" + msg + "]");
            };
            CancelCallback cancelCallback = (consumerTag) -> {
                logger.info("消息被取消...");
            };
            channel.basicConsume(QUEUE1, true, deliverCallback, cancelCallback);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
