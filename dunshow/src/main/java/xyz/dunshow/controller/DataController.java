package xyz.dunshow.controller;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.annotation.JsonView;
import com.google.common.collect.Maps;

import lombok.RequiredArgsConstructor;
import xyz.dunshow.constants.CacheKey;
import xyz.dunshow.dto.AjaxResponse;
import xyz.dunshow.service.AvatarService;
import xyz.dunshow.service.JobService;
import xyz.dunshow.service.MarketMasterService;
import xyz.dunshow.util.ServerUtil;
import xyz.dunshow.view.Views;

@Controller
@RequiredArgsConstructor
@RequestMapping("/data")
public class DataController extends BaseController{

    private final JobService jobService;

    private final AvatarService avatarService;

    private final MarketMasterService marketMasterService;

    /**
     *  아바타 정옵, 엠블렘 조회 페이지
     * @param model
     * @return
     */
    @RequestMapping("/option/ability")
    public String abilityView(Model model) {
        model.addAttribute("jobList", this.jobService.getJobList());
        model.addAttribute("partsList", this.jobService.getPartsList());
        model.addAttribute("jobDetailList", this.jobService.getJobDetailList());
        return "/option/ability";
    }

    /**
     *  아바타 정옵, 엠블렘 데이터 조회
     * @return
     */
    @GetMapping("/ability.ajax")
    @ResponseBody
    @JsonView(Views.Simple.class)
    public AjaxResponse getAbility() {
        Map<String, Object> map = Maps.newHashMap();
        map.put("data", this.avatarService.getCorrectOption());

        return new AjaxResponse(map);
    }

    /**
     *  아바타마켓 페이지
     * @param model
     * @return
     */
    @RequestMapping("/avatar/auction")
    public String auctionView(Model model) {
        model.addAttribute("jobList", this.jobService.getJobList());
        model.addAttribute("partsList", this.jobService.getPartsList());
        model.addAttribute("jobDetailList", this.jobService.getJobDetailList());
        return "/avatar/auction";
    }

    /**
     *  아바타 마켓 데이터 조회
     * @return
     */
    @GetMapping("/auction.ajax")
    @ResponseBody
    @JsonView(Views.Simple.class)
    public AjaxResponse getAuction() {
        Map<String, Object> map;

        if (CacheKey.FIND_ALL_MARKET_ORDER) {
            map = this.marketMasterService.getAllMasterAndDetail1();
            map.put("info", this.marketMasterService.getEmblemAllWithPrice1());
        } else {
            map = this.marketMasterService.getAllMasterAndDetail2();
            map.put("info", this.marketMasterService.getEmblemAllWithPrice2());
        }
        
        map.put("marketDate", ServerUtil.MARKET_DATA_TIME);

        return new AjaxResponse(map);
    }
}
