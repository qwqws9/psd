package xyz.dunshow.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import xyz.dunshow.dto.EmblemRateDto;

@Mapper
public interface EmblemRateMapper {
	
	List<EmblemRateDto> selectAll();
}
