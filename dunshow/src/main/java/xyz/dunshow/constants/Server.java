package xyz.dunshow.constants;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import xyz.dunshow.dto.EnumCode;

@RequiredArgsConstructor
@Getter
public enum Server implements EnumCode {

	CAIN("cain", "카인" ),
	DIREGIE("diregie", "디레지에"),
	SIROCO("siroco", "시로코"),
	PREY("prey", "프레이"),
	CASILLAS("casillas", "카시야스"),
	HILDER("hilder", "힐더"),
	ANTON("anton", "안톤"),
	BAKAL("bakal", "바칼"),
	ALL("all", "전체");

    public static final String CODE = "SERVER";
    private final String value;
    private final String desc;
}
