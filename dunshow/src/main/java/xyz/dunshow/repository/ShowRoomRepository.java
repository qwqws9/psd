package xyz.dunshow.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import xyz.dunshow.entity.ShowRoom;

public interface ShowRoomRepository extends JpaRepository<ShowRoom, Integer>{

    List<ShowRoom> findByJobSeqAndPartsName(String jobSeq, String partsName);
    
    List<ShowRoom> findByJobSeq(String jobSeq);
    
    List<ShowRoom> findByItemIdIsNull();
    
}
