package xyz.dunshow.service;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;

import com.google.common.collect.Maps;

import lombok.RequiredArgsConstructor;
import xyz.dunshow.constants.Server;
import xyz.dunshow.dto.InfoDto;
import xyz.dunshow.exception.BusinessException;
import xyz.dunshow.exception.PageException;
import xyz.dunshow.util.EnumCodeUtil;

@Service
@RequiredArgsConstructor
public class DataService {

    private final OpenApiService api;

    private final EnumCodeUtil enumCodeUtil;

    public Map<String, Object> getSearchDetail(String server, String characterId) {
        // 유효성 체크
        if (StringUtils.isEmpty(this.enumCodeUtil.get(Server.CODE, server))) {
            throw new BusinessException("서버 선택이 잘못되었습니다.");
        }
        if (StringUtils.isEmpty(characterId)) {
            throw new BusinessException("캐릭터명을 입력해 주세요.");
        }

        // 상세 조회
        JSONObject rs = this.api.getCharacterDetail(server, characterId);
        if (rs.isEmpty()) {
            throw new BusinessException("정상적인 접근이 아닙니다.");
        }

        InfoDto look;
        List<InfoDto> list = new ArrayList<InfoDto>();
        JSONArray jsonArr = (JSONArray) rs.get("avatar");

        for (Object o : jsonArr) {
            look = new InfoDto();
            JSONObject j = (JSONObject) o;
            String slotId = (String) j.get("slotId");
            String itemName = (String) j.get("itemName");

            // 오라나 무기압타는 제외
            if ("AURORA".equals(slotId) || "WEAPON".equals(slotId) || "AURA_SKIN".equals(slotId)) { continue; }
            look.setSlotName((String) j.get("slotName"));
            look.setItemId((String) j.get("itemId"));
            look.setItemName(itemName);

            JSONObject j2 = (JSONObject) j.get("clone");
            if (j2.get("itemId") != null) {
                // 클론아바타 장착시 원본 아바타 추출
                look.setItemId((String) j2.get("itemId"));
                look.setItemName((String) j2.get("itemName"));
            } else {
                if (StringUtils.isNotEmpty(itemName) && itemName.indexOf("클론") > -1) {
                    continue;
                }
            }

            list.add(look);
        }

        if (CollectionUtils.isEmpty(list)) {
            throw new PageException("입혀");
        }

        int totalPrice = 0;
        int totalAverage = 0;
        DecimalFormat format = new DecimalFormat("###,###");

        for (InfoDto i : list) {
            // 가격 조회
            rs = this.api.getAuction(i.getItemId(), null, "1");
            jsonArr = (JSONArray) rs.get("rows");
            if (!rs.get("rows").toString().trim().equals("[]")) {
                rs = (JSONObject) jsonArr.get(0);
                i.setUnitPrice(((Long) rs.get("unitPrice")) + "");
                i.setAveragePrice(((Long) rs.get("averagePrice")) + "");
            }
        }

        for (InfoDto i : list) {
            // 가격이 비었을경우 시세 조회 (최대 10건의 평균값)
            if (StringUtils.isEmpty(i.getUnitPrice())) {
                rs = this.api.getAuctionSold(i.getItemId(), null, "10");
                Long price = 0L;
                int count = 0;
                jsonArr = (JSONArray) rs.get("rows");
                if (!rs.get("rows").toString().trim().equals("[]")) {
                    for (Object o : jsonArr) {
                        count++;
                        JSONObject o2 = (JSONObject) o;
                        price += (Long) o2.get("unitPrice");
                    }
                    i.setAveragePrice(Math.round((price / count)) + "");
                }

                if (StringUtils.isEmpty(i.getAveragePrice())) {
                    i.setUnitPrice("교환 불가");
                    i.setAveragePrice("교환 불가");
                } else {
                    totalPrice += Integer.parseInt(i.getAveragePrice());
                    totalAverage += Integer.parseInt(i.getAveragePrice());
                    i.setUnitPrice("-");
                }

            } else {
                totalPrice += Integer.parseInt(i.getUnitPrice());
                if (StringUtils.isEmpty(i.getAveragePrice()) || "0".equals(i.getAveragePrice())) {
                    i.setAveragePrice("평균 거래가 없음");
                } else {
                    totalAverage += Integer.parseInt(i.getAveragePrice());
                }
            }

            i.setUnitPrice(StringUtils.isNumeric(i.getUnitPrice()) ? format.format(Integer.parseInt(i.getUnitPrice())) + " 골드" : i.getUnitPrice());
            i.setAveragePrice(StringUtils.isNumeric(i.getAveragePrice()) ? format.format(Integer.parseInt(i.getAveragePrice())) + " 골드" : i.getAveragePrice());
        }

        Map<String,Object> map = Maps.newHashMap();
        map.put("data", list);
        map.put("totalPrice", "최저가 합계 : " + format.format(totalPrice) + " 골드");
        map.put("totalAverage", "평균가 합계 : " + format.format(totalAverage) + " 골드");

        return map;
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
}
