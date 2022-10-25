package com.example.demo4;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.nio.charset.StandardCharsets;

/**
 * Producer. 路由模式
 * 基于广播模式，但每个队列都有特定的路由 key
 * 在生产者推送消息时，每一个消息也会带一个路由 key
 * 交换机在推送消息时，会对比消息中的路由 key，选择与之相对于的队列推送消息
 *
 * @author Thou
 * @date 2022/10/25
 */
public class Producer {

    public static String EXANME = "routing";
    public static String[] routingKeys = {"one", "two", "all"};

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

            channel.exchangeDeclare(EXANME, BuiltinExchangeType.DIRECT);

            for (int i = 0; i < 3; i++) {
                String msg = "routing " + routingKeys[0] + " demo4 " + i;
                channel.basicPublish(EXANME, routingKeys[0], null, msg.getBytes(StandardCharsets.UTF_8));
            }

            for (int i = 0; i < 3; i++) {
                String msg = "routing " + routingKeys[1] + " demo4 " + i;
                channel.basicPublish(EXANME, routingKeys[1], null, msg.getBytes(StandardCharsets.UTF_8));
            }

            for (int i = 0; i < 3; i++) {
                String msg = "routing " + routingKeys[2] + " demo4 " + i;
                channel.basicPublish(EXANME, routingKeys[2], null, msg.getBytes(StandardCharsets.UTF_8));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
