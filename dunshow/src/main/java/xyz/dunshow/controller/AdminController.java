package xyz.dunshow.controller;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.annotation.JsonView;
import com.google.common.collect.Maps;

import lombok.RequiredArgsConstructor;
import xyz.dunshow.annotation.LoginUser;
import xyz.dunshow.constants.UserRole;
import xyz.dunshow.dto.AjaxResponse;
import xyz.dunshow.dto.UserSession;
import xyz.dunshow.entity.ShowRoom;
import xyz.dunshow.exception.PageException;
import xyz.dunshow.repository.ShowRoomRepository;
import xyz.dunshow.service.AdminService;
import xyz.dunshow.service.FileIoService;
import xyz.dunshow.service.JobService;
import xyz.dunshow.view.Views;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {
    
    private final JobService jobService;
    
    private final FileIoService fileIoService;
    
    private final AdminService adminService;
    
	@GetMapping("/avatarManage")
    public String main(Model model, @LoginUser UserSession userSession) {
	    if (!UserRole.ADMIN.getValue().equals(userSession.getRole())) {
	        throw new PageException("권한이 없습니다.");
	    }
	    
	    model.addAttribute("jobList", this.jobService.getJobList());
	    
	    
        return "admin/avatarManage";
    }
	
	@GetMapping("/data.ajax")
	@ResponseBody
	@JsonView(Views.Simple.class)
	public AjaxResponse data() {
	    Map<String, Object> map = Maps.newHashMap();
	    
	    map.put("data", this.jobService.getJobList());
	    
	    return new AjaxResponse(map);
	}
	
	@GetMapping("/ioInsert.ajax")
    @ResponseBody
    @JsonView(Views.Simple.class)
    public AjaxResponse ioInsert(String job, String target) {
        this.fileIoService.fileReader(job, target);
        
        return new AjaxResponse("200", "성공");
    }
	
	@GetMapping("/ioIdInsert.ajax")
	@ResponseBody
	@JsonView(Views.Simple.class)
	public AjaxResponse ioIdInsert(String job, String target) {
	    this.fileIoService.ioIdInsert(job, target);
	    return new AjaxResponse("200", "성공");
	}
	
	@GetMapping("/emblem.ajax")
    @ResponseBody
    @JsonView(Views.Simple.class)
    public AjaxResponse getDnfNowEmbl(String job, String target) {
	    this.adminService.getEmblem();
        return new AjaxResponse("200", "성공");
    }
}