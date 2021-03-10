package xyz.dunshow.service;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import lombok.RequiredArgsConstructor;
import xyz.dunshow.constants.CacheKey;
import xyz.dunshow.constants.Color;
import xyz.dunshow.constants.PartsName;
import xyz.dunshow.constants.Server;
import xyz.dunshow.dto.InfoDto;
import xyz.dunshow.dto.JobDto;
import xyz.dunshow.dto.MarketDetailDto;
import xyz.dunshow.dto.MarketMasterDto;
import xyz.dunshow.entity.Job;
import xyz.dunshow.entity.JobDetail;
import xyz.dunshow.entity.MarketDetail;
import xyz.dunshow.entity.MarketMaster;
import xyz.dunshow.entity.OptionAbility;
import xyz.dunshow.exception.BusinessException;
import xyz.dunshow.mapper.MarketDetailMapper;
import xyz.dunshow.mapper.MarketMasterMapper;
import xyz.dunshow.repository.EmblemRepository;
import xyz.dunshow.repository.JobDetailRepository;
import xyz.dunshow.repository.JobRepository;
import xyz.dunshow.repository.MarketDetailRepository;
import xyz.dunshow.repository.MarketMasterRepository;
import xyz.dunshow.repository.OptionAbilityRepository;
import xyz.dunshow.util.EncodeUtil;
import xyz.dunshow.util.EnumCodeUtil;
import xyz.dunshow.util.ObjectMapperUtils;

@Service
@RequiredArgsConstructor
public class DataService {

    private final OpenApiService api;

    private final EnumCodeUtil enumCodeUtil;
    
    private final JobRepository jobRepository;
    
    private final MarketMasterRepository marketMasterRepository;
    
    private final MarketDetailRepository marketDetailRepository;
    
    private final OptionAbilityRepository optionAbilityRepository;
    
    private final EmblemRepository emblemRepository;
    
    private final JobDetailRepository jobDetailRepository;
    
    private final MarketMasterMapper marketMasterMapper;
    
    private final MarketDetailMapper marketDetailMapper;
    
    private final JobService jobService;

    public Map<String, Object> getSearchDetail(String server, String characterId) {
        // 유효성 체크
        if (StringUtils.isEmpty(this.enumCodeUtil.get(Server.CODE, server))) {
            throw new BusinessException("서버 선택이 잘못되었습니다.");
        }
        if (StringUtils.isEmpty(characterId)) {
            throw new BusinessException("캐릭터명을 입력해 주세요.");
        }

        // equip avatar 조회
        JSONObject rs = this.api.getEquipAvatar(server, characterId);
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

        return getAuctionList(list);
    }
    
    public Map<String, Object> getAuctionList(List<InfoDto> list) {
    	if (CollectionUtils.isEmpty(list)) {
            throw new BusinessException("입혀");
        }

        int totalPrice = 0;
        int totalAverage = 0;
        JSONObject rs;
        JSONArray jsonArr;
        DecimalFormat format = new DecimalFormat("###,###");

        for (InfoDto i : list) {
            // 가격 조회
            rs = this.api.getAuction(i.getItemId(), i.getItemName(), "1");
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
                rs = this.api.getAuctionSold(i.getItemId(), i.getItemName(), "10");
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
    
    
    // 데이터 초기화시 ALTER TABLE [TABLE명] AUTO_INCREMENT = [시작할 값]; mst, detail seq 초기화 시켜주기
    @Transactional
//    @CacheEvict(beforeInvocation = true, value = "")
    public void test() {
    	//this.marketDetailRepository.deleteAll();
    	//this.marketMasterRepository.deleteAll();
        this.marketDetailMapper.deleteAll();
        this.marketMasterMapper.deleteAll();
    	List<JobDto> jobList = this.jobService.getJobList();
    	StringBuilder sb = new StringBuilder();
    	String[] code = {"100", "110", "120", "130", "999"};
    	
    	long startTime = System.currentTimeMillis();
    	for (JobDto j : jobList) {
    		if(StringUtils.isEmpty(j.getJobDesc())) { continue; }
    		

    		// method call
    		for (int i = 1; i < 17; i++) {
    			sb.setLength(0);
        		sb.append(j.getJobDesc());
        		sb.append(" ");
        		if (i == 16) {
        			sb.append("클론");
        		} else {
        			sb.append(i);
        			sb.append("차 레어");
        		}
    			for (int k = 0; k < code.length; k++) {
    				this.getMarket(EncodeUtil.encodeURIComponent(sb.toString()), j.getJobId(), code[k], j.getJobValue());
    			}
    		}
    	}
    	long endTime = System.currentTimeMillis();
    	
    	System.out.println("총 걸린시간...");
    	System.out.println((endTime - startTime) + "밀리초");
    }
    
    // 100, 110, 120, 130, 999
    private void getMarket(String searchTitle, String jobId, String emblemCode, String jobValue) {
    	Map<String, String> colorMap = this.enumCodeUtil.getMap(Color.CODE);
    	Map<String, String> partsMap = this.enumCodeUtil.getMap(PartsName.CODE);
    	List<JobDetail> list = this.jobDetailRepository.findByJobValue(jobValue);
    	List<Integer> jobDetail = Lists.newArrayList();
    	for (JobDetail j : list) {
    	    jobDetail.add(j.getJobDetailSeq());
    	}
    	
    	MarketMasterDto master;
    	MarketDetailDto detail;
    	List<MarketDetailDto> detailList;
    	String jacketOption = null;
    	JSONObject rs = this.api.getMarket(searchTitle, emblemCode, EncodeUtil.encodeURIComponent("레어"), jobId, "front", "8", "9", "50");
    	JSONArray arr = (JSONArray) rs.get("rows");
    	
    	for (Object o : arr) {
    		master = new MarketMasterDto();
    		detailList = Lists.newArrayList();
    		
    	    JSONObject obj = (JSONObject) o;
    	    master.setJobValue(jobValue);
    	    master.setEmblemCode(emblemCode);
    	    master.setTitle(obj.get("title").toString());
    	    master.setPrice(obj.get("price").toString());
    	    
    	    JSONArray arr2 = (JSONArray) obj.get("avatar");
    	    for (Object o2 : arr2) {
    	    	detail = new MarketDetailDto();
    	    	
    	        JSONObject obj2 = (JSONObject) o2;
    	        detail.setSlotId(partsMap.get(obj2.get("slotName").toString())); // hair
    	        detail.setItemName(obj2.get("itemName").toString());
    	        detail.setChoiceOption(obj2.get("optionAbility") == null ? null : obj2.get("optionAbility").toString());
    	        
    	        if ("coat".equals(detail.getSlotId())) {
    	            jacketOption = detail.getChoiceOption(); // 이걸로 먼저 JOB_VALUE 조회 후 정해서 하위옵들 조회
    	        }
    	        
    	        JSONArray arr3 = (JSONArray) obj2.get("emblems");
    	        int count = 1;
    	        for (Object o3 : arr3) {
    	            JSONObject obj3 = (JSONObject) o3;
    	            String slotColor = colorMap.get(obj3.get("slotColor").toString());
    	            if (count == 1) { detail.setEmblemColor1(slotColor); detail.setEmblemName1(obj3.get("itemName").toString()); }
    	            else if (count ==2) { detail.setEmblemColor2(slotColor); detail.setEmblemName2(obj3.get("itemName").toString()); }
    	            else { detail.setEmblemColor3(slotColor); detail.setEmblemName3(obj3.get("itemName").toString()); }
    	            count++;
    	        }
    	        
    	        detailList.add(detail);
    	    }
    	    
    	    List<OptionAbility> optionEntity = this.optionAbilityRepository.findByChoiceOptionAndJobDetailSeqIn(jacketOption, jobDetail);
    	    int jobDetailSeq = 99;
    	    if (optionEntity != null) {
    	        String prevRate = "0.0";
    	        for (OptionAbility op : optionEntity) {
    	            if (Double.parseDouble(prevRate) <= Double.parseDouble(op.getRate())) {
    	                jobDetailSeq = op.getJobDetailSeq();
    	            }
    	            prevRate = op.getRate();
    	        }
    	    }
    	    master.setJobDetailSeq(jobDetailSeq);
    	    MarketMaster entityMst = this.marketMasterRepository.save(ObjectMapperUtils.map(master, MarketMaster.class));
    	    
    	    for (MarketDetailDto m : detailList) {
    	    	m.setMarketMstSeq(entityMst.getMarketMstSeq());
    	    }
    	    
    	    this.marketDetailRepository.saveAll(ObjectMapperUtils.mapList(detailList, MarketDetail.class));
    	}
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
}
