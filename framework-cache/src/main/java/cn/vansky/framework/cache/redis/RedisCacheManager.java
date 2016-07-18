/*
 * Copyright (C) 2015 CK, Inc. All Rights Reserved.
 */

package cn.vansky.framework.cache.redis;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * a implement of CacheManager in Redis
 * <p>
 * a example:
 *
 * <pre>
 * {@code
 * <bean id="myCacheManager" class="cn.vansky.framework.core.cache.redis.RedisCacheManager">
 *     <property name="clientList">
 *         <list>
 *             <ref local="client"/>
 *         </list>
 *     </property>
 * </bean>
 *
 * <bean id="client" class="cn.vansky.framework.core.cache.redis.RedisClient">
 *     <property name="redisServer">
 *         <value>127.0.0.1</value>
 *     </property>
 *     <property name="port">
 *         <value>6379</value>
 *     </property>
 *     <property name="redisAuthKey">
 *         <value>123456</value>
 *     </property>
 * </bean>
 *
 *
 * }
 * </pre>
 * Author: CK
 * Date: 2015/12/1
 */
public class RedisCacheManager extends AbstractRedisCacheManager {
    /**
     * 日志类
     */
    private final Log LOGGER = LogFactory.getLog(getClass());

    @Override
    public String toString() {
        int size = 0;
        if (CollectionUtils.isNotEmpty(getClientList())) {
            size = getClientList().size();
        }
        return "RedisCacheManager clientSize[" + size + "]";
    }

    @Override
    protected List<RedisClient> getClients(Object key) {
        RedisClient client = getClient(key);
        if (client == null) {
            LOGGER.error("Redis Cache Manager get redis client is null.");
            return Collections.emptyList();
        }
        return Arrays.asList(client);
    }

    /**
     * get cache client. use hash code to select client
     *
     * @param key
     *            cache key
     * @return {@link RedisClient} client
     */
    private RedisClient getClient(Object key) {
        if (CollectionUtils.isEmpty(getClientList())) {
            return null;
        }
        List<RedisClient> copied = new ArrayList<RedisClient>(getClientList());
        int index = Math.abs(key.toString().hashCode() % copied.size());
        return copied.get(index);
    }
}
