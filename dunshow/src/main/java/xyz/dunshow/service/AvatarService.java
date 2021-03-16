package xyz.dunshow.service;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import xyz.dunshow.constants.CacheKey;
import xyz.dunshow.dto.InfoDto;
import xyz.dunshow.entity.ShowRoom;
import xyz.dunshow.mapper.EmblemRateMapper;
import xyz.dunshow.mapper.OptionAbilityMapper;
import xyz.dunshow.repository.ShowRoomRepository;

@Service
@RequiredArgsConstructor
public class AvatarService {

    private final ShowRoomRepository showRoomRepository;

    private final EmblemRateMapper emblemRateMapper;

    private final OptionAbilityMapper optionAbilityMapper;

    public List<ShowRoom> getAvatarListByJobSeq(String jobValue) {
        return this.showRoomRepository.findByJobValue(jobValue);
    }

    @Cacheable(value = CacheKey.FIND_ALL_SHOWROOM)
    public List<ShowRoom> getAllAvatarList() {
        return this.showRoomRepository.findAll();
    }

    @Cacheable(value = CacheKey.FIND_ALL_CORRECT_OPTION)
    public InfoDto getCorrectOption() {
        InfoDto info = new InfoDto();
        info.setEmblemRates(this.emblemRateMapper.selectAll());
        info.setOptionAbilitys(this.optionAbilityMapper.selectAll());

        return info;
    }
}
