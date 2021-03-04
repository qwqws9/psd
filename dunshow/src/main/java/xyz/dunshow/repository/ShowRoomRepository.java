package xyz.dunshow.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import xyz.dunshow.entity.ShowRoom;

public interface ShowRoomRepository extends JpaRepository<ShowRoom, Integer>{

    List<ShowRoom> findByJobValueAndPartsName(String jobValue, String partsName);
    
    List<ShowRoom> findByJobValue(String jobValue);
    
    List<ShowRoom> findByItemIdIsNull();
    
}
