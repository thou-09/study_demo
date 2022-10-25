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
 * Consumer1.
 *
 * @author Thou
 * @date 2022/10/25
 */
public class Consumer1 {

    public static Logger logger = Logger.getLogger(Consumer1.class.getName());

    // 业务交换机
    public static String NORMAL_EXCHANGE = "normal_ex";
    // 业务队列
    public static String NORMAL_QUEUE = "normal_queue";
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

            // 声明业务交换机
            channel.exchangeDeclare(NORMAL_EXCHANGE, BuiltinExchangeType.DIRECT);
            // 声明死信交换机
            channel.exchangeDeclare(DEAD_EXCHANGE, BuiltinExchangeType.DIRECT);
            // 声明死信队列
            channel.queueDeclare(DEAD_QUEUE, false, false, false, null);
            // 绑定死信交换机和死信队列
            channel.queueBind(DEAD_QUEUE, DEAD_EXCHANGE, DEAD_ROUTINGKEY);
            // 核心配置，绑定死信
            Map<String, Object> params = new HashMap<>();
            params.put("x-dead-letter-exchange", DEAD_EXCHANGE);
            params.put("x-dead-letter-routing-key", DEAD_ROUTINGKEY);

            // 队列过期时间
            // params.put("x-message-ttl", 10000);
            // 设置队列最大长度
            // params.put("x-max-length", 8);

            // 业务队列设置
            channel.queueDeclare(NORMAL_QUEUE, false, false, false, params);
            channel.queueBind(NORMAL_QUEUE, NORMAL_EXCHANGE, "service");

            // // 开启发布确认模式
            // channel.confirmSelect();

            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String msg = new String(delivery.getBody(), StandardCharsets.UTF_8);
                logger.info("消费者[" + Consumer1.class.getName() + "], 消息主题[" + msg + "]");

                // if (delivery.getEnvelope().getDeliveryTag() >= 5) {
                //     msg = new String(delivery.getBody(), StandardCharsets.UTF_8);
                //     logger.info("消费者[" + Consumer1.class.getName() + "], 确认消息主题[" + msg + "]");
                //     channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
                // } else {
                //     msg = new String(delivery.getBody(), StandardCharsets.UTF_8);
                //     logger.info("消费者[" + Consumer1.class.getName() + "], 未确认消息主题[" + msg + "]");
                //     channel.basicReject(delivery.getEnvelope().getDeliveryTag(), false);
                // }
            };
            CancelCallback cancelCallback = (consumerTag) -> {
                logger.info("消息被取消...");
            };
            channel.basicConsume(NORMAL_QUEUE, true, deliverCallback, cancelCallback);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
