package xyz.dunshow.controller;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.annotation.JsonView;
import com.google.common.collect.Maps;

import lombok.RequiredArgsConstructor;
import xyz.dunshow.dto.AjaxResponse;
import xyz.dunshow.dto.ShowRoomDto;
import xyz.dunshow.entity.ShowRoom;
import xyz.dunshow.exception.BusinessException;
import xyz.dunshow.exception.PageException;
import xyz.dunshow.service.AvatarService;
import xyz.dunshow.service.JobService;
import xyz.dunshow.view.Views;

@Controller
@RequiredArgsConstructor
@RequestMapping("/avatar")
public class AvatarController {

    private final AvatarService avatarService;
    
    private final JobService jobService;
    
    @GetMapping("/showroom")
    public String test(Model model) {
        
        model.addAttribute("jobList", this.jobService.getJobList());
        model.addAttribute("partsList", this.jobService.getPartsList());
        
        return "avatar/showroom";
    }
    
    @GetMapping("/data.ajax")
    @ResponseBody
    @JsonView(Views.Simple.class)
    public AjaxResponse test1(String jobSeq) {
        if (StringUtils.isEmpty(jobSeq) || !StringUtils.isNumeric(jobSeq)) {
            throw new BusinessException("직업 선택이 잘못 되었습니다.");
        }
        int check = Integer.parseInt(jobSeq);
        if (check < 0 || check > 15) {
            throw new BusinessException("직업 선택이 잘못 되었습니다.");
        }
        
        if ("9".equals(jobSeq)) {
            jobSeq = "0";
        }
        if ("10".equals(jobSeq)) {
            jobSeq = "3";
        }
        
        Map<String, Object> map = Maps.newHashMap();
        
//        List<ShowRoomDto> dtoList = this.avatarService.getAvatarListByJobSeq(jobSeq);
        List<ShowRoom> dtoList = this.avatarService.getAvatarListByJobSeq(jobSeq);
        
        map.put("data", dtoList);
        
        return new AjaxResponse(map);
    }
}
