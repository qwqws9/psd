package xyz.dunshow.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.google.common.collect.Maps;

import lombok.RequiredArgsConstructor;
import xyz.dunshow.constants.CacheKey;
import xyz.dunshow.dto.EmblemRateDto;
import xyz.dunshow.dto.MarketMasterDto;
import xyz.dunshow.mapper.EmblemRateMapper;
import xyz.dunshow.mapper.MarketMasterMapper;

@Service
@RequiredArgsConstructor
public class MarketMasterService {

    private final MarketMasterMapper marketMasterMapper;

    private final EmblemRateMapper emblemRateMapper;

    @Cacheable(value = CacheKey.FIND_ALL_MARKET_FRIST)
    public Map<String, Object> getAllMasterAndDetail1() {
        Map<String, Object> map = Maps.newHashMap();
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

        return map;
    }

    @Cacheable(value = CacheKey.FIND_ALL_MARKET_SECOND)
    public Map<String, Object> getAllMasterAndDetail2() {
        Map<String, Object> map = Maps.newHashMap();
        List<MarketMasterDto> list = this.marketMasterMapper.selectAllMasterAndDetail2();

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

        return map;
    }

    @Cacheable(value = CacheKey.FIND_ALL_EMBLEM_PRICE_FIRST)
    public Map<String, Object> getEmblemAllWithPrice1() {
        List<EmblemRateDto> list = this.emblemRateMapper.selectAllWithPrice();
        Map<String, Object> map = Maps.newHashMap();
        Map<String, Map<String, List<EmblemRateDto>>> outerMap = Maps.newHashMap();

        for (EmblemRateDto e : list) {
            Map<String, List<EmblemRateDto>> detailMap = outerMap.getOrDefault("D" + e.getJobDetailSeq(), new HashMap<String, List<EmblemRateDto>>());
            List<EmblemRateDto> emblemMap = detailMap.getOrDefault(e.getEmblemColor(), new ArrayList<EmblemRateDto>());

            emblemMap.add(e);

            detailMap.putIfAbsent(e.getEmblemColor(), emblemMap);
            outerMap.putIfAbsent("D" + e.getJobDetailSeq(), detailMap);
        }

        map.put("price", outerMap);

        return map;
    }

    @Cacheable(value = CacheKey.FIND_ALL_EMBLEM_PRICE_SECOND)
    public Map<String, Object> getEmblemAllWithPrice2() {
        List<EmblemRateDto> list = this.emblemRateMapper.selectAllWithPrice();
        Map<String, Object> map = Maps.newHashMap();
        Map<String, Map<String, List<EmblemRateDto>>> outerMap = Maps.newHashMap();
        
        for (EmblemRateDto e : list) {
            Map<String, List<EmblemRateDto>> detailMap = outerMap.getOrDefault("D" + e.getJobDetailSeq(), new HashMap<String, List<EmblemRateDto>>());
            List<EmblemRateDto> emblemMap = detailMap.getOrDefault(e.getEmblemColor(), new ArrayList<EmblemRateDto>());

            emblemMap.add(e);

            detailMap.putIfAbsent(e.getEmblemColor(), emblemMap);
            outerMap.putIfAbsent("D" + e.getJobDetailSeq(), detailMap);
        }

        map.put("price", outerMap);

        return map;
    }
}
