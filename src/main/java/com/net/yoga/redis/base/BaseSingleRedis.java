package com.net.yoga.redis.base;

import com.net.yoga.common.constant.CommonConstant;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Component
@Data
@Slf4j
@ConfigurationProperties(prefix = "redis.single")
public class BaseSingleRedis {
    //使用threadLocal避免释放的时候传递jedis对象
    private static ThreadLocal<Jedis> jedisLocal = new ThreadLocal<>();
    private JedisPool jedisPool;
    private JedisPoolConfig config;

    @Value("${redis.mode}")
    private String mode;
    private Integer maxTotal;
    private Integer maxIdle;
    private Integer minIdle;
    private Long maxWaitMillis;
    private String host;
    private Integer port;
    private Integer timeout;
    private String password;

    @PostConstruct
    public void initPool() {
        if (StringUtils.isBlank(mode) || CommonConstant.REDIS_MODE_SINGLE.equals(mode)) {
            if (config == null) {
                config = new JedisPoolConfig();
                // Maximum active connections to RedisClient instance
                config.setMaxTotal(maxTotal);
                // Number of connections to RedisClient that just sit there and do nothing
                config.setMaxIdle(maxIdle);
                // Minimum number of idle connections to RedisClient
                // these can be seen as always open and ready to serve
                config.setMinIdle(minIdle);
                config.setMaxWaitMillis(maxWaitMillis);//ms
            }
            if (null == jedisPool) {
                jedisPool = new JedisPool(config, host, port, timeout, password);
            }
            log.info("BaseSingleRedis initPool over.");
        }
    }

    @PreDestroy
    public void destroy() {
        if (this.jedisPool != null) {
            try {
                this.jedisPool.destroy();
            } catch (Exception var2) {
                log.error("exception while destroy jedisPool", var2);
            }
        }
    }

    public Jedis getJedis() {
        Jedis jedis = null;
        try {
            jedis = jedisLocal.get();
            if (jedis != null) {
                try {
                    if (jedis.isConnected()) {
                        return jedis;
                    }
                } catch (Exception var3) {
                    this.releaseJedis();
                }
            }

            if (jedisPool == null) {
                this.initPool();
            }

            jedis = jedisPool.getResource();
            jedisLocal.set(jedis);
        } catch (Exception var4) {
            this.releaseJedis();
            if (jedisLocal.get() != null) {
                jedisLocal.remove();
            }
            log.error("Could not get a resource from the pool, pls check the host and port settings", var4);
        }

        return jedis;
    }

    public void releaseJedis() {
        Jedis jedis = jedisLocal.get();
        if (jedis != null) {
            jedis.close();
        }
        jedisLocal.remove();
    }

    public void setJedisPool(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    public void setConfig(JedisPoolConfig config) {
        this.config = config;
    }

    public static void main(String[] args) throws InterruptedException {
        final BaseSingleRedis baseSingleRedis = new BaseSingleRedis();
        JedisPoolConfig config = new JedisPoolConfig();
        // Maximum active connections to RedisClient instance
        config.setMaxTotal(5);
        // Number of connections to RedisClient that just sit there and do nothing
        config.setMaxIdle(5);
        // Minimum number of idle connections to RedisClient
        // these can be seen as always open and ready to serve
        config.setMinIdle(1);
        config.setMaxWaitMillis(1000);//ms
        JedisPool jedisPool = new JedisPool(config, "127.0.0.1", 6379, 10000, "123456");
        baseSingleRedis.setConfig(config);
        baseSingleRedis.setJedisPool(jedisPool);
        for (int i = 0; i < 20; i++) {
            Jedis jedis = baseSingleRedis.getJedis();
//            Jedis jedis = jedisPool.getResource();
            jedis.set(String.valueOf(i), String.valueOf(i));
//            jedis.close();
            System.out.println("set " + i + ". value:" + jedis.get(String.valueOf(i)));
//            TimeUnit.MILLISECONDS.sleep(500);
        }
    }
}
