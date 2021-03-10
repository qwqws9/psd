package xyz.dunshow.constants;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import xyz.dunshow.dto.EnumCode;

@RequiredArgsConstructor
@Getter
public enum Color implements EnumCode {

	RED("붉은빛", "red"),
	YELLOW("노란빛", "yellow"),
	BLUE("푸른빛", "blue"),
	GREEN("녹색빛", "green"),
	ALL("다색", "all"),
	PLATINUM("플래티넘", "platinum");

    public static final String CODE = "COLOR";
    private final String value;
    private final String desc;
}
