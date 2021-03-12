package xyz.dunshow.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import xyz.dunshow.dto.RankingDto;

@Mapper
public interface RankingMapper {
	
	void deleteMinPriceOfJobValue(String jobValue);
	
	List<RankingDto> selectAll();
}
