package xyz.dunshow.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.cache.annotation.Cacheable;

import xyz.dunshow.constants.CacheKey;
import xyz.dunshow.dto.MarketMasterDto;

@Mapper
public interface MarketMasterMapper {
	
    void deleteAll();
    
    @Cacheable(value =CacheKey.FIND_ALL_MARKET_FRIST)
    List<MarketMasterDto> selectAllMasterAndDetail1();

    @Cacheable(value =CacheKey.FIND_ALL_MARKET_SECOND)
    List<MarketMasterDto> selectAllMasterAndDetail2();
}
