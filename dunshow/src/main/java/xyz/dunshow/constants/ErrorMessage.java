package xyz.dunshow.constants;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ErrorMessage {
    ERROR("오류가 발생하였습니다."),
    PERMISSION_DENIED("사용정지 계정입니다.");

    private final String message;
}
