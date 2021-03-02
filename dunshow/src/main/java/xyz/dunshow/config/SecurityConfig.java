package xyz.dunshow.config;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import xyz.dunshow.constants.UserRole;
import xyz.dunshow.service.CustomOAuth2UserService;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	private final CustomOAuth2UserService customOAuth2UserService;
	
	public SecurityConfig(CustomOAuth2UserService customOAuth2UserService) {
		this.customOAuth2UserService = customOAuth2UserService;
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		 http
         .csrf().disable()
         .headers().frameOptions().disable() 
         .and()
             .authorizeRequests()
             .antMatchers("/favicon.ico", "/static/**", "/oauth2/**").permitAll()
             .antMatchers("/admin/**").hasRole(UserRole.
                                              ADMIN.getRole())
             .anyRequest().permitAll()//.authenticated()
         .and()
             .oauth2Login()
                 .userInfoEndpoint()
                     .userService(customOAuth2UserService);
		 
		 http.logout().deleteCookies("JSESSIONID")
		 .logoutUrl("/logout")
		 .logoutSuccessUrl("/main");
	}
	
}
