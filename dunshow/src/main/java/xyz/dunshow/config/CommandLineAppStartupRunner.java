package xyz.dunshow.config;

import java.util.Date;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import xyz.dunshow.constants.ApiKey;
import xyz.dunshow.constants.CacheKey;
import xyz.dunshow.mapper.MarketMasterMapper;
import xyz.dunshow.service.AvatarService;
import xyz.dunshow.service.DataService;
import xyz.dunshow.service.MarketMasterService;
import xyz.dunshow.util.DateUtils;
import xyz.dunshow.util.ServerUtil;

@Component
@RequiredArgsConstructor
public class CommandLineAppStartupRunner implements CommandLineRunner{

	private final AvatarService avatarService;
	
	private final MarketMasterMapper marketMasterMapper;
	
	private final DataService dataService;
	
	private final MarketMasterService marketMasterService;
	
	@Override
	public void run(String... args) throws Exception {
		ServerUtil.SERVER_RUNTIME = DateUtils.getServerDate(new Date());
		
		System.out.println("서버 구동완료..");
//		System.out.println("캐시 생성 시작");
		System.out.println("아직 캐시 생성없음 ");
		System.out.println("서버구동시 @Schdule에 등록되지 않고 캐시를 사용하고 있는 쿼리 날리기");
		System.out.println("이 문구는 CommandLineAppStartupRunner.java");
		System.out.println();
		System.out.println("스케줄 관리는 Task.java 확인");
		System.out.println();

		// 옵션조회
		this.avatarService.getCorrectOption();
		// 마켓데이터 초기화
//		this.dataService.initMarketMaster();
		
		// 운영시 삭제
		this.marketMasterService.getAllMasterAndDetail1();
		this.marketMasterService.getEmblemAllWithPrice1();
		ServerUtil.MARKET_DATA_TIME = DateUtils.getServerDate(new Date());
//		this.avatarService.getAllAvatarList();
		
		System.out.println("서버 데이터 준비가 완료되었습니다.");
		ServerUtil.SERVER_STARTING = !ServerUtil.SERVER_STARTING;
//		System.out.println("캐시 생성 완료");
	}
}
