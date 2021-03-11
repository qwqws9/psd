package xyz.dunshow.schedule;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import xyz.dunshow.constants.ApiKey;
import xyz.dunshow.constants.CacheKey;
import xyz.dunshow.dto.ShowRoomDto;
import xyz.dunshow.entity.ShowRoom;
import xyz.dunshow.mapper.MarketMasterMapper;
import xyz.dunshow.mapper.ShowRoomMapper;
import xyz.dunshow.repository.ShowRoomRepository;
import xyz.dunshow.service.AvatarService;
import xyz.dunshow.service.CacheService;
import xyz.dunshow.service.DataService;

@Component
@RequiredArgsConstructor
public class Task {

	private final ShowRoomRepository showRoomRepository;
	
	private final AvatarService avatarService;
	
	private final CacheService cacheService;
	
	private final ShowRoomMapper showRoomMapper;
	
	private final MarketMasterMapper marketMasterMapper;
	
	private final DataService dataService;

	// 서버 시작하고 약 41분 후 부터 5분 간격으로 초기화
//	@Scheduled(fixedDelay = 300000, initialDelay = 2500000)
	public void task1() {
		System.out.println("마켓 캐시 초기화 시작");
		// Market 데이터 초기화
		this.dataService.initMarketMaster();
		
		// 캐시 삭제 및 생성
		if (CacheKey.FIND_ALL_MARKET_ORDER) {
			this.cacheService.evictFindAllMarketMaster1();
			this.marketMasterMapper.selectAllMasterAndDetail1();
		} else {
			this.cacheService.evictFindAllMarketMaster2();
			this.marketMasterMapper.selectAllMasterAndDetail2();
		}
		
		CacheKey.FIND_ALL_MARKET_ORDER = !CacheKey.FIND_ALL_MARKET_ORDER;
	}
	
//	@Scheduled(fixedDelay = 3000, initialDelay = 1000)
	public void task2() {
		long startTime = System.currentTimeMillis();
		
		if (!CacheKey.FIND_ALL_MARKET_ORDER) {
			this.marketMasterMapper.selectAllMasterAndDetail1();
		} else {
			this.marketMasterMapper.selectAllMasterAndDetail2();
		}
		
		long endTime = System.currentTimeMillis();
		
		
		System.out.println((endTime - startTime) + "밀리초");
		System.out.println("스케줄러 실행 끝");
	}
}
