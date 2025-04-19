package com.xxl.mq.admin.router.impl;

import com.xxl.mq.admin.router.PartitionRouter;
import com.xxl.mq.admin.util.PartitionUtil;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

import static com.xxl.mq.admin.util.PartitionUtil.MAX_PARTITION;

public class CyclePartitionRouter implements PartitionRouter {

    private static ConcurrentMap<Integer, AtomicInteger> routeCountEachTopic = new ConcurrentHashMap<>();
    private static long CACHE_VALID_TIME = 0;

    private static int calculate(int paramData) {

        // cache clear
        if (System.currentTimeMillis() > CACHE_VALID_TIME) {
            routeCountEachTopic.clear();
            CACHE_VALID_TIME = System.currentTimeMillis() + 1000*60*60*24;
        }

        AtomicInteger count = routeCountEachTopic.get(paramData);
        if (count == null || count.get() > 1000000) {
            // 初始化时主动Random一次，缓解首次压力
            count = new AtomicInteger(new Random().nextInt(100));
        } else {
            // count++
            count.addAndGet(1);
        }
        routeCountEachTopic.put(paramData, count);
        return count.get();
    }

    @Override
    public int route(String topic, String partitionKey, Map<String, PartitionUtil.PartitionRange> instancePartitionRange) {
        int topicHashCode = topic.hashCode()>0
                ?topic.hashCode()% MAX_PARTITION
                :(topic.hashCode() & Integer.MAX_VALUE) % MAX_PARTITION;
        return calculate(topicHashCode);
    }
}
