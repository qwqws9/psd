package xyz.dunshow.constants;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import xyz.dunshow.dto.EnumCode;

@RequiredArgsConstructor
@Getter
public enum PartsName implements EnumCode {

	HAIR("머리 아바타", "hair"),
	CAP("모자 아바타", "cap"),
	FACE("얼굴 아바타", "face"),
	NECK("목가슴 아바타", "neck"),
	COAT("상의 아바타", "coat"),
	PANTS("하의 아바타", "pants"),
	BELT("허리 아바타", "belt"),
	SHOES("신발 아바타", "shoes"),
	SKIN("피부 아바타", "skin");

    public static final String CODE = "PARTS";
    private final String value;
    private final String desc;
}
