package com.example.demo1;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Envelope;

import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;

/**
 * Consumer.
 *
 * @author Thou
 * @date 2022/10/25
 */
public class Consumer {

    public static Logger logger = Logger.getLogger(Consumer.class.getName());

    public static String QUEUE_NAME = "demo1";

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
            channel.queueDeclare(QUEUE_NAME, false, false, true, null);

            // 消费成功时回调
            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                // 获取信件对象
                Envelope envelope = delivery.getEnvelope();
                // 获取消息推送的交换机
                String exchangeName = envelope.getExchange();
                // 获取路由 key
                String routingKey = envelope.getRoutingKey();
                // 接收标记，每次推送 +1
                long deliveryTag = envelope.getDeliveryTag();
                // 消息主体
                String msg = new String(delivery.getBody(), StandardCharsets.UTF_8);
                logger.info("交换机名[" + exchangeName + "], 路由 key [" + routingKey + "], 接收标记[" + deliveryTag + "], 消息主题[" + msg + "]");
            };
            // 消息取消回调
            CancelCallback cancelCallback = (consumerTag) -> {
                logger.info("消息被取消...");
            };
            /*
                消费者声明
                1. 队列名称
                2. 自动应答
                3. 成功回调
                4. 取消回调
             */
            channel.basicConsume(QUEUE_NAME, true, deliverCallback, cancelCallback);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
