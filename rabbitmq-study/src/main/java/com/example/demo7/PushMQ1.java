package com.example.demo7;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.nio.charset.StandardCharsets;

/**
 * PushMQ1.
 *
 * @author Thou
 * @date 2022/10/25
 */
public class PushMQ1 {

    public static String QUEUE = "queue1";

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

            channel.queueDeclare(QUEUE, false, true, true, null);
            // 开启发布确认模式
            channel.confirmSelect();

            long start = System.currentTimeMillis();
            for (int i = 0; i < 1000; i++) {
                String msg = "demo7 " + i;
                channel.basicPublish("", QUEUE, null, msg.getBytes(StandardCharsets.UTF_8));
                // 确认消息是否发送成功
                boolean flag = channel.waitForConfirms();
                System.out.println("发送" + (flag ? "成功" : "失败"));
            }
            long end = System.currentTimeMillis();
            System.out.println("发送 1000 条消息的确认时间 " + (end - start) + "毫秒");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
