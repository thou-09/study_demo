package com.example;

import redis.clients.jedis.Jedis;

/**
 * RedisDemo2.<br/>
 * 使用 redis 实现分布式锁<br/>
 * 保证谁加锁谁释放锁
 *
 * @author Thou
 * @date 2022/10/22
 */
public class RedisDemo2 {

    public static String LOCK_SUCCESS = "OK";

    public static Long waitTimeOut = 10_000L;

    public static Integer timeOut = 10_000;

    /**
     * 获取分布式锁
     *
     * @param k -
     * @param v -
     * @return boolean
     * @author Thou
     * @date 2022/10/22
     */
    public static boolean getLock(String k, String v) {
        try {
            Jedis jedis = new Jedis("127.0.0.1", 6379);
            long endTime = System.currentTimeMillis() + waitTimeOut;
            while (System.currentTimeMillis() < endTime) {
                String result = jedis.set(k, v, "NX", "EX", timeOut);
                if (LOCK_SUCCESS.equals(result)) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 释放锁
     *
     * @param k -
     * @author Thou
     * @date 2022/10/22
     */
    public static void unlock(String k, String requestId) {
        try {
            Jedis jedis = new Jedis("127.0.0.1", 6379);
            if (requestId.equals(jedis.get(k))) {
                System.out.println("释放锁的是（" + requestId + "）释放了" + Thread.currentThread().getName());
                jedis.del(k);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
