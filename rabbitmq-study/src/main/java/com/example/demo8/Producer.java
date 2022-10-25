package com.example.demo8;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.nio.charset.StandardCharsets;

/**
 * Producer. 死信
 * 已经死亡的信息
 * 1. 消息生产者通过业务交换机将消息推送到业务队列上，如果触发了死信条件
 * 2. 则该信息通过在业务交换机中设置的死信交换机推送到死信队列上
 * 3. 通过绑定在该队列上的死信消费者将消息二次消费，避免消息积压
 *
 * 死信条件
 * 1. ttl 超时
 * 2. 消息队列到达上线，多余消息成为死信
 * 3. 被拒绝的消息（未被确认）
 *
 * @author Thou
 * @date 2022/10/25
 */
public class Producer {

    // 业务交换机
    public static String NORMAL_EXCHANGE = "normal_ex";

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

            channel.exchangeDeclare(NORMAL_EXCHANGE, BuiltinExchangeType.DIRECT);
            // 设置消息过期时间 ttl
            AMQP.BasicProperties properties = new AMQP.BasicProperties()
                    .builder()
                    .expiration("10000")
                    .build();
            for (int i = 0; i < 10; i++) {
                channel.basicPublish(NORMAL_EXCHANGE, "service", properties, ("ttl 过期消息" + i).getBytes(StandardCharsets.UTF_8));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
