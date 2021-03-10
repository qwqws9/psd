package xyz.dunshow.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import xyz.dunshow.constants.Color;
import xyz.dunshow.constants.PartsName;
import xyz.dunshow.constants.Server;
import xyz.dunshow.interceptor.AuthenticationInterceptor;
import xyz.dunshow.util.EnumCodeUtil;


@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final LoginUserArgumentResolver loginUserResolver;

    public WebConfig(LoginUserArgumentResolver loginUserResolver) {
        this.loginUserResolver = loginUserResolver;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AuthenticationInterceptor()).excludePathPatterns(new String[] { "/static/**" });
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(loginUserResolver);
        WebMvcConfigurer.super.addArgumentResolvers(resolvers);
    }
    
    @Bean
    public EnumCodeUtil enumCodeUtil() {
        EnumCodeUtil enumCodeUtil = new EnumCodeUtil();
        enumCodeUtil.put(Server.CODE, Server.class);
        enumCodeUtil.put(Color.CODE, Color.class);
        enumCodeUtil.put(PartsName.CODE, PartsName.class);
        return enumCodeUtil;
    }
}
