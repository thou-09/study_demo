package com.example.demo1;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.nio.charset.StandardCharsets;

/**
 * Producer.
 *
 * @author Thou
 * @date 2022/10/25
 */
public class Producer {

    public static String QUEUE_NAME = "demo1";

    public static void main(String[] args) {
        try {
            // 创建连接工厂
            ConnectionFactory factory = new ConnectionFactory();
            // 设置 MQ 服务器地址
            factory.setHost("127.0.0.1");
            // 用户名称
            factory.setUsername("guest");
            // 密码
            factory.setPassword("guest");
            // 设置端口号
            factory.setPort(5672);
            // 设置虚拟机
            factory.setVirtualHost("/");
            // 获取连接对象
            Connection conn = factory.newConnection();
            // 创建并获取一个信道
            Channel channel = conn.createChannel();
            /*
                生成一个队列
                1. 队列名称
                2. 是否持久化
                3. 是否排他
                    true 只有同一个 Connection 创建的 Channel 才能消费
                         当 Connection 断开时，会直接删除队列，优先级高于不自动删除
                4. 是否自动删除
                    true 所有消费者断开才会删除队列
                    false 需要手动指令清空 reset
                5. 队列的其他参数
             */
            channel.queueDeclare(QUEUE_NAME, false, false, true, null);
            /*
                将消息存入队列中
                1. 交换机名称
                2. 路由 key
                3. 消息的其他参数
                4. 消息体
             */
            channel.basicPublish("", QUEUE_NAME, null, "Hello demo1.".getBytes(StandardCharsets.UTF_8));
            channel.basicPublish("", QUEUE_NAME, null, "Hello demo1..".getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
