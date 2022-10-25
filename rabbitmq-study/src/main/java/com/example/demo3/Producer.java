package com.example.demo3;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.nio.charset.StandardCharsets;

/**
 * Producer. 广播模式
 * 类似网络中的广播报文
 *
 * @author Thou
 * @date 2022/10/25
 */
public class Producer {

    public static String QUEUE1_NAME = "demo3_broadcast_1";
    public static String QUEUE2_NAME = "demo3_broadcast_2";
    public static String EXANME = "fanoutExchange";

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

            // 声明交换机
            channel.exchangeDeclare(EXANME, BuiltinExchangeType.FANOUT);

            channel.queueDeclare(QUEUE1_NAME, false, false, true, null);
            channel.queueDeclare(QUEUE2_NAME, false, false, true, null);

            // 队列与交换机绑定
            channel.queueBind(QUEUE1_NAME, EXANME, "");
            channel.queueBind(QUEUE2_NAME, EXANME, "");

            for (int i = 0; i < 10; i++) {
                String msg = "broadcast demo3 " + i;
                channel.basicPublish(EXANME, "", null, msg.getBytes(StandardCharsets.UTF_8));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
