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
import xyz.dunshow.service.AvatarService;
import xyz.dunshow.service.DataService;
import xyz.dunshow.service.JobService;
import xyz.dunshow.service.OpenApiService;
import xyz.dunshow.view.Views;

@Controller
@RequiredArgsConstructor
@RequestMapping("/data")
public class DataController extends BaseController{

    private final JobService jobService;

    private final AdminService adminService;

    private final OpenApiService openApiService;

    private final DataService dataService;
    
    private final AvatarService avatarService;

    @RequestMapping("/option/ability")
    public String abilityView(Model model) {
        model.addAttribute("jobList", this.jobService.getJobList());
        model.addAttribute("partsList", this.jobService.getPartsList());
        model.addAttribute("jobDetailList", this.jobService.getJobDetailList());
        return "/option/ability";
    }

    @GetMapping("/ability.ajax")
    @ResponseBody
    @JsonView(Views.Simple.class)
    public AjaxResponse getAbility() {
        Map<String, Object> map = Maps.newHashMap();
        map.put("data", this.avatarService.getCorrectOption());

        return new AjaxResponse(map);
    }
    
}
