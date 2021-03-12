package xyz.dunshow.service;

import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import xyz.dunshow.constants.CacheKey;
import xyz.dunshow.dto.EmblemRateDto;
import xyz.dunshow.dto.InfoDto;
import xyz.dunshow.dto.OptionAbilityDto;
import xyz.dunshow.entity.EmblemRate;
import xyz.dunshow.entity.OptionAbility;
import xyz.dunshow.entity.ShowRoom;
import xyz.dunshow.mapper.EmblemRateMapper;
import xyz.dunshow.mapper.OptionAbilityMapper;
import xyz.dunshow.repository.EmblemRateRepository;
import xyz.dunshow.repository.OptionAbilityRepository;
import xyz.dunshow.repository.ShowRoomRepository;
import xyz.dunshow.util.ObjectMapperUtils;

@Service
@RequiredArgsConstructor
public class AvatarService {

    private final OptionAbilityRepository optionAbilityRepository;

    private final EmblemRateRepository emblemRateRepository;

    private final ShowRoomRepository showRoomRepository;
    
    private final EmblemRateMapper emblemRateMapper;
    
    private final OptionAbilityMapper optionAbilityMapper;
    
    public List<ShowRoom> getAvatarListByJobSeq(String jobValue) {
        List<ShowRoom> list = this.showRoomRepository.findByJobValue(jobValue);
        
//        return ObjectMapperUtils.mapList(list, ShowRoomDto.class);
        return list;
    }
    
    @Cacheable(value = CacheKey.FIND_ALL_SHOWROOM)
    public List<ShowRoom> getAllAvatarList() {
        List<ShowRoom> list = this.showRoomRepository.findAll();
        
        //List<ShowRoomDto> dtoList = ObjectMapperUtils.mapList(list, ShowRoomDto.class);
//        return ObjectMapperUtils.mapList(list, ShowRoomDto.class);
        return list;
    }

    @Cacheable(value = CacheKey.FIND_ALL_CORRECT_OPTION)
    public InfoDto getCorrectOption() {
    	InfoDto info = new InfoDto();
    	info.setEmblemRates(this.emblemRateMapper.selectAll());
    	info.setOptionAbilitys(this.optionAbilityMapper.selectAll());

        return info;
    }
}
