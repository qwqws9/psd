package xyz.dunshow.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.RequiredArgsConstructor;
import xyz.dunshow.util.ServerUtil;

@Controller
@RequiredArgsConstructor
public class CommonController {

	@GetMapping("/main")
    public String main(Model model) {
        return "main";
    }
	
	@GetMapping("/start")
	public String start(Model model) {
		// 서버가 실행중일땐 메인으로
		if (ServerUtil.SERVER_STARTING) {
			return "redirect:/main";
		}
		model.addAttribute("time", ServerUtil.SERVER_RUNTIME);
		return "start";
	}
}
