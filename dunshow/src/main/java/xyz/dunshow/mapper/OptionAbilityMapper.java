package xyz.dunshow.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import xyz.dunshow.dto.OptionAbilityDto;

@Mapper
public interface OptionAbilityMapper {
	
	List<OptionAbilityDto> selectAll();
}
