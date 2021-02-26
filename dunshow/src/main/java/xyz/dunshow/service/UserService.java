package xyz.dunshow.service;

import java.util.Date;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import xyz.dunshow.constants.UserRole;
import xyz.dunshow.dto.UserDto;
import xyz.dunshow.entity.User;
import xyz.dunshow.repository.UserRepository;
import xyz.dunshow.util.DateUtils;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;
	
	public UserDto getUserOrSaveUserBySub(UserDto dto) {
		
		User entity = this.userRepository.findBySub(dto.getSub());
		
		if (entity == null) {
			if ("dunshowprice@gmail.com".equals(dto.getEmail())) { dto.setRole(UserRole.ADMIN.getValue()); }
			dto.setUseYn("Y");
			dto.setRole(UserRole.USER.getValue());
			dto.setRegDt(DateUtils.getDateString(new Date()));
			entity = this.userRepository.save(dto.toEntity(User.class));
		}
		
		return entity.toDto(UserDto.class);
	}
}
