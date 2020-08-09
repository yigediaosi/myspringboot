package com.net.yoga.redis;

import com.net.yoga.redis.base.BaseSingleRedis;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * RedisAop：在执行完redis相关操作后，释放redis连接
 */
@Aspect
@Slf4j
public class RedisAop {
    @Autowired
    private BaseSingleRedis baseSingleRedis;

    @After("@annotation(com.net.yoga.redis.annotation.JedisWay) || @within(com.net.yoga.redis.annotation.JedisWay)")
    public void doAfter(JoinPoint jp) {
        baseSingleRedis.releaseJedis();
    }

    @AfterThrowing("@annotation(com.net.yoga.redis.annotation.JedisWay) || @within(com.net.yoga.redis.annotation" +
            ".JedisWay)")
    public void afterThrowing() {
        log.info("Release brokenJedis after Throwing Exception!");
        baseSingleRedis.releaseJedis();
    }
}
