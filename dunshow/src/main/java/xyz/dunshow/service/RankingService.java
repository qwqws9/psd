package xyz.dunshow.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.transaction.Transactional;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import lombok.RequiredArgsConstructor;
import xyz.dunshow.dto.RankingDto;
import xyz.dunshow.entity.Ranking;
import xyz.dunshow.mapper.RankingMapper;
import xyz.dunshow.repository.RankingRepository;

@Service
@RequiredArgsConstructor
public class RankingService {

    private final RankingMapper rankingMapper;
    
    private final RankingRepository rankingRepository;
    
    public void select() {
    	List<RankingDto> list = this.rankingMapper.selectAll();
    	Map<String, List<RankingDto>> map = Maps.newHashMap();
    	List<RankingDto> rank = Lists.newArrayList();
    	
    	
    	for (RankingDto r : list) {
    		if (rank.size() != 5) {
    			rank.add(r);
    		}
    		
    		List<RankingDto> l = map.getOrDefault(r.getJobValue(), new ArrayList<RankingDto>());
    		l.add(r);
    		map.putIfAbsent(r.getJobValue(), l);
    	}
    	
    	for (Entry<String, List<RankingDto>> entry : map.entrySet()) {
    		for (RankingDto r : entry.getValue()) {
    			r.getPrice();
    		}
    	}
    }
    
    @Transactional
    public void insert(RankingDto param) {
    	// 현재 데이터 중복으로 들어감. 조회 후 현재 등록된 가격보다 낮으면 return
    	List<Ranking> entity = this.rankingRepository.findByJobValue(param.getJobValue(), Sort.by(Order.asc("price")));
    	
    	if (entity == null || entity.size() < 5) {
    		this.rankingRepository.save(param.toEntity(Ranking.class));
    	} else {
    		for (Ranking r : entity) {
    			if (Integer.parseInt(r.getPrice()) < Integer.parseInt(param.getPrice())) {
    				this.rankingRepository.delete(r);
    				this.rankingRepository.save(param.toEntity(Ranking.class));
    				break;
    			}
    		}
    	}
    }
}
