package xyz.dunshow.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import xyz.dunshow.dto.MarketMasterDto;

@Mapper
public interface MarketMasterMapper {
	
    void deleteAll();
    
    List<MarketMasterDto> selectAllMasterAndDetail1();
    
    List<MarketMasterDto> selectAllMasterAndDetail2();
}
