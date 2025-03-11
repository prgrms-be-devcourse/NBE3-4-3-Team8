package com.ll.nbe342team8.domain.order.order.service;

import com.ll.nbe342team8.domain.order.order.dto.OrderCacheDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class OrderCacheService {
    private final RedisTemplate<String, Object> redisTemplate;
    private static final String ORDER_CACHE_PREFIX = "order:";
    private static final long ORDER_CACHE_TTL = 10; // 10분

    /**
     * 주문 정보를 Redis에 캐싱
     */
    public void cacheOrder(String tossOrderId, OrderCacheDto orderCacheDto) {
        String key = ORDER_CACHE_PREFIX + tossOrderId;
        redisTemplate.opsForValue().set(key, orderCacheDto);
        redisTemplate.expire(key, ORDER_CACHE_TTL, TimeUnit.MINUTES);
    }

    /**
     * Redis에서 주문 정보 조회
     */
    public OrderCacheDto getOrderFromCache(String tossOrderId) {
        String key = ORDER_CACHE_PREFIX + tossOrderId;
        Object cachedOrder = redisTemplate.opsForValue().get(key);

        if (cachedOrder == null) {
            return null;
        }

        return (OrderCacheDto) cachedOrder;
    }

    /**
     * Redis에서 주문 정보 삭제
     */
    public void deleteOrderFromCache(String tossOrderId) {
        String key = ORDER_CACHE_PREFIX + tossOrderId;
        redisTemplate.delete(key);
    }
}
