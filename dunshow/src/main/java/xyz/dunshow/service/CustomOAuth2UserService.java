package xyz.dunshow.service;

import java.util.Collections;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import xyz.dunshow.constants.SessionKey;
import xyz.dunshow.dto.UserDto;
import xyz.dunshow.dto.UserSession;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User>{

    private final UserService userService;

    private final HttpSession httpSession;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);
        Map<String, Object> attributes = oAuth2User.getAttributes();
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint()
                .getUserNameAttributeName();

        UserDto dto = new UserDto();
        dto.setEmail(oAuth2User.getAttribute("email"));
        dto.setSub(oAuth2User.getAttribute("sub"));

        dto = this.userService.getUserOrSaveUserBySub(dto);
        if ("N".equals(dto.getUseYn())) {
            throw new OAuth2AuthenticationException(new OAuth2Error("use_yn_n"), "사용불가 상태인 계정입니다.");
        }

        dto.setPicture(oAuth2User.getAttribute("picture"));
        UserSession userSession = new UserSession(dto);

        this.httpSession.setAttribute(SessionKey.LOGIN.getValue(), userSession);

        return new DefaultOAuth2User(Collections.singleton(new SimpleGrantedAuthority(dto.getRole())), attributes,
                userNameAttributeName);
    }
}
