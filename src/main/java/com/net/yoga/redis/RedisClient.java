package com.net.yoga.redis;

import com.net.yoga.common.constant.CommonConstant;
import com.net.yoga.common.exception.BusinessException;
import com.net.yoga.redis.base.BaseClusterRedis;
import com.net.yoga.redis.base.BaseSingleRedis;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPubSub;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Author: gaort
 * @Date: 2019/10/13 17:30
 */
@Data
@NoArgsConstructor
public class RedisClient {
    private Jedis jedis;
    private JedisCluster jedisCluster;

    public RedisClient(String mode, BaseSingleRedis baseSingleRedis, BaseClusterRedis baseClusterRedis) {
        if (StringUtils.isBlank(mode) || CommonConstant.REDIS_MODE_SINGLE.equals(mode)) {
            this.jedis = baseSingleRedis.getJedis();
        } else if (CommonConstant.REDIS_MODE_CLUSTER.equals(mode)) {
            this.jedisCluster = baseClusterRedis.getJedisCluster();
        } else {
            throw new BusinessException("参数有误，请确认");
        }
    }

    public Long zremrangeByScore(byte[] key, double min, double max) {
        return new CommonClient<Long>() {
            @Override
            public Long singleClient() {
                return jedis.zremrangeByScore(key, min, max);
            }

            @Override
            public Long clusterClient() {
                return jedisCluster.zremrangeByScore(key, min, max);
            }
        }.clientExecute();
    }

    public Long zremrangeByRank(byte[] key, long start, long stop) {
        return new CommonClient<Long>() {
            @Override
            public Long singleClient() {
                return jedis.zremrangeByRank(key, start, stop);
            }

            @Override
            public Long clusterClient() {
                return jedisCluster.zremrangeByRank(key, start, stop);
            }
        }.clientExecute();
    }

    public Long zadd(byte[] key, double score, byte[] member) {
        return new CommonClient<Long>() {
            @Override
            public Long singleClient() {
                return jedis.zadd(key, score, member);
            }

            @Override
            public Long clusterClient() {
                return jedisCluster.zadd(key, score, member);
            }
        }.clientExecute();
    }

    public Long zadd(String key, Map<String, Double> scoreMembers) {
        return new CommonClient<Long>() {
            @Override
            public Long singleClient() {
                return jedis.zadd(key, scoreMembers);
            }

            @Override
            public Long clusterClient() {
                return jedisCluster.zadd(key, scoreMembers);
            }
        }.clientExecute();
    }

    public Long zadd(byte[] key, Map<byte[], Double> scoreMembers) {
        return new CommonClient<Long>() {
            @Override
            public Long singleClient() {
                return jedis.zadd(key, scoreMembers);
            }

            @Override
            public Long clusterClient() {
                return jedisCluster.zadd(key, scoreMembers);
            }
        }.clientExecute();
    }

    public Set<byte[]> zrangeByScore(byte[] key, double min, double max) {
        return new CommonClient<Set<byte[]>>() {
            @Override
            public Set<byte[]> singleClient() {
                return jedis.zrangeByScore(key, min, max);
            }

            @Override
            public Set<byte[]> clusterClient() {
                return jedisCluster.zrangeByScore(key, min, max);
            }
        }.clientExecute();
    }

    public Set<byte[]> zrangeByScore(byte[] key, double min, double max, int offset, int count) {
        return new CommonClient<Set<byte[]>>() {
            @Override
            public Set<byte[]> singleClient() {
                return jedis.zrangeByScore(key, min, max, offset, count);
            }

            @Override
            public Set<byte[]> clusterClient() {
                return jedisCluster.zrangeByScore(key, min, max, offset, count);
            }
        }.clientExecute();
    }

    public Set<byte[]> zrevrangeByScore(byte[] key, double max, double min) {
        return new CommonClient<Set<byte[]>>() {
            @Override
            public Set<byte[]> singleClient() {
                return jedis.zrevrangeByScore(key, max, min);
            }

            @Override
            public Set<byte[]> clusterClient() {
                return jedisCluster.zrevrangeByScore(key, max, min);
            }
        }.clientExecute();
    }

    public Set<byte[]> zrevrange(byte[] key, long start, long stop) {
        return new CommonClient<Set<byte[]>>() {
            @Override
            public Set<byte[]> singleClient() {
                return jedis.zrevrange(key, start, stop);
            }

            @Override
            public Set<byte[]> clusterClient() {
                return jedisCluster.zrevrange(key, start, stop);
            }
        }.clientExecute();
    }

    public Set<byte[]> zrevrangeByScore(byte[] key, double max, double min, int offset, int count) {
        return new CommonClient<Set<byte[]>>() {
            @Override
            public Set<byte[]> singleClient() {
                return jedis.zrevrangeByScore(key, max, min, offset, count);
            }

            @Override
            public Set<byte[]> clusterClient() {
                return jedisCluster.zrevrangeByScore(key, max, min, offset, count);
            }
        }.clientExecute();
    }

    public byte[] get(byte[] key) {
        return new CommonClient<byte[]>() {
            @Override
            public byte[] singleClient() {
                return jedis.get(key);
            }

            @Override
            public byte[] clusterClient() {
                return jedisCluster.get(key);
            }
        }.clientExecute();
    }

    public String get(String key) {
        return new CommonClient<String>() {
            @Override
            public String singleClient() {
                return jedis.get(key);
            }

            @Override
            public String clusterClient() {
                return jedisCluster.get(key);
            }
        }.clientExecute();
    }

    public String set(byte[] key, byte[] value) {
        return new CommonClient<String>() {
            @Override
            public String singleClient() {
                return jedis.set(key, value);
            }

            @Override
            public String clusterClient() {
                return jedisCluster.set(key, value);
            }
        }.clientExecute();
    }

    public String set(String key, String value) {
        return new CommonClient<String>() {
            @Override
            public String singleClient() {
                return jedis.set(key, value);
            }

            @Override
            public String clusterClient() {
                return jedisCluster.set(key, value);
            }
        }.clientExecute();
    }

    public Set<byte[]> smembers(byte[] key) {
        return new CommonClient<Set<byte[]>>() {
            @Override
            public Set<byte[]> singleClient() {
                return jedis.smembers(key);
            }

            @Override
            public Set<byte[]> clusterClient() {
                return jedisCluster.smembers(key);
            }
        }.clientExecute();
    }

    public byte[] spop(byte[] key) {
        return new CommonClient<byte[]>() {
            @Override
            public byte[] singleClient() {
                return jedis.spop(key);
            }

            @Override
            public byte[] clusterClient() {
                return jedisCluster.spop(key);
            }
        }.clientExecute();
    }

    public Long scard(byte[] key) {
        return new CommonClient<Long>() {
            @Override
            public Long singleClient() {
                return jedis.scard(key);
            }

            @Override
            public Long clusterClient() {
                return jedisCluster.scard(key);
            }
        }.clientExecute();
    }

    public Set<byte[]> spop(byte[] key, long count) {
        return new CommonClient<Set<byte[]>>() {
            @Override
            public Set<byte[]> singleClient() {
                return jedis.spop(key, count);
            }

            @Override
            public Set<byte[]> clusterClient() {
                return jedisCluster.spop(key, count);
            }
        }.clientExecute();
    }

    public Long zunionstore(byte[] dstkey, byte[]... sets) {
        return new CommonClient<Long>() {
            @Override
            public Long singleClient() {
                return jedis.zunionstore(dstkey, sets);
            }

            @Override
            public Long clusterClient() {
                return jedisCluster.zunionstore(dstkey, sets);
            }
        }.clientExecute();
    }

    public Long sunionstore(byte[] dstkey, byte[]... keys) {
        return new CommonClient<Long>() {
            @Override
            public Long singleClient() {
                return jedis.sunionstore(dstkey, keys);
            }

            @Override
            public Long clusterClient() {
                return jedisCluster.sunionstore(dstkey, keys);
            }
        }.clientExecute();
    }

    public Long zcard(byte[] key) {
        return new CommonClient<Long>() {
            @Override
            public Long singleClient() {
                return jedis.zcard(key);
            }

            @Override
            public Long clusterClient() {
                return jedisCluster.zcard(key);
            }
        }.clientExecute();
    }

    public Long zcount(byte[] key, double min, double max) {
        return new CommonClient<Long>() {
            @Override
            public Long singleClient() {
                return jedis.zcount(key, min, max);
            }

            @Override
            public Long clusterClient() {
                return jedisCluster.zcount(key, min, max);
            }
        }.clientExecute();
    }

    public Double zincrby(byte[] key, double increment, byte[] member) {
        return new CommonClient<Double>() {
            @Override
            public Double singleClient() {
                return jedis.zincrby(key, increment, member);
            }

            @Override
            public Double clusterClient() {
                return jedisCluster.zincrby(key, increment, member);
            }
        }.clientExecute();
    }

    public String setex(byte[] key, int seconds, byte[] value) {
        return new CommonClient<String>() {
            @Override
            public String singleClient() {
                return jedis.setex(key, seconds, value);
            }

            @Override
            public String clusterClient() {
                return jedisCluster.setex(key, seconds, value);
            }
        }.clientExecute();
    }

    public Long setnx(byte[] key, byte[] value) {
        return new CommonClient<Long>() {
            @Override
            public Long singleClient() {
                return jedis.setnx(key, value);
            }

            @Override
            public Long clusterClient() {
                return jedisCluster.setnx(key, value);
            }
        }.clientExecute();
    }

    public Long llen(byte[] key) {
        return new CommonClient<Long>() {
            @Override
            public Long singleClient() {
                return jedis.llen(key);
            }

            @Override
            public Long clusterClient() {
                return jedisCluster.llen(key);
            }
        }.clientExecute();
    }

    public Long hset(byte[] key, byte[] field, byte[] value) {
        return new CommonClient<Long>() {
            @Override
            public Long singleClient() {
                return jedis.hset(key, field, value);
            }

            @Override
            public Long clusterClient() {
                return jedisCluster.hset(key, field, value);
            }
        }.clientExecute();
    }

    public String hmset(byte[] key, Map<byte[], byte[]> hash) {
        return new CommonClient<String>() {
            @Override
            public String singleClient() {
                return jedis.hmset(key, hash);
            }

            @Override
            public String clusterClient() {
                return jedisCluster.hmset(key, hash);
            }
        }.clientExecute();
    }

    public byte[] hget(byte[] key, byte[] field) {
        return new CommonClient<byte[]>() {
            @Override
            public byte[] singleClient() {
                return jedis.hget(key, field);
            }

            @Override
            public byte[] clusterClient() {
                return jedisCluster.hget(key, field);
            }
        }.clientExecute();
    }

    public List<byte[]> hmget(byte[] key, byte[]... fields) {
        return new CommonClient<List<byte[]>>() {
            @Override
            public List<byte[]> singleClient() {
                return jedis.hmget(key, fields);
            }

            @Override
            public List<byte[]> clusterClient() {
                return jedisCluster.hmget(key, fields);
            }
        }.clientExecute();
    }

    public Map<byte[], byte[]> hgetAll(byte[] key) {
        return new CommonClient<Map<byte[], byte[]>>() {
            @Override
            public Map<byte[], byte[]> singleClient() {
                return jedis.hgetAll(key);
            }

            @Override
            public Map<byte[], byte[]> clusterClient() {
                return jedisCluster.hgetAll(key);
            }
        }.clientExecute();
    }

    public Long incr(byte[] key) {
        return new CommonClient<Long>() {
            @Override
            public Long singleClient() {
                return jedis.incr(key);
            }

            @Override
            public Long clusterClient() {
                return jedisCluster.incr(key);
            }
        }.clientExecute();
    }

    public Long incrBy(byte[] key, long increment) {
        return new CommonClient<Long>() {
            @Override
            public Long singleClient() {
                return jedis.incrBy(key, increment);
            }

            @Override
            public Long clusterClient() {
                return jedisCluster.incrBy(key, increment);
            }
        }.clientExecute();
    }

    public Long hdel(byte[] key, byte[]... fields) {
        return new CommonClient<Long>() {
            @Override
            public Long singleClient() {
                return jedis.hdel(key, fields);
            }

            @Override
            public Long clusterClient() {
                return jedisCluster.hdel(key, fields);
            }
        }.clientExecute();
    }

    public Boolean exists(byte[] key) {
        return new CommonClient<Boolean>() {
            @Override
            public Boolean singleClient() {
                return jedis.exists(key);
            }

            @Override
            public Boolean clusterClient() {
                return jedisCluster.exists(key);
            }
        }.clientExecute();
    }

    public Boolean exists(String key) {
        return new CommonClient<Boolean>() {
            @Override
            public Boolean singleClient() {
                return jedis.exists(key);
            }

            @Override
            public Boolean clusterClient() {
                return jedisCluster.exists(key);
            }
        }.clientExecute();
    }

    public Long del(byte[] key) {
        return new CommonClient<Long>() {
            @Override
            public Long singleClient() {
                return jedis.del(key);
            }

            @Override
            public Long clusterClient() {
                return jedisCluster.del(key);
            }
        }.clientExecute();
    }

    public Long expire(byte[] key, int seconds) {
        return new CommonClient<Long>() {
            @Override
            public Long singleClient() {
                return jedis.expire(key, seconds);
            }

            @Override
            public Long clusterClient() {
                return jedisCluster.expire(key, seconds);
            }
        }.clientExecute();
    }

    public Long ttl(byte[] key) {
        return new CommonClient<Long>() {
            @Override
            public Long singleClient() {
                return jedis.ttl(key);
            }

            @Override
            public Long clusterClient() {
                return jedisCluster.ttl(key);
            }
        }.clientExecute();
    }

    public Long rpush(byte[] key, byte[]... strings) {
        return new CommonClient<Long>() {
            @Override
            public Long singleClient() {
                return jedis.rpush(key, strings);
            }

            @Override
            public Long clusterClient() {
                return jedisCluster.rpush(key, strings);
            }
        }.clientExecute();
    }

    public Long lpush(byte[] key, byte[]... strings) {
        return new CommonClient<Long>() {
            @Override
            public Long singleClient() {
                return jedis.lpush(key, strings);
            }

            @Override
            public Long clusterClient() {
                return jedisCluster.lpush(key, strings);
            }
        }.clientExecute();
    }

    public Long lpush(String key, String... strings) {
        return new CommonClient<Long>() {
            @Override
            public Long singleClient() {
                return jedis.lpush(key, strings);
            }

            @Override
            public Long clusterClient() {
                return jedisCluster.lpush(key, strings);
            }
        }.clientExecute();
    }

    public Long lrem(byte[] key, long count, byte[] value) {
        return new CommonClient<Long>() {
            @Override
            public Long singleClient() {
                return jedis.lrem(key, count, value);
            }

            @Override
            public Long clusterClient() {
                return jedisCluster.lrem(key, count, value);
            }
        }.clientExecute();
    }

    public String ltrim(byte[] key, long start, long stop) {
        return new CommonClient<String>() {
            @Override
            public String singleClient() {
                return jedis.ltrim(key, start, stop);
            }

            @Override
            public String clusterClient() {
                return jedisCluster.ltrim(key, start, stop);
            }
        }.clientExecute();
    }

    public Long sadd(byte[] key, byte[]... members) {
        return new CommonClient<Long>() {
            @Override
            public Long singleClient() {
                return jedis.sadd(key, members);
            }

            @Override
            public Long clusterClient() {
                return jedisCluster.sadd(key, members);
            }
        }.clientExecute();
    }

    public Long srem(byte[] key, byte[]... member) {
        return new CommonClient<Long>() {
            @Override
            public Long singleClient() {
                return jedis.srem(key, member);
            }

            @Override
            public Long clusterClient() {
                return jedisCluster.srem(key, member);
            }
        }.clientExecute();
    }

    public byte[] srandmember(byte[] key) {
        return new CommonClient<byte[]>() {
            @Override
            public byte[] singleClient() {
                return jedis.srandmember(key);
            }

            @Override
            public byte[] clusterClient() {
                return jedisCluster.srandmember(key);
            }
        }.clientExecute();
    }

    public Boolean sismember(byte[] key, byte[] member) {
        return new CommonClient<Boolean>() {
            @Override
            public Boolean singleClient() {
                return jedis.sismember(key, member);
            }

            @Override
            public Boolean clusterClient() {
                return jedisCluster.sismember(key, member);
            }
        }.clientExecute();
    }

    public Double zscore(byte[] key, byte[] member) {
        return new CommonClient<Double>() {
            @Override
            public Double singleClient() {
                return jedis.zscore(key, member);
            }

            @Override
            public Double clusterClient() {
                return jedisCluster.zscore(key, member);
            }
        }.clientExecute();
    }

    public byte[] lpop(byte[] key) {
        return new CommonClient<byte[]>() {
            @Override
            public byte[] singleClient() {
                return jedis.lpop(key);
            }

            @Override
            public byte[] clusterClient() {
                return jedisCluster.lpop(key);
            }
        }.clientExecute();
    }

    public byte[] rpop(byte[] key) {
        return new CommonClient<byte[]>() {
            @Override
            public byte[] singleClient() {
                return jedis.rpop(key);
            }

            @Override
            public byte[] clusterClient() {
                return jedisCluster.rpop(key);
            }
        }.clientExecute();
    }

    public List<byte[]> lrange(byte[] key, long start, long stop) {
        return new CommonClient<List<byte[]>>() {
            @Override
            public List<byte[]> singleClient() {
                return jedis.lrange(key, start, stop);
            }

            @Override
            public List<byte[]> clusterClient() {
                return jedisCluster.lrange(key, start, stop);
            }
        }.clientExecute();
    }

    public List<String> lrange(String key, long start, long stop) {
        return new CommonClient<List<String>>() {
            @Override
            public List<String> singleClient() {
                return jedis.lrange(key, start, stop);
            }

            @Override
            public List<String> clusterClient() {
                return jedisCluster.lrange(key, start, stop);
            }
        }.clientExecute();
    }

    public byte[] lindex(byte[] key, long index) {
        return new CommonClient<byte[]>() {
            @Override
            public byte[] singleClient() {
                return jedis.lindex(key, index);
            }

            @Override
            public byte[] clusterClient() {
                return jedisCluster.lindex(key, index);
            }
        }.clientExecute();
    }

    public Set<byte[]> zrange(byte[] key, long start, long stop) {
        return new CommonClient<Set<byte[]>>() {
            @Override
            public Set<byte[]> singleClient() {
                return jedis.zrange(key, start, stop);
            }

            @Override
            public Set<byte[]> clusterClient() {
                return jedisCluster.zrange(key, start, stop);
            }
        }.clientExecute();
    }

    public Long zrank(byte[] key, byte[] member) {
        return new CommonClient<Long>() {
            @Override
            public Long singleClient() {
                return jedis.zrank(key, member);
            }

            @Override
            public Long clusterClient() {
                return jedisCluster.zrank(key, member);
            }
        }.clientExecute();
    }

    public Long zrem(byte[] key, byte[]... members) {
        return new CommonClient<Long>() {
            @Override
            public Long singleClient() {
                return jedis.zrem(key, members);
            }

            @Override
            public Long clusterClient() {
                return jedisCluster.zrem(key, members);
            }
        }.clientExecute();
    }

    public void subscribe(JedisPubSub jedisPubSub, String... channels) {
        new CommonClient() {
            @Override
            public Object singleClient() {
                jedis.subscribe(jedisPubSub, channels);
                return null;
            }

            @Override
            public Object clusterClient() {
                jedisCluster.subscribe(jedisPubSub, channels);
                return null;
            }
        }.clientExecute();
    }

    public Long publish(String channel, String message) {
        return new CommonClient<Long>() {
            @Override
            public Long singleClient() {
                return jedis.publish(channel, message);
            }

            @Override
            public Long clusterClient() {
                return jedisCluster.publish(channel, message);
            }
        }.clientExecute();
    }

    public void close() {
        new CommonClient() {
            @Override
            public Object singleClient() {
                jedis.close();
                return null;
            }

            @Override
            public Object clusterClient() {
                return null;
            }
        }.clientExecute();
    }

    abstract class CommonClient<T> {
        public T clientExecute() {
            if (jedis != null) {
                return singleClient();
            } else if (jedisCluster != null) {
                return clusterClient();
            } else {
                throw new BusinessException("参数有误，请确认");
            }
        }

        abstract public T singleClient();

        abstract public T clusterClient();
    }
}
