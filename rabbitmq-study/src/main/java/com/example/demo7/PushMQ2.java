package com.example.demo7;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmCallback;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * PushMQ2.
 *
 * @author Thou
 * @date 2022/10/25
 */
public class PushMQ2 {

    public static String QUEUE = "queue2";

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

            /*
                线程安全的有序跳表集合
                1. 轻松关联标记消息
                2. 支持批量清除消息
                3. 支持并发访问
             */
            ConcurrentSkipListMap<Long, String> outMsg = new ConcurrentSkipListMap<>();

            /*
                定义确认发布的回调方法
                1. 消息的序列号
                2. 是否批量处理
             */
            ConfirmCallback confirmCallback = (seqNum, multiple) -> {
                System.out.println("ACK[" + seqNum + "], 批量处理[" + multiple + "]");
                if (multiple) {
                    outMsg.headMap(seqNum, true).clear();;
                } else {
                    outMsg.remove(seqNum);
                }
            };

            /*
                未确认回调
             */
            ConfirmCallback unConfirmCallback = (seqNum, multiple) -> {
                System.out.println("ACK[" + seqNum + "], 消息体[" + outMsg.get(seqNum) + "]");
            };

            channel.addConfirmListener(confirmCallback, unConfirmCallback);

            long start = System.currentTimeMillis();
            for (int i = 0; i < 1000; i++) {
                String msg = "demo7 " + i;
                outMsg.put(channel.getNextPublishSeqNo(), msg);
                channel.basicPublish("", QUEUE, null, msg.getBytes(StandardCharsets.UTF_8));
            }
            long end = System.currentTimeMillis();
            System.out.println("发送 1000 条消息的确认时间 " + (end - start) + "毫秒");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
