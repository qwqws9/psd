package xyz.dunshow.service;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import xyz.dunshow.dto.ShowRoomDto;
import xyz.dunshow.entity.ShowRoom;
import xyz.dunshow.repository.ShowRoomRepository;
import xyz.dunshow.util.ObjectMapperUtils;

@Service
@RequiredArgsConstructor
public class AvatarService {

    private final ShowRoomRepository showRoomRepository;
    
    public List<ShowRoom> getAvatarListByJobSeq(String jobSeq) {
        List<ShowRoom> list = this.showRoomRepository.findByJobSeq(jobSeq);
        
//        return ObjectMapperUtils.mapList(list, ShowRoomDto.class);
        return list;
    }
    
    public List<ShowRoomDto> getAllAvatarList() {
        List<ShowRoom> list = this.showRoomRepository.findAll();
        
        //List<ShowRoomDto> dtoList = ObjectMapperUtils.mapList(list, ShowRoomDto.class);
        return ObjectMapperUtils.mapList(list, ShowRoomDto.class);
    }
}
