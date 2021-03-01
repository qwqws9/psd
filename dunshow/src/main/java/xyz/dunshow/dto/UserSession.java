package xyz.dunshow.dto;

import java.io.Serializable;

import lombok.Getter;

@Getter
public class UserSession implements Serializable {
    private static final long serialVersionUID = 1L;

    private int usrSeq;
    private String email;
    private String sub;
    private String role;
    private String picture;

    public UserSession(UserDto user) {
        this.usrSeq = user.getUsrSeq();
        this.email = user.getEmail();
        this.sub = user.getSub();
        this.role = user.getRole();
        this.picture = user.getPicture();
    }
}
