package xyz.dunshow.service;

import java.util.List;
import java.util.stream.Collectors;

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
        List<JobDto> list = ObjectMapperUtils.mapList(this.jobRepository.findAll(), JobDto.class);
        list = list.stream().filter(j -> !"99".equals(j.getJobValue())).collect(Collectors.toList());
        return list;
    }
    
    public List<JobDetailDto> getJobDetailList() {
        List<JobDetailDto> list = ObjectMapperUtils.mapList(this.jobDetailRepository.findAll(), JobDetailDto.class);
        list = list.stream().filter(j -> !"99".equals(j.getJobValue())).collect(Collectors.toList());
        return list;
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
