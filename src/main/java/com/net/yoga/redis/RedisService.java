package com.net.yoga.redis;

import com.net.yoga.common.constant.CommonConstant;
import com.net.yoga.redis.base.BaseClusterRedis;
import com.net.yoga.redis.base.BaseSingleRedis;
import com.net.yoga.util.SerializeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import redis.clients.jedis.JedisPubSub;

import java.io.IOException;
import java.util.*;

//redis基础操作类
@Service("commonRedisService")
@Slf4j
public class RedisService {
    @Value("${redis.mode}")
    private String mode;
    @Autowired
    private BaseSingleRedis baseSingleRedis;
    @Autowired
    private BaseClusterRedis baseClusterRedis;

    public Long kryoZRemRangeByRank(String key, int start, int end) {
        Long commandResult = new RedisCommand<Long>() {
            @Override
            public Long execute(RedisClient redisClient) throws IOException {
                byte[] keyBytes = key.getBytes(CommonConstant.REDIS_DEFAULT_CHARSET);
                Long result = redisClient.zremrangeByRank(keyBytes, start, end);
                return result == null ? 0L : result;
            }

            @Override
            public Boolean checkCommandParam() {
                return null == key;
            }
        }.command();
        if (commandResult == null) {
            commandResult = 0L;
        }
        return commandResult;
    }

    public Boolean kryoZAddSet(String key, Long timeLine, Object value) {
        Boolean commandResult = new RedisCommand<Boolean>() {
            @Override
            public Boolean execute(RedisClient redisClient) throws IOException {
                byte[] keyBytes = key.getBytes(CommonConstant.REDIS_DEFAULT_CHARSET);
                byte[] valueBytes = SerializeUtil.KryoSerialize(value);
                Long numMembers = redisClient.zadd(keyBytes, timeLine, valueBytes);
                return numMembers.equals(CommonConstant.REDIS_DEFAULT_RESULT_LONG);
            }

            @Override
            public Boolean checkCommandParam() {
                return null == key;
            }
        }.command();
        if (commandResult == null) {
            return Boolean.FALSE;
        }
        return commandResult;
    }

    public Boolean kryoZAddSet(String key, Map<Object, Long> scoreMembers) {
        Boolean commandResult = new RedisCommand<Boolean>() {
            @Override
            public Boolean execute(RedisClient redisClient) throws IOException {
                byte[] keyBytes = key.getBytes(CommonConstant.REDIS_DEFAULT_CHARSET);
                Map<byte[], Double> scoreMemberMap = new HashMap<>();
                for (Map.Entry<Object, Long> entry : scoreMembers.entrySet()) {
                    byte[] valueBytes = SerializeUtil.KryoSerialize(entry.getKey());
                    scoreMemberMap.put(valueBytes, new Double(entry.getValue()));
                }
                Long numMembers = redisClient.zadd(keyBytes, scoreMemberMap);
                return numMembers.equals(CommonConstant.REDIS_DEFAULT_RESULT_LONG);
            }

            @Override
            public Boolean checkCommandParam() {
                return null == key;
            }
        }.command();
        if (commandResult == null) {
            return Boolean.FALSE;
        }
        return commandResult;
    }

    public Boolean kryoZAddSetStr(String key, Map<String, Double> scoreMembers) {
        Boolean commandResult = new RedisCommand<Boolean>() {
            @Override
            public Boolean execute(RedisClient redisClient) throws IOException {
                Long numMembers = redisClient.zadd(key, scoreMembers);
                return numMembers.equals(CommonConstant.REDIS_DEFAULT_RESULT_LONG);
            }

            @Override
            public Boolean checkCommandParam() {
                return null == key;
            }
        }.command();
        if (commandResult == null) {
            return Boolean.FALSE;
        }
        return commandResult;
    }

    public <T> List<T> kryoZRangeByScoreGet(String key, Long min, Long max, Class type) {
        List<T> commandResult = new RedisCommand<List<T>>() {
            @Override
            public List<T> execute(RedisClient redisClient) throws IOException {
                List<T> resultList = new ArrayList<>();
                byte[] keyBytes = key.getBytes(CommonConstant.REDIS_DEFAULT_CHARSET);
                Set docs = redisClient.zrangeByScore(keyBytes, min, max);
                for (Object valueBytes : docs) {
                    resultList.add((T) SerializeUtil.KryoDeserialize((byte[]) valueBytes, type));
                }
                return resultList;
            }

            @Override
            public Boolean checkCommandParam() {
                return null == key;
            }
        }.command();
        return commandResult;
    }

    public <T> List<T> kryoZRangeByScoreGet(String key, Long min, Long max, int offset, int limit, Class type) {
        List<T> commandResult = new RedisCommand<List<T>>() {
            @Override
            public List<T> execute(RedisClient redisClient) throws IOException {

                List<T> resultList = new ArrayList<>();
                byte[] keyBytes = key.getBytes(CommonConstant.REDIS_DEFAULT_CHARSET);
                Set docs = redisClient.zrangeByScore(keyBytes, min, max, offset, limit);
                for (Object valueBytes : docs) {
                    resultList.add((T) SerializeUtil.KryoDeserialize((byte[]) valueBytes, type));
                }
                return resultList;
            }

            @Override
            public Boolean checkCommandParam() {
                return null == key;
            }
        }.command();
        return commandResult;
    }

    public <T> List<T> kryoZRevRangeByScoreGet(String key, Long min, Long max, Class type) {
        List<T> commandResult = new RedisCommand<List<T>>() {
            @Override
            public List<T> execute(RedisClient redisClient) throws IOException {
                List<T> resultList = new ArrayList<>();
                byte[] keyBytes = key.getBytes(CommonConstant.REDIS_DEFAULT_CHARSET);
                Set docs = redisClient.zrevrangeByScore(keyBytes, min, max);
                for (Object valueBytes : docs) {
                    resultList.add((T) SerializeUtil.KryoDeserialize((byte[]) valueBytes, type));
                }
                return resultList;
            }

            @Override
            public Boolean checkCommandParam() {
                return null == key;
            }
        }.command();
        return commandResult;
    }

    public <T> List<T> kryoZRevRangeByScoreGet(String key, Long max, Long min, int offset, int count, Class type) {
        List<T> commandResult = new RedisCommand<List<T>>() {
            @Override
            public List<T> execute(RedisClient redisClient) throws IOException {
                List<T> resultList = new ArrayList<>();
                byte[] keyBytes = key.getBytes(CommonConstant.REDIS_DEFAULT_CHARSET);
                Set docs = redisClient.zrevrangeByScore(keyBytes, max, min, offset, count);
                for (Object valueBytes : docs) {
                    resultList.add((T) SerializeUtil.KryoDeserialize((byte[]) valueBytes, type));
                }
                return resultList;
            }

            @Override
            public Boolean checkCommandParam() {
                return null == key;
            }
        }.command();
        return commandResult;
    }

    public <T> List<T> kryoZRevRange(String key, long start, long end, Class type) {
        List<T> commandResult = new RedisCommand<List<T>>() {
            @Override
            List<T> execute(RedisClient redisClient) throws IOException {
                List<T> resultList = new ArrayList<>();
                byte[] keyBytes = key.getBytes(CommonConstant.REDIS_DEFAULT_CHARSET);
                Set docs = redisClient.zrevrange(keyBytes, start, end);
                for (Object valueBytes : docs) {
                    resultList.add((T) SerializeUtil.KryoDeserialize((byte[]) valueBytes, type));
                }
                return resultList;
            }

            @Override
            Boolean checkCommandParam() {
                return null == key;
            }
        }.command();
        return commandResult;
    }

    public <T> T kryoGet(String key, Class<T> type) {
        T commandResult = new RedisCommand<T>() {
            @Override
            public T execute(RedisClient redisClient) throws IOException {
                byte[] keyBytes = key.getBytes(CommonConstant.REDIS_DEFAULT_CHARSET);
                byte[] valueBytes = redisClient.get(keyBytes);
                return (T) SerializeUtil.KryoDeserialize(valueBytes, type);
            }

            @Override
            public Boolean checkCommandParam() {
                return null == key;
            }
        }.command();
        return commandResult;
    }

    public Boolean kryoSet(String key, Object value) {
        Boolean commandResult = new RedisCommand<Boolean>() {
            @Override
            public Boolean execute(RedisClient redisClient) throws IOException {
                byte[] keyBytes = key.getBytes(CommonConstant.REDIS_DEFAULT_CHARSET);
                byte[] valueBytes = SerializeUtil.KryoSerialize(value);
                Boolean result = CommonConstant.REDIS_DEFAULT_OK.equalsIgnoreCase(
                        redisClient.set(keyBytes, valueBytes));
                return result;
            }

            @Override
            public Boolean checkCommandParam() {
                return null == key;
            }
        }.command();
        if (commandResult == null) {
            return Boolean.FALSE;
        }
        return commandResult;
    }

    public <T> Set<T> kryoSMembers(String key, Class type) {
        Set<T> commandResult = new RedisCommand<Set<T>>() {
            @Override
            public Set<T> execute(RedisClient redisClient) throws IOException {
                Set<T> results = new HashSet<>();
                byte[] keyBytes = key.getBytes(CommonConstant.REDIS_DEFAULT_CHARSET);
                Set docs = redisClient.smembers(keyBytes);
                for (Object valueBytes : docs) {
                    results.add((T) SerializeUtil.KryoDeserialize((byte[]) valueBytes, type));
                }
                return results;
            }

            @Override
            public Boolean checkCommandParam() {
                return null == key;
            }
        }.command();
        return commandResult;
    }

    public <T> T kryoSPop(String key, Class type) {
        T commandResult = new RedisCommand<T>() {
            @Override
            public T execute(RedisClient redisClient) throws IOException {
                byte[] keyBytes = key.getBytes(CommonConstant.REDIS_DEFAULT_CHARSET);
                byte[] valueBytes = redisClient.spop(keyBytes);
                return (T) SerializeUtil.KryoDeserialize(valueBytes, type);
            }

            @Override
            public Boolean checkCommandParam() {
                return null == key;
            }
        }.command();
        return commandResult;
    }

    public Long kryoSCard(String key) {
        Long commandResult = new RedisCommand<Long>() {
            @Override
            public Long execute(RedisClient redisClient) throws IOException {
                return redisClient.scard(key.getBytes(CommonConstant.REDIS_DEFAULT_CHARSET));
            }

            @Override
            public Boolean checkCommandParam() {
                return null == key;
            }
        }.command();
        return commandResult;
    }

    public Set<byte[]> kryoSPop(String key, long count) {
        Set<byte[]> commandResult = new RedisCommand<Set<byte[]>>() {
            @Override
            public Set<byte[]> execute(RedisClient redisClient) throws IOException {
                Set<byte[]> valueBytes = redisClient.spop(key.getBytes(CommonConstant.REDIS_DEFAULT_CHARSET), count);
                return valueBytes;
            }

            @Override
            public Boolean checkCommandParam() {
                return null == key;
            }
        }.command();
        return commandResult;
    }

    public Long kryoSUnionStore(String key, String keyF, String KeyS) {
        Long commandResult = new RedisCommand<Long>() {
            @Override
            public Long execute(RedisClient redisClient) throws IOException {
                byte[] keyBytes = key.getBytes(CommonConstant.REDIS_DEFAULT_CHARSET);
                byte[] keyFBytes = keyF.getBytes(CommonConstant.REDIS_DEFAULT_CHARSET);
                byte[] keySBytes = KeyS.getBytes(CommonConstant.REDIS_DEFAULT_CHARSET);
                Long result = redisClient.zunionstore(keyBytes, keyFBytes, keySBytes);
                return result == null ? 0L : result;
            }

            @Override
            public Boolean checkCommandParam() {
                return null == key || null == keyF || null == KeyS;
            }
        }.command();
        if (commandResult == null) {
            commandResult = 0L;
        }
        return commandResult;
    }

    public Long kryoZUnionStore(String key, String keyF, String KeyS) {
        Long commandResult = new RedisCommand<Long>() {
            @Override
            public Long execute(RedisClient redisClient) throws IOException {
                byte[] keyBytes = key.getBytes(CommonConstant.REDIS_DEFAULT_CHARSET);
                byte[] keyFBytes = keyF.getBytes(CommonConstant.REDIS_DEFAULT_CHARSET);
                byte[] keySBytes = KeyS.getBytes(CommonConstant.REDIS_DEFAULT_CHARSET);
                Long result = redisClient.sunionstore(keyBytes, keyFBytes, keySBytes);
                return result == null ? 0L : result;
            }

            @Override
            public Boolean checkCommandParam() {
                return null == key || null == keyF || null == KeyS;
            }
        }.command();
        if (commandResult == null) {
            commandResult = 0L;
        }
        return commandResult;
    }

    public Long kryoZCard(String key) {
        Long commandResult = new RedisCommand<Long>() {
            @Override
            public Long execute(RedisClient redisClient) throws IOException {
                byte[] keyBytes = key.getBytes(CommonConstant.REDIS_DEFAULT_CHARSET);
                Long result = redisClient.zcard(keyBytes);
                return result == null ? 0L : result;
            }

            @Override
            public Boolean checkCommandParam() {
                return null == key;
            }
        }.command();
        if (commandResult == null) {
            commandResult = 0L;
        }
        return commandResult;
    }

    public Long kryoZCount(String key, Long min, Long max) {
        Long commandResult = new RedisCommand<Long>() {
            @Override
            public Long execute(RedisClient redisClient) throws IOException {
                byte[] keyBytes = key.getBytes(CommonConstant.REDIS_DEFAULT_CHARSET);

                Long result = redisClient.zcount(keyBytes, min.doubleValue(), max.doubleValue());
                return result == null ? 0L : result;
            }

            @Override
            public Boolean checkCommandParam() {
                return null == key;
            }
        }.command();
        if (commandResult == null) {
            commandResult = 0L;
        }
        return commandResult;
    }

    public Long kryoZIncrby(String key, Long score, Object value) {
        Long commandResult = new RedisCommand<Long>() {
            @Override
            public Long execute(RedisClient redisClient) throws IOException {
                byte[] keyBytes = key.getBytes(CommonConstant.REDIS_DEFAULT_CHARSET);
                byte[] valueBytes = SerializeUtil.KryoSerialize(value);
                Long result = redisClient.zincrby(keyBytes, score.doubleValue(), valueBytes).longValue();
                return result == null ? 0L : result;
            }

            @Override
            public Boolean checkCommandParam() {
                return null == key;
            }
        }.command();
        if (commandResult == null) {
            commandResult = 0L;
        }
        return commandResult;
    }

    public Boolean kryoSetEx(String key, int seconds, Object value) {
        Boolean commandResult = new RedisCommand<Boolean>() {
            @Override
            public Boolean execute(RedisClient redisClient) throws IOException {
                byte[] keyBytes = key.getBytes(CommonConstant.REDIS_DEFAULT_CHARSET);
                byte[] valueBytes = SerializeUtil.KryoSerialize(value);
                Boolean result = CommonConstant.REDIS_DEFAULT_OK.equalsIgnoreCase(redisClient.setex(keyBytes, seconds,
                        valueBytes));
                return result;
            }

            @Override
            public Boolean checkCommandParam() {
                return null == key;
            }
        }.command();
        if (commandResult == null) {
            return Boolean.FALSE;
        }
        return commandResult;
    }

    public Long kryoSetNx(String key, Object value) {
        Long commandResult = new RedisCommand<Long>() {
            @Override
            public Long execute(RedisClient redisClient) throws IOException {
                byte[] keyBytes = key.getBytes(CommonConstant.REDIS_DEFAULT_CHARSET);
                byte[] valueBytes = SerializeUtil.KryoSerialize(value);
                Long result = redisClient.setnx(keyBytes, valueBytes);
                return result == null ? 0L : result;
            }

            @Override
            public Boolean checkCommandParam() {
                return null == key;
            }
        }.command();
        if (commandResult == null) {
            commandResult = 0L;
        }
        return commandResult;
    }

    public Long llen(String key) {
        Long commandResult = new RedisCommand<Long>() {
            @Override
            Long execute(RedisClient redisClient) throws IOException {
                byte[] keyBytes = key.getBytes(CommonConstant.REDIS_DEFAULT_CHARSET);
                return redisClient.llen(keyBytes);
            }

            @Override
            Boolean checkCommandParam() {
                return null == key;
            }
        }.command();
        return commandResult;
    }

    public <T, S> Long kryoHset(String key, T field, S value) {
        Long commandResult = new RedisCommand<Long>() {
            @Override
            public Long execute(RedisClient redisClient) throws IOException {
                byte[] keyBytes = key.getBytes(CommonConstant.REDIS_DEFAULT_CHARSET);
                byte[] fieldBytes = SerializeUtil.KryoSerialize(field);
                byte[] valueBytes = SerializeUtil.KryoSerialize(value);
                Long result = redisClient.hset(keyBytes, fieldBytes, valueBytes);
                return result == null ? 0L : result;
            }

            @Override
            public Boolean checkCommandParam() {
                return null == key || null == value;
            }
        }.command();
        if (commandResult == null) {
            commandResult = 0L;
        }
        return commandResult;
    }

    public <T, S> Boolean kryoHmset(String key, Map<T, S> fieldValueMap) {
        Boolean commandResult = new RedisCommand<Boolean>() {
            @Override
            public Boolean execute(RedisClient redisClient) throws IOException {
                byte[] keyBytes = key.getBytes(CommonConstant.REDIS_DEFAULT_CHARSET);
                Map<byte[], byte[]> valueMap = new HashMap<byte[], byte[]>();
                for (Map.Entry<T, S> entry : fieldValueMap.entrySet()) {
                    T entryKey = entry.getKey();
                    S entryOb = entry.getValue();
                    byte[] fieldBytes = SerializeUtil.KryoSerialize(entryKey);
                    byte[] valueBytes = SerializeUtil.KryoSerialize(entryOb);
                    valueMap.put(fieldBytes, valueBytes);
                }
                Boolean result = CommonConstant.REDIS_DEFAULT_OK.equalsIgnoreCase(
                        redisClient.hmset(keyBytes, valueMap));
                return result;
            }

            @Override
            public Boolean checkCommandParam() {
                return null == key || null == fieldValueMap;
            }
        }.command();
        if (commandResult == null) {
            return Boolean.FALSE;
        }
        return commandResult;
    }

    public <T, S> T kryoHget(String key, S field, Class<T> type) {
        T commandResult = new RedisCommand<T>() {
            @Override
            public T execute(RedisClient redisClient) throws IOException {
                byte[] keyBytes = key.getBytes(CommonConstant.REDIS_DEFAULT_CHARSET);
                byte[] fieldBytes = SerializeUtil.KryoSerialize(field);
                byte[] valueBytes = redisClient.hget(keyBytes, fieldBytes);
                return (T) SerializeUtil.KryoDeserialize(valueBytes, type);
            }

            @Override
            public Boolean checkCommandParam() {
                return null == key;
            }
        }.command();
        return commandResult;
    }

    public <T, S> List<T> kryoHmget(String key, List<S> fields, Class<T> t) {
        List<T> commandResult = new RedisCommand<List<T>>() {
            @Override
            public List<T> execute(RedisClient redisClient) throws IOException {
                byte[][] fieldsArray = new byte[fields.size()][];
                byte[] keyByte = key.getBytes(CommonConstant.REDIS_DEFAULT_CHARSET);
                for (int i = 0; i < fields.size(); i++) {
                    fieldsArray[i] = SerializeUtil.KryoSerialize(fields.get(i));
                }
                List<T> resultList = new ArrayList<>();

                List<byte[]> byteResults = redisClient.hmget(keyByte, fieldsArray);
                for (byte[] result : byteResults) {
                    resultList.add(SerializeUtil.KryoDeserialize(result, t));
                }
                return resultList;
            }

            @Override
            public Boolean checkCommandParam() {
                return null == key;
            }
        }.command();
        return commandResult;
    }

    public <S, T> Map<S, T> kryoHgetAll(String key, Class<S> s, Class<T> t) {
        Map<S, T> commandResult = new RedisCommand<Map<S, T>>() {
            @Override
            public Map<S, T> execute(RedisClient redisClient) throws IOException {
                byte[] keyBytes = key.getBytes(CommonConstant.REDIS_DEFAULT_CHARSET);
                Map<byte[], byte[]> fieldValueBytes = redisClient.hgetAll(keyBytes);
                if (fieldValueBytes.size() == 0 || fieldValueBytes == null) {
                    return null;
                }
                Map<S, T> resultMap = new HashMap<>();
                for (Map.Entry<byte[], byte[]> entry : fieldValueBytes.entrySet()) {
                    byte[] entryKey = entry.getKey();
                    byte[] entryValue = entry.getValue();
                    resultMap.put((S) SerializeUtil.KryoDeserialize(entryKey, s), (T) SerializeUtil
                            .KryoDeserialize(entryValue, t));
                }
                return resultMap;
            }

            @Override
            public Boolean checkCommandParam() {
                return null == key;
            }
        }.command();
        return commandResult;
    }

    public Long incr(String key) {
        Long commandResult = new RedisCommand<Long>() {
            @Override
            public Long execute(RedisClient redisClient) throws IOException {
                Long result = redisClient.incr(key.getBytes(CommonConstant.REDIS_DEFAULT_CHARSET));
                return result == null ? 0L : result;
            }

            @Override
            public Boolean checkCommandParam() {
                return null == key;
            }
        }.command();
        if (commandResult == null) {
            commandResult = 0L;
        }
        return commandResult;
    }

    public Long incrBy(String key, Long increment) {
        Long commandResult = new RedisCommand<Long>() {
            @Override
            public Long execute(RedisClient redisClient) throws IOException {
                byte[] keyBytes = key.getBytes(CommonConstant.REDIS_DEFAULT_CHARSET);
                Long result = redisClient.incrBy(keyBytes, increment);
                return result == null ? 0L : result;
            }

            @Override
            public Boolean checkCommandParam() {
                return null == key;
            }
        }.command();
        if (commandResult == null) {
            commandResult = 0L;
        }
        return commandResult;
    }

    public Long hdel(String key, String field) {
        Long commandResult = new RedisCommand<Long>() {
            @Override
            public Long execute(RedisClient redisClient) throws IOException {
                byte[] keyBytes = key.getBytes(CommonConstant.REDIS_DEFAULT_CHARSET);
                byte[] fieldBytes = field.getBytes(CommonConstant.REDIS_DEFAULT_CHARSET);
                Long result = 0L;
                if (redisClient.exists(keyBytes)) {
                    result = redisClient.hdel(keyBytes, fieldBytes);
                }
                return result == null ? 0L : result;
            }

            @Override
            public Boolean checkCommandParam() {
                return null == key;
            }
        }.command();
        if (commandResult == null) {
            commandResult = 0L;
        }
        return commandResult;
    }

    public Long kryoHdel(String key, String... fields) {
        Long commandResult = new RedisCommand<Long>() {
            @Override
            public Long execute(RedisClient redisClient) throws IOException {
                byte[] keyBytes = key.getBytes(CommonConstant.REDIS_DEFAULT_CHARSET);
                byte[][] fieldBytes = new byte[fields.length][];
                for (int i = 0; i < fields.length; i++) {
                    fieldBytes[i] = SerializeUtil.KryoSerialize(fields[i]);
                }
                Long result = redisClient.hdel(keyBytes, fieldBytes);
                return result == null ? 0L : result;
            }

            @Override
            public Boolean checkCommandParam() {
                return null == key;
            }
        }.command();
        if (commandResult == null) {
            commandResult = 0L;
        }
        return commandResult;
    }

    public Boolean isKeyByteExist(String key) {
        Boolean commandResult = new RedisCommand<Boolean>() {
            @Override
            public Boolean execute(RedisClient redisClient) throws IOException {
                byte[] keyBytes = key.getBytes(CommonConstant.REDIS_DEFAULT_CHARSET);
                return redisClient.exists(keyBytes);
            }

            @Override
            public Boolean checkCommandParam() {
                return null == key;
            }
        }.command();
        if (commandResult == null) {
            return Boolean.FALSE;
        }
        return commandResult;
    }

    public Boolean isKeyExist(String key) {
        Boolean commandResult = new RedisCommand<Boolean>() {
            @Override
            public Boolean execute(RedisClient redisClient) throws IOException {
                return redisClient.exists(key);
            }

            @Override
            public Boolean checkCommandParam() {
                return null == key;
            }
        }.command();
        if (commandResult == null) {
            return Boolean.FALSE;
        }
        return commandResult;
    }

    public Long del(String key) {
        Long commandResult = new RedisCommand<Long>() {
            @Override
            public Long execute(RedisClient redisClient) throws IOException {
                byte[] keyBytes = key.getBytes(CommonConstant.REDIS_DEFAULT_CHARSET);
                Long result = redisClient.del(keyBytes);
                return result == null ? 0L : result;
            }

            @Override
            public Boolean checkCommandParam() {
                return null == key;
            }
        }.command();
        if (commandResult == null) {
            commandResult = 0L;
        }
        return commandResult;
    }

    public Long expire(String key, int seconds) {
        Long commandResult = new RedisCommand<Long>() {
            @Override
            public Long execute(RedisClient redisClient) throws IOException {
                byte[] keyBytes = key.getBytes(CommonConstant.REDIS_DEFAULT_CHARSET);
                Long result = redisClient.expire(keyBytes, seconds);
                return result == null ? 0L : result;
            }

            @Override
            public Boolean checkCommandParam() {
                return null == key;
            }
        }.command();
        if (commandResult == null) {
            commandResult = 0L;
        }
        return commandResult;
    }

    public Long ttl(String key) {
        Long commandResult = new RedisCommand<Long>() {
            @Override
            public Long execute(RedisClient redisClient) throws IOException {
                byte[] keyBytes = key.getBytes(CommonConstant.REDIS_DEFAULT_CHARSET);
                Long result = redisClient.ttl(keyBytes);
                return result == null ? 0L : result;
            }

            @Override
            public Boolean checkCommandParam() {
                return null == key;
            }
        }.command();
        if (commandResult == null) {
            commandResult = 0L;
        }
        return commandResult;
    }

    //list相关操作
    public Long kryoRPush(String key, Object value) {
        Long commandResult = new RedisCommand<Long>() {
            @Override
            public Long execute(RedisClient redisClient) throws IOException {
                byte[] keyBytes = key.getBytes(CommonConstant.REDIS_DEFAULT_CHARSET);
                byte[] valueBytes = SerializeUtil.KryoSerialize(value);
                Long length = redisClient.rpush(keyBytes, valueBytes);//返回list长度
                return length;
            }

            @Override
            public Boolean checkCommandParam() {
                return null == key || value == null;
            }
        }.command();
        if (commandResult == null) {
            return 0L;
        }
        return commandResult;
    }

    public Long kryoLPush(String key, Object value) {
        Long commandResult = new RedisCommand<Long>() {
            @Override
            public Long execute(RedisClient redisClient) throws IOException {
                byte[] keyBytes = key.getBytes(CommonConstant.REDIS_DEFAULT_CHARSET);
                byte[] valueBytes = SerializeUtil.KryoSerialize(value);
                Long length = redisClient.lpush(keyBytes, valueBytes);//返回list长度
                return length;
            }

            @Override
            public Boolean checkCommandParam() {
                return null == key || value == null;
            }
        }.command();
        if (commandResult == null) {
            return 0L;
        }
        return commandResult;
    }

    public Long kryoLPushStr(String key, String[] value) {
        Long commandResult = new RedisCommand<Long>() {
            @Override
            public Long execute(RedisClient redisClient) throws IOException {
                Long length = redisClient.lpush(key, value);//返回list长度
                return length;
            }

            @Override
            public Boolean checkCommandParam() {
                return null == key || value == null;
            }
        }.command();
        if (commandResult == null) {
            return 0L;
        }
        return commandResult;
    }

    public Long kryoLrem(String key, long count, Object value) {
        Long commandResult = new RedisCommand<Long>() {
            @Override
            public Long execute(RedisClient redisClient) throws IOException {
                byte[] keyBytes = key.getBytes(CommonConstant.REDIS_DEFAULT_CHARSET);
                byte[] valueBytes = SerializeUtil.KryoSerialize(value);
                Long result = redisClient.lrem(keyBytes, count, valueBytes);
                return result == null ? 0L : result;
            }

            @Override
            public Boolean checkCommandParam() {
                return null == key;
            }
        }.command();
        if (commandResult == null) {
            commandResult = 0L;
        }
        return commandResult;
    }

    public Boolean kryoLtrim(String key, long start, long end) {
        Boolean commandResult = new RedisCommand<Boolean>() {
            @Override
            public Boolean execute(RedisClient redisClient) throws IOException {
                byte[] keyBytes = key.getBytes(CommonConstant.REDIS_DEFAULT_CHARSET);
                Boolean result = CommonConstant.REDIS_DEFAULT_OK.equalsIgnoreCase(redisClient.ltrim(keyBytes, start, end));
                return result;
            }

            @Override
            public Boolean checkCommandParam() {
                return null == key;
            }
        }.command();
        if (commandResult == null) {
            return Boolean.FALSE;
        }
        return commandResult;
    }

    public Boolean kryoSAddSet(String key, Object value) {
        Boolean commandResult = new RedisCommand<Boolean>() {
            @Override
            public Boolean execute(RedisClient redisClient) throws IOException {
                byte[] keyBytes = key.getBytes(CommonConstant.REDIS_DEFAULT_CHARSET);
                byte[] valueBytes = SerializeUtil.KryoSerialize(value);
                Long numMembers = redisClient.sadd(keyBytes, valueBytes);
                return numMembers.equals(CommonConstant.REDIS_DEFAULT_RESULT_LONG);
            }

            @Override
            public Boolean checkCommandParam() {
                return null == key;
            }
        }.command();
        if (commandResult == null) {
            return Boolean.FALSE;
        }
        return commandResult;
    }

    public Boolean kryoSAddSets(String key, Set<String> value) {
        Boolean commandResult = new RedisCommand<Boolean>() {
            @Override
            public Boolean execute(RedisClient redisClient) throws IOException {
                byte[] keyBytes = key.getBytes(CommonConstant.REDIS_DEFAULT_CHARSET);
                int i = 0;
                byte[][] valueByte3 = new byte[value.size()][1];
                for (String temp : value) {
                    valueByte3[i] = SerializeUtil.KryoSerialize(temp);
                    i++;
                }
                Long numMembers = redisClient.sadd(keyBytes, valueByte3);
                return numMembers.equals(Long.valueOf(value.size()));
            }

            @Override
            public Boolean checkCommandParam() {
                return null == key;
            }
        }.command();
        if (commandResult == null) {
            return Boolean.FALSE;
        }
        return commandResult;
    }

    public Long kryoSrem(String key, Object value) {
        Long commandResult = new RedisCommand<Long>() {
            @Override
            public Long execute(RedisClient redisClient) throws IOException {
                byte[] keyBytes = key.getBytes(CommonConstant.REDIS_DEFAULT_CHARSET);
                byte[] valueBytes = SerializeUtil.KryoSerialize(value);
                return redisClient.srem(keyBytes, valueBytes);
            }

            @Override
            public Boolean checkCommandParam() {
                return null == key;
            }
        }.command();
        return commandResult;
    }

    public <T> T kryoSrandMember(String key, Class<T> type) {
        T commandResult = new RedisCommand<T>() {
            @Override
            public T execute(RedisClient redisClient) throws IOException {
                byte[] keyBytes = key.getBytes(CommonConstant.REDIS_DEFAULT_CHARSET);
                byte[] valueBytes = redisClient.srandmember(keyBytes);
                return (T) SerializeUtil.KryoDeserialize(valueBytes, type);
            }

            @Override
            public Boolean checkCommandParam() {
                return null == key;
            }
        }.command();
        return commandResult;
    }

    public Boolean kryoSismemberSet(String key, Object value) {
        Boolean commandResult = new RedisCommand<Boolean>() {
            @Override
            public Boolean execute(RedisClient redisClient) throws IOException {
                byte[] keyBytes = key.getBytes(CommonConstant.REDIS_DEFAULT_CHARSET);
                byte[] valueBytes = SerializeUtil.KryoSerialize(value);
                return redisClient.sismember(keyBytes, valueBytes);
            }

            @Override
            public Boolean checkCommandParam() {
                return null == key;
            }
        }.command();
        if (commandResult == null) {
            return Boolean.FALSE;
        }
        return commandResult;
    }

    public Double kryoZScore(String key, Object value) {
        Double commandResult = new RedisCommand<Double>() {
            @Override
            Double execute(RedisClient redisClient) throws IOException {
                byte[] keyBytes = key.getBytes(CommonConstant.REDIS_DEFAULT_CHARSET);
                byte[] valueBytes = SerializeUtil.KryoSerialize(value);
                return redisClient.zscore(keyBytes, valueBytes);
            }

            @Override
            Boolean checkCommandParam() {
                return null == key;
            }
        }.command();
        if (commandResult == null) {
            commandResult = null;
        }
        return commandResult;
    }

    public <T> T kryoLPop(String key, Class<T> type) {
        T commandResult = new RedisCommand<T>() {
            @Override
            public T execute(RedisClient redisClient) throws IOException {
                byte[] valueBytes = redisClient.lpop(key.getBytes(CommonConstant.REDIS_DEFAULT_CHARSET));
                return (T) SerializeUtil.KryoDeserialize(valueBytes, type);
            }

            @Override
            public Boolean checkCommandParam() {
                return null == key;
            }
        }.command();
        return commandResult;
    }

    public <T> T kryoRPop(String key, Class<T> type) {
        T commandResult = new RedisCommand<T>() {
            @Override
            public T execute(RedisClient redisClient) throws IOException {
                byte[] valueBytes = redisClient.rpop(key.getBytes(CommonConstant.REDIS_DEFAULT_CHARSET));
                return (T) SerializeUtil.KryoDeserialize(valueBytes, type);
            }

            @Override
            public Boolean checkCommandParam() {
                return null == key;
            }
        }.command();
        return commandResult;
    }

    public <T> List<T> kryoLRange(String key, Long start, Long end, Class<T> type) {
        List<T> commandResult = new RedisCommand<List<T>>() {
            @Override
            public List<T> execute(RedisClient redisClient) throws IOException {
                byte[] keyBytes = key.getBytes(CommonConstant.REDIS_DEFAULT_CHARSET);
                List<byte[]> tempList = redisClient.lrange(keyBytes, start, end);
                List<T> resultList = new ArrayList<>();
                for (byte[] temp : tempList) {
                    resultList.add((T) SerializeUtil.KryoDeserialize(temp, type));
                }
                return resultList;
            }

            @Override
            public Boolean checkCommandParam() {
                return null == key || start == null || end == null;
            }
        }.command();
        return commandResult;
    }

    public List<String> lRange(String key, Long start, Long end) {
        List<String> commandResult = new RedisCommand<List<String>>() {
            @Override
            public List<String> execute(RedisClient redisClient) throws IOException {
                List<String> resultList = redisClient.lrange(key, start, end);
                return resultList;
            }

            @Override
            public Boolean checkCommandParam() {
                return null == key || start == null || end == null;
            }
        }.command();
        return commandResult;
    }

    public <T> T kryoLindex(String key, Integer index, Class<T> type) {
        T commandResult = new RedisCommand<T>() {
            @Override
            T execute(RedisClient redisClient) throws IOException {
                byte[] keyBytes = key.getBytes(CommonConstant.REDIS_DEFAULT_CHARSET);
                byte[] valueBytes = redisClient.lindex(keyBytes, index);
                return (T) SerializeUtil.KryoDeserialize(valueBytes, type);
            }

            @Override
            Boolean checkCommandParam() {
                return null == key;
            }
        }.command();

        return commandResult;
    }

    //SortedSet
    public <T> List<T> kryoZRange(String key, Long start, Long end, Class<T> type) {
        List<T> commandResult = new RedisCommand<List<T>>() {
            @Override
            public List<T> execute(RedisClient redisClient) throws IOException {
                byte[] keyBytes = key.getBytes(CommonConstant.REDIS_DEFAULT_CHARSET);
                Set<byte[]> set = redisClient.zrange(keyBytes, start, end);//LinkedHashSet
                List<T> resultList = new ArrayList<>();
                for (byte[] temp : set) {
                    resultList.add((T) SerializeUtil.KryoDeserialize(temp, type));
                }
                return resultList;
            }

            @Override
            public Boolean checkCommandParam() {
                return null == key || start == null || end == null;
            }
        }.command();
        return commandResult;
    }

    public Long kryoZRank(String key, Object awardNumber) {
        Long commandResult = new RedisCommand<Long>() {
            @Override
            public Long execute(RedisClient redisClient) throws IOException {
                byte[] keyBytes = key.getBytes(CommonConstant.REDIS_DEFAULT_CHARSET);
                byte[] valueBytes = SerializeUtil.KryoSerialize(awardNumber);
                Long rank = redisClient.zrank(keyBytes, valueBytes);
                return rank;
            }

            @Override
            public Boolean checkCommandParam() {
                return null == key || awardNumber == null;
            }
        }.command();
        return commandResult;
    }

    public Long kryoZRem(String key, Object val) {
        Long commandResult = new RedisCommand<Long>() {
            @Override
            public Long execute(RedisClient redisClient) throws IOException {
                byte[] keyBytes = key.getBytes(CommonConstant.REDIS_DEFAULT_CHARSET);
                byte[] valueBytes = SerializeUtil.KryoSerialize(val);
                Long value = redisClient.zrem(keyBytes, valueBytes);
                return value;
            }

            @Override
            public Boolean checkCommandParam() {
                return null == key || val == null;
            }
        }.command();
        return commandResult;
    }


    public String get(String key) {
        String commandResult = new RedisCommand<String>() {
            @Override
            public String execute(RedisClient redisClient) throws IOException {
                String value = redisClient.get(key);
                return value;
            }

            @Override
            public Boolean checkCommandParam() {
                return null == key;
            }
        }.command();
        return commandResult;
    }

    public Boolean set(String key, String value) {
        Boolean commandResult = new RedisCommand<Boolean>() {
            @Override
            public Boolean execute(RedisClient redisClient) throws IOException {
                Boolean result = CommonConstant.REDIS_DEFAULT_OK.equalsIgnoreCase(redisClient.set(key, value));
                return result;
            }

            @Override
            public Boolean checkCommandParam() {
                return null == key;
            }
        }.command();
        if (commandResult == null) {
            return Boolean.FALSE;
        }
        return commandResult;
    }

    public void close() {
        new RedisCommand() {
            @Override
            public Object execute(RedisClient redisClient) {
                redisClient.close();
                return null;
            }

            @Override
            public Boolean checkCommandParam() {
                return true;
            }
        }.command();
    }

    public void subscribe(JedisPubSub jedisPubSub, String... channels) {
        new RedisCommand() {
            @Override
            public Object execute(RedisClient redisClient) {
                redisClient.subscribe(jedisPubSub, channels);
                return null;
            }

            @Override
            public Boolean checkCommandParam() {
                return null == jedisPubSub || channels == null;
            }
        }.command();
    }

    public Long publish(String channel, String message) {
        Long commandResult = new RedisCommand<Long>() {
            @Override
            public Long execute(RedisClient redisClient) {
                return redisClient.publish(channel, message);
            }

            @Override
            public Boolean checkCommandParam() {
                return null == channel || message == null;
            }
        }.command();
        if (commandResult == null) {
            commandResult = 0L;
        }
        return commandResult;
    }

    abstract class RedisCommand<T> {
        abstract T execute(RedisClient jedis) throws IOException;

        abstract Boolean checkCommandParam();

        public T command() {
            if (checkCommandParam()) {
                return null;
            }
            RedisClient redisClient = new RedisClient(mode, baseSingleRedis, baseClusterRedis);
            try {
                return execute(redisClient);
            } catch (Exception e) {
                log.warn("RedisCommand error", e);
            } finally {
                if (redisClient != null) {
                    redisClient.close();
                }
            }
            return null;
        }
    }
}
