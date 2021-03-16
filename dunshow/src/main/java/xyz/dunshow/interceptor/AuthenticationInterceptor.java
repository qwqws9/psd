package xyz.dunshow.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;

import xyz.dunshow.util.ServerUtil;


//@Slf4j
public class AuthenticationInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

    	
    	//log.info("URI:: {}", request.getRequestURI());
    	// 캐시데이터 생성하는동안 접근못하게 캐시사용하는 컨트롤러 접근막기
    	if (!ServerUtil.SERVER_STARTING) {
    		String rq = request.getRequestURI();
    		if (rq.startsWith("/data")) {
    			response.sendRedirect("/start");
    			return false;
    		}
    	}
    	
        return true;
    }

}