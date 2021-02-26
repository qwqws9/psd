package xyz.dunshow.constants;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import xyz.dunshow.dto.EnumCode;

/**
 * 유저 권한
 */
@RequiredArgsConstructor
@Getter
public enum UserRole implements EnumCode {

    ADMIN("ADM", "관리자"),
    USER("USR", "사원");

    public static final String CODE = "ROLE";
    private final String value;
    private final String desc;

    @Override
    public String getValue() {
        return "ROLE_" + this.value;
    }
    public String getRole() {
        return this.value;
    }
}