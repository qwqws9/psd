package xyz.dunshow.controller;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.annotation.JsonView;
import com.google.common.collect.Maps;

import lombok.RequiredArgsConstructor;
import xyz.dunshow.dto.AjaxResponse;
import xyz.dunshow.dto.InfoDto;
import xyz.dunshow.dto.SubmitDto;
import xyz.dunshow.entity.ShowRoom;
import xyz.dunshow.exception.BusinessException;
import xyz.dunshow.service.AvatarService;
import xyz.dunshow.service.DataService;
import xyz.dunshow.service.JobService;
import xyz.dunshow.service.OpenApiService;
import xyz.dunshow.view.Views;

@Controller
@RequiredArgsConstructor
@RequestMapping("/avatar")
public class AvatarController {

    private final AvatarService avatarService;

    private final JobService jobService;

    private final OpenApiService openApiService;

    private final DataService dataService;
    
//    private final 

    /**
     *  showroom 페이지조회
     * @param model
     * @return
     */
    @GetMapping("/showroom")
    public String test(Model model) {
        model.addAttribute("jobList", this.jobService.getJobList());
        model.addAttribute("partsList", this.jobService.getPartsList());

        return "avatar/showroom";
    }

    /**
     *  avatar 정보 조회
     * @param jobSeq
     * @return
     */
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

        if ("9".equals(jobSeq)) { jobSeq = "0"; }
        if ("10".equals(jobSeq)) { jobSeq = "3"; }

        Map<String, Object> map = Maps.newHashMap();
        List<ShowRoom> dtoList = this.avatarService.getAvatarListByJobSeq(jobSeq);
        map.put("data", dtoList);

        return new AjaxResponse(map);
    }

    /**
     *  가격 조회
     * @param param
     * @return
     */
    @PostMapping("/showroom/getAuction.ajax")
    @ResponseBody
    @JsonView(Views.Simple.class)
    public AjaxResponse test1(@RequestBody SubmitDto param) {
        if (param == null || CollectionUtils.isEmpty(param.getChoice()) || param.getChoice().size() > 9) {
            throw new BusinessException("파라미터 오류");
        }

        return new AjaxResponse(this.dataService.getAuctionList(param.getChoice()));
    }

    /**
     *  캐릭터 조회
     * @param server
     * @param name
     * @param model
     * @return
     */
    @GetMapping("/search/list")
    public String searchList(String server, String name, Model model) {
        List<InfoDto> list = this.openApiService.getCharacterId(server, name, "50");
        if (CollectionUtils.isEmpty(list)) {
            model.addAttribute("msg", "서버, 캐릭터명을 확인해 주세요.");
        }

        model.addAttribute("data", list);

        return "search/list";
    }

    /**
     *  캐릭터 상세 조회
     * @param serverId
     * @param characterId
     * @param model
     * @return
     */
    @RequestMapping("/search/detail/{serverId}/{characterId}")
    public String searchDetail(@PathVariable String serverId, @PathVariable String characterId, Model model) {

        Map<String, Object> map = this.dataService.getSearchDetail(serverId, characterId);

        model.addAttribute("data", map.get("data"));
        model.addAttribute("totalPrice", map.get("totalPrice"));
        model.addAttribute("totalAverage", map.get("totalAverage"));
        model.addAttribute("imgSrc", "https://img-api.neople.co.kr/df/servers/" + serverId +"/characters/"+ characterId + "?zoom=1");

        return "search/detail";
    }

}
