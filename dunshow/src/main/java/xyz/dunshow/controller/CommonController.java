package xyz.dunshow.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.RequiredArgsConstructor;
import xyz.dunshow.annotation.LoginUser;
import xyz.dunshow.dto.UserSession;
import xyz.dunshow.service.OpenApiService;

@Controller
@RequiredArgsConstructor
public class CommonController {
    
    private final OpenApiService openApiService;
    
	@GetMapping("/main")
    public String main(Model model, @LoginUser UserSession userSession) {
		model.addAttribute("email", userSession.getEmail());
		model.addAttribute("sub", userSession.getSub());
        return "main";
    }
	
	@RequestMapping("/user/login")
	public String login() {
		
		return "login/userLogin";
	}
	
	@RequestMapping("/test")
	@ResponseBody
	public String b() {
		return "";
	}
	
	
}
