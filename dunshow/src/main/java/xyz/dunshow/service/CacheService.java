package xyz.dunshow.service;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import xyz.dunshow.constants.CacheKey;

@Service
public class CacheService {

	@CacheEvict(value = CacheKey.FIND_ALL_SHOWROOM)
	public void evictFindAllShowRoom() {}
}
