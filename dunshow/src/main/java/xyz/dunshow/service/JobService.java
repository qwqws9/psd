package xyz.dunshow.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

import lombok.RequiredArgsConstructor;
import xyz.dunshow.dto.JobDetailDto;
import xyz.dunshow.dto.JobDto;
import xyz.dunshow.dto.PartsDto;
import xyz.dunshow.repository.JobDetailRepository;
import xyz.dunshow.repository.JobRepository;
import xyz.dunshow.util.ObjectMapperUtils;

@Service
@RequiredArgsConstructor
public class JobService {

    private final JobRepository jobRepository;
    
    private final JobDetailRepository jobDetailRepository;
    
    public List<JobDto> getJobList() {
        return ObjectMapperUtils.mapList(this.jobRepository.findAll(), JobDto.class);
    }
    
    public List<JobDetailDto> getJobDetailList() {
        return ObjectMapperUtils.mapList(this.jobDetailRepository.findAll(), JobDetailDto.class);
    }
    
    public List<PartsDto> getPartsList() {
        PartsDto p = new PartsDto();
        List<PartsDto> list = Lists.newArrayList();
        
        String[] wearInfoEngArr = {"hair", "cap", "face", "neck", "coat", "pants", "belt", "shoes", "skin"};
        String[] wearInfoKorArr = {"머리", "모자", "얼굴", "목가슴", "상의", "하의", "허리", "신발", "피부"};
        
        for (int i = 0; i < wearInfoEngArr.length; i++) {
            p = new PartsDto();
            p.setPartsSeq(i+"");
            p.setPartsEngName(wearInfoEngArr[i]);
            p.setPartsKorName(wearInfoKorArr[i]);
            list.add(p);
        }
        
        return list;
    }
}
