package xyz.dunshow.schedule;

import java.util.Date;
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
import xyz.dunshow.service.MarketMasterService;
import xyz.dunshow.util.DateUtils;
import xyz.dunshow.util.ServerUtil;

@Component
@RequiredArgsConstructor
public class Task {

    private final CacheService cacheService;

    private final MarketMasterMapper marketMasterMapper;

    private final DataService dataService;

    private final MarketMasterService marketMasterService;

//    @Scheduled(fixedDelay = 300000)
    public void task1() {
        System.out.println("마켓 캐시 초기화 시작");
        // Market 데이터 초기화
        this.dataService.initMarketMaster();
        
        // 캐시 삭제 및 생성
        if (!CacheKey.FIND_ALL_MARKET_ORDER) {
            this.cacheService.evictFindAllEmblemPrice1();
            this.cacheService.evictFindAllMarketMaster1();
            
            this.marketMasterService.getAllMasterAndDetail1();
            this.marketMasterService.getEmblemAllWithPrice1();
        } else {
            this.cacheService.evictFindAllEmblemPrice2();
            this.cacheService.evictFindAllMarketMaster2();
            
            this.marketMasterService.getAllMasterAndDetail2();
            this.marketMasterService.getEmblemAllWithPrice2();
        }
        
        ServerUtil.MARKET_DATA_TIME = DateUtils.getServerDate(new Date());
        CacheKey.FIND_ALL_MARKET_ORDER = !CacheKey.FIND_ALL_MARKET_ORDER;
    }
    
//    @Scheduled(fixedDelay = 3000, initialDelay = 1000)
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
