package com.example;

import redis.clients.jedis.Jedis;

/**
 * RedisDemo1.<br/>
 * 使用 redis 实现分布式锁
 *
 * @author Thou
 * @date 2022/10/22
 */
public class RedisDemo1 {

    /**
     * 锁自旋重试等待时间
     */
    public static Long waitTimeOut = 10_000L;

    /**
     * 防止锁一致占用的超时时间
     */
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
            // 声明 jedis 客户端
            Jedis jedis = new Jedis("127.0.0.1", 6379);
            // 计算获取锁的时间
            long endTime = System.currentTimeMillis() + waitTimeOut;
            // 尝试获取锁，获取不到则自旋重试
            while (System.currentTimeMillis() < endTime) {
                // 判断是否能拿到锁对象
                if (jedis.setnx(k, v) == 1) {
                    jedis.expire(k, timeOut);
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
    public static void unlock(String k) {
        try {
            Jedis jedis = new Jedis("127.0.0.1", 6379);
            jedis.del(k);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
