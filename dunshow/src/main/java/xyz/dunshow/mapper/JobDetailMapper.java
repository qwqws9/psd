package xyz.dunshow.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import xyz.dunshow.dto.JobDetailDto;

@Mapper
public interface JobDetailMapper {
	List<JobDetailDto> selectJobDetailList(JobDetailDto param);
}
