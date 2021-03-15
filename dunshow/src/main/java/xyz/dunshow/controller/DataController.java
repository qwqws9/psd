package xyz.dunshow.controller;

import java.util.ArrayList;
import java.util.HashMap;
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
import xyz.dunshow.constants.CacheKey;
import xyz.dunshow.constants.ErrorMessage;
import xyz.dunshow.constants.UserRole;
import xyz.dunshow.dto.AjaxResponse;
import xyz.dunshow.dto.InfoDto;
import xyz.dunshow.dto.MarketMasterDto;
import xyz.dunshow.dto.UserSession;
import xyz.dunshow.exception.BusinessException;
import xyz.dunshow.exception.PageException;
import xyz.dunshow.mapper.MarketMasterMapper;
import xyz.dunshow.service.AdminService;
import xyz.dunshow.service.AvatarService;
import xyz.dunshow.service.DataService;
import xyz.dunshow.service.JobService;
import xyz.dunshow.service.OpenApiService;
import xyz.dunshow.util.ServerUtil;
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
    
    private final MarketMasterMapper marketMasterMapper;

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
    
    @RequestMapping("/avatar/auction")
    public String auctionView(Model model) {
        model.addAttribute("jobList", this.jobService.getJobList());
        model.addAttribute("partsList", this.jobService.getPartsList());
        model.addAttribute("jobDetailList", this.jobService.getJobDetailList());
        return "/avatar/auction";
    }
    
    @GetMapping("/auction.ajax")
    @ResponseBody
    @JsonView(Views.Simple.class)
    public AjaxResponse getAuction() {
        Map<String, Object> map = Maps.newHashMap();
        map.put("marketDate", ServerUtil.MARKET_DATA_TIME);

        if (CacheKey.FIND_ALL_MARKET_ORDER) {
//            map.put("data", this.marketMasterMapper.selectAllMasterAndDetail1());
        } else {
//            map.put("data", this.marketMasterMapper.selectAllMasterAndDetail2());
        }
        
        List<MarketMasterDto> list = this.marketMasterMapper.selectAllMasterAndDetail1();
        
        Map<String, Map<String, Map<String, Map<String, List<MarketMasterDto>>>>> map2 = Maps.newHashMap();
        
        for (MarketMasterDto m : list) {
            Map<String, Map<String, Map<String, List<MarketMasterDto>>>> jobMap = map2.getOrDefault("B"+m.getJobValue(), new HashMap<String, Map<String,Map<String,List<MarketMasterDto>>>>());
            Map<String, Map<String, List<MarketMasterDto>>> detailMap = jobMap.getOrDefault("D"+m.getJobDetailSeq(), new HashMap<String, Map<String,List<MarketMasterDto>>>());
            Map<String, List<MarketMasterDto>> degreeMap = detailMap.getOrDefault("G"+m.getDegree(), new HashMap<String, List<MarketMasterDto>>());
            List<MarketMasterDto> innerList = degreeMap.getOrDefault("E"+m.getEmblemCode(), new ArrayList<MarketMasterDto>());
            
            innerList.add(m);
            degreeMap.putIfAbsent("E"+m.getEmblemCode(), innerList);
            detailMap.putIfAbsent("G"+m.getDegree(), degreeMap);
            jobMap.putIfAbsent("D"+m.getJobDetailSeq()+"", detailMap);
            map2.putIfAbsent("B"+m.getJobValue(), jobMap);
        }
        
        map.put("outer", map2);
        return new AjaxResponse(map);
    }
}
