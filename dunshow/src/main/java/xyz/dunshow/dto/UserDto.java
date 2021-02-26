package xyz.dunshow.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto extends BaseDto {

	private int usrSeq;
	private String email;
	private String sub;
	private String role;
	private String regDt;
	private String useYn;
}
