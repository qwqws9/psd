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
		System.out.println("캐시 생성 시작");
		// 서버구동시 @스케줄에 등록되지 않고 캐시를 사용하고 있는 쿼리 날리기
		// this.avatarService.getAllAvatarList();
		System.out.println("캐시 생성 완료");
	}
}
