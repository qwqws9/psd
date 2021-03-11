package xyz.dunshow.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.cache.annotation.Cacheable;

import xyz.dunshow.dto.ShowRoomDto;

@Mapper
public interface ShowRoomMapper {
	@Cacheable("mapper")
	List<ShowRoomDto> selectAll();
}
