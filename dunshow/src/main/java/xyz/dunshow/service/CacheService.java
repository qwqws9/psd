package xyz.dunshow.service;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import xyz.dunshow.constants.CacheKey;

@Service
public class CacheService {

    @CacheEvict(value = CacheKey.FIND_ALL_SHOWROOM)
    public void evictFindAllShowRoom() {}

    @CacheEvict(value = CacheKey.FIND_ALL_MARKET_FRIST)
    public void evictFindAllMarketMaster1() {}

    @CacheEvict(value = CacheKey.FIND_ALL_MARKET_SECOND)
    public void evictFindAllMarketMaster2() {}

    @CacheEvict(value = CacheKey.FIND_ALL_EMBLEM_PRICE_FIRST)
    public void evictFindAllEmblemPrice1() {}

    @CacheEvict(value = CacheKey.FIND_ALL_EMBLEM_PRICE_SECOND)
    public void evictFindAllEmblemPrice2() {}
}
