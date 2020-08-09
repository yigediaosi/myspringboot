package com.net.yoga.redis.base;

import com.net.yoga.common.constant.CommonConstant;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component
@Data
@Slf4j
@ConfigurationProperties(prefix = "redis.cluster")
public class BaseClusterRedis {
    private JedisCluster jedisCluster;
    private GenericObjectPoolConfig config;

    @Value("${redis.mode}")
    private String mode;
    private Integer maxTotal;
    private Integer maxIdle;
    private Integer minIdle;
    private Long maxWaitMillis;
    private String host;
    private Integer timeout;
    private Integer maxRedirection;

    /**
     * spring注入的时候立即执行初始化
     */
    @PostConstruct
    public void initPool() {
        if (CommonConstant.REDIS_MODE_CLUSTER.equals(mode)) {
            if (config == null) {
                config = new GenericObjectPoolConfig();
                // Maximum active connections to RedisClient instance
                config.setMaxTotal(maxTotal);
                // Number of connections to RedisClient that just sit there and do nothing
                config.setMaxIdle(maxIdle);
                // Minimum number of idle connections to RedisClient
                // these can be seen as always open and ready to serve
                config.setMinIdle(minIdle);
                config.setMaxWaitMillis(maxWaitMillis);//ms
            }
            if (null == jedisCluster) {
                Set<HostAndPort> hostAndPortSet = new HashSet();
                String[] hpArray = host.split(CommonConstant.COMMON_ESCAPE_STR + CommonConstant.COMMA_SPLIT_STR);
                for (String hp : hpArray) {
                    String[] ipAndPort = hp.split(CommonConstant.COMMON_ESCAPE_STR + CommonConstant.COMMON_COLON_STR);
                    HostAndPort hap = new HostAndPort(ipAndPort[0], Integer.parseInt(ipAndPort[1]));
                    hostAndPortSet.add(hap);
                }
                jedisCluster = new JedisCluster(hostAndPortSet, timeout, timeout, maxRedirection, config);
            }
            log.info("BaseClusterRedis initPool over.");
        }
    }

    public static void main(String[] args) throws InterruptedException {
        final BaseClusterRedis baseClusterRedis = new BaseClusterRedis();
        baseClusterRedis.initPool();
        for (int i = 0; i < 20; i++) {
            JedisCluster jedisCluster = baseClusterRedis.getJedisCluster();
            jedisCluster.set(String.valueOf(i), String.valueOf(i));
            System.out.println("set " + i + ". value:" + jedisCluster.get(String.valueOf(i)));
            TimeUnit.MILLISECONDS.sleep(500);

        }
    }
}
