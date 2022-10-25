package com.example.demo6;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * Producer. 头部模式
 * 等价于 topic 模式，不同在于头部模式取消了对路由 key 的使用
 * 该使用头部的 k-v 键值对进行匹配
 * 在声明消息时需要指定头部信息 x-match
 * [all] 所有键值对都能匹配上才推送消息
 * [any] 只要有任意一个能匹配上就推送消息
 *
 * @author Thou
 * @date 2022/10/25
 */
public class Producer {

    public static String EXANME = "header";

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

            for (int i = 0; i < 5; i++) {
                String msg = "header [key1=value1] demo6 " + i;
                Map<String, Object> map = new HashMap<>(2);
                map.put("key1", "value1");
                AMQP.BasicProperties.Builder properties = new AMQP.BasicProperties.Builder();
                properties.headers(map);
                channel.basicPublish(EXANME, "", properties.build(), msg.getBytes(StandardCharsets.UTF_8));
            }

            for (int i = 0; i < 5; i++) {
                String msg = "header [key1=value1, key2=value2] demo6 " + i;
                Map<String, Object> map = new HashMap<>(4);
                map.put("key1", "value1");
                map.put("key2", "value2");
                AMQP.BasicProperties.Builder properties = new AMQP.BasicProperties.Builder();
                properties.headers(map);
                channel.basicPublish(EXANME, "", properties.build(), msg.getBytes(StandardCharsets.UTF_8));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
