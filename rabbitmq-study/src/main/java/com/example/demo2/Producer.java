package com.example.demo2;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.nio.charset.StandardCharsets;

/**
 * Producer. 工作模式
 * 使用默认交换机
 * 多个终端监听一个队列
 * 采用轮询方法
 *
 * @author Thou
 * @date 2022/10/25
 */
public class Producer {

    public static String QUEUE_NAME = "demo2_work";

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

            for (int i = 0; i < 5; i++) {
                String msg = "Hello demo2 " + i;
                channel.basicPublish("", QUEUE_NAME, null, msg.getBytes(StandardCharsets.UTF_8));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
