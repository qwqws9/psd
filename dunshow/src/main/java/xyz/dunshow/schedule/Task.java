package xyz.dunshow.schedule;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import xyz.dunshow.constants.CacheKey;
import xyz.dunshow.entity.ShowRoom;
import xyz.dunshow.repository.ShowRoomRepository;
import xyz.dunshow.service.AvatarService;
import xyz.dunshow.service.CacheService;

@Component
@RequiredArgsConstructor
public class Task {

	private final ShowRoomRepository showRoomRepository;
	
	private final AvatarService avatarService;
	
	private final CacheService cacheService;

//	@Scheduled(fixedDelay = 100000, initialDelay = 500)
//	@Transactional
//	@CacheEvict(value = "testCache")
	public void task1() {
		// 삭제
		
		// API 조회
		
		// 정제
		
		// 생성
		
		// 캐시 삭제
		this.cacheService.evictFindAllShowRoom();
		
		// 조회 (캐시 생성)
		System.out.println("캐시 삭제");
	}
	
//	@Scheduled(fixedDelay = 3000, initialDelay = 1000)
	public void task2() {
//		System.out.println(DateUtils.getDateStringDetail(new Date()));
		long startTime = System.currentTimeMillis();
//		List<ShowRoom> list = this.showRoomRepository.findAll();
		List<ShowRoom> list = this.avatarService.getAllAvatarList();
		
		for (ShowRoom s : list) {
//			if (s.getShowRoomSeq() == test) {
//				s.setItemId(DateUtils.getDateStringDetail(new Date()));
//			}
		}
		
//		test++;
		long endTime = System.currentTimeMillis();
		System.out.println((endTime - startTime));
		System.out.println("스케줄러 실행 끝");
	}
}
