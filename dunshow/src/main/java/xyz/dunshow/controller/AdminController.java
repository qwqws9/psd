package xyz.dunshow.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.UrlPathHelper;

import com.fasterxml.jackson.annotation.JsonView;
import com.google.common.collect.Maps;

import lombok.RequiredArgsConstructor;
import xyz.dunshow.annotation.LoginUser;
import xyz.dunshow.constants.ErrorMessage;
import xyz.dunshow.constants.UserRole;
import xyz.dunshow.dto.AjaxResponse;
import xyz.dunshow.dto.InfoDto;
import xyz.dunshow.dto.UserSession;
import xyz.dunshow.exception.BusinessException;
import xyz.dunshow.exception.PageException;
import xyz.dunshow.service.AdminService;
import xyz.dunshow.service.DataService;
import xyz.dunshow.service.JobService;
import xyz.dunshow.service.OpenApiService;
import xyz.dunshow.view.Views;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController extends BaseController{

    private final JobService jobService;

    private final AdminService adminService;

    private final OpenApiService openApiService;

    private final DataService dataService;

    @GetMapping("/dataManage")
    public String main(Model model, @LoginUser UserSession userSession) {
        if (!UserRole.ADMIN.getValue().equals(userSession.getRole())) {
            throw new PageException("권한이 없습니다.");
        }

        model.addAttribute("jobList", this.jobService.getJobList());

        return "admin/dataManage";
    }

    @GetMapping("/data.ajax")
    @ResponseBody
    @JsonView(Views.Simple.class)
    public AjaxResponse data() {
    	
        Map<String, Object> map = Maps.newHashMap();
        map.put("data", "1234");
//        System.out.println(super.getRequest().getHeader("Referer"));
//        System.out.println(super.checkReferer("/admin/dataManage"));
        return new AjaxResponse(map);
    }

    @GetMapping("/dataManage.ajax")
    @ResponseBody
    @JsonView(Views.Simple.class)
    public AjaxResponse dataManage(String target, @LoginUser UserSession userSession) {
        if (!UserRole.ADMIN.getValue().equals(userSession.getRole())) {
            throw new BusinessException(ErrorMessage.PERMISSION_DENIED.getMessage());
        }

        if ("initShowRoomData".equals(target)) {
            this.adminService.initShowRoomData();
        } else if ("bakShowRoomData".equals(target)) {
            this.adminService.bakShowRoomData();
        } else if ("getEmblem".equals(target)) {
            this.adminService.getEmblem();
        } else if ("initEmblemData".equals(target)) {
            this.adminService.initEmblemData();
        } else if ("bakEmblemData".equals(target)) {
            this.adminService.bakEmblemData();
        } else if ("initJobDetail".equals(target)) {
            this.adminService.getJobDetail();
        } else if ("ioIdInsert".equals(target)) {
            this.openApiService.ioIdInsert();
        } else if ("getRankByDundam".equals(target)) {
            this.adminService.getRankByDundam();
        } else if ("initRankData".equals(target)) {
            this.adminService.initRankData();
        } else if ("testCase".equals(target)) {  // 테스트 메서드
            this.adminService.test();
        } else if ("bakRankData".equals(target)) {
            this.adminService.bakRankData();
        } else if ("initOptionAndEmblemByRankData".equals(target)) {
            this.adminService.initOptionAndEmblemByRankData();
        } else if ("testDataCase".equals(target)) {
        	this.dataService.test();
        } else if ("initEmblemRateData".equals(target)) {
        	this.adminService.initEmblemRateData();
        } else if ("bakEmblemRateData".equals(target)) {
        	this.adminService.bakEmblemRateData();
        } else if ("initOptionAbilityData".equals(target)) {
        	this.adminService.initOptionAbilityData();
        } else if ("bakOptionAbilityData".equals(target)) {
        	this.adminService.bakOptionAbilityData();
        }

        return new AjaxResponse("200", "성공");
    }

    @GetMapping("/charIdTest.ajax")
    @ResponseBody
    @JsonView(Views.Simple.class)
    public AjaxResponse charIdTest(String server, String name) {
        Map<String, Object> map = Maps.newHashMap();

        List<InfoDto> list = this.openApiService.getCharacterId(server, name, "50");
        map.put("data", list);

        return new AjaxResponse(map);
    }
}
