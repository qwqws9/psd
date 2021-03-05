package xyz.dunshow.service;

import java.util.Date;

import javax.transaction.Transactional;

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

    @Transactional
    public UserDto getUserOrSaveUserBySub(UserDto dto) {
        User entity = this.userRepository.findBySub(dto.getSub());

        if (entity == null) {
            dto.setUseYn("Y");
            dto.setRole(UserRole.USER.getValue());
            dto.setRegDt(DateUtils.getDateString(new Date()));
            // 관리자계정 설정
            if ("dunshowprice@gmail.com".equals(dto.getEmail())) { dto.setRole(UserRole.ADMIN.getValue()); }
            entity = this.userRepository.save(dto.toEntity(User.class));
        }

        entity.setLastDt(DateUtils.getDateStringDetail(new Date()));

        return entity.toDto(UserDto.class);
    }
}
