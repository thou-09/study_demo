package com.example.demo5;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.nio.charset.StandardCharsets;

/**
 * Producer. 主题模式
 * 基于路由模式，只是在路由 key 的对比上，采用模糊匹配代替等价匹配
 * [#] 匹配 0 个或多个单词，单词以 [.] 分割 com.example com.example.demo
 * [*] 匹配 1 个单词 com.example
 *
 * @author Thou
 * @date 2022/10/25
 */
public class Producer {

    public static String QUEUE1 = "queue1";
    public static String QUEUE2 = "queue2";
    public static String EXANME = "topic";

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
            channel.queueDeclare(QUEUE1, false, false, true, null);
            channel.queueDeclare(QUEUE2, false, false, true, null);

            channel.queueBind(QUEUE1, EXANME, "com.#.one.#");
            channel.queueBind(QUEUE2, EXANME, "cn.*");

            for (int i = 0; i < 3; i++) {
                String msg = "topic com.#.one.# demo5 " + i;
                channel.basicPublish(EXANME, "com.one", null, msg.getBytes(StandardCharsets.UTF_8));
            }

            for (int i = 0; i < 3; i++) {
                String msg = "topic cn.one demo5 " + i;
                channel.basicPublish(EXANME, "cn.one", null, msg.getBytes(StandardCharsets.UTF_8));
            }

            for (int i = 0; i < 3; i++) {
                // 不匹配的信息
                String msg = "topic cn.one.two demo5 " + i;
                channel.basicPublish(EXANME, "cn.one.two", null, msg.getBytes(StandardCharsets.UTF_8));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
