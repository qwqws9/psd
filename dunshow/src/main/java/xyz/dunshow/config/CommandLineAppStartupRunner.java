package xyz.dunshow.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import xyz.dunshow.service.AvatarService;

@Component
@RequiredArgsConstructor
public class CommandLineAppStartupRunner implements CommandLineRunner{

	private final AvatarService avatarService;
	
	@Override
	public void run(String... args) throws Exception {
		System.out.println("서버 구동완료..");
//		System.out.println("캐시 생성 시작");
		System.out.println("아직 캐시 생성없음 ");
		System.out.println("서버구동시 @Schdule에 등록되지 않고 캐시를 사용하고 있는 쿼리 날리기");
		System.out.println("이 문구는 CommandLineAppStartupRunner.java");
		System.out.println();
		System.out.println("스케줄 관리는 Task.java 확인");
		System.out.println();
		// this.avatarService.getAllAvatarList();
//		System.out.println("캐시 생성 완료");
	}
}
