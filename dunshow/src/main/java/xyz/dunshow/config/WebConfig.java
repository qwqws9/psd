package xyz.dunshow.config;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import xyz.dunshow.interceptor.AuthenticationInterceptor;


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
}
