package xyz.dunshow.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;

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
    
    public List<RankingDto> select() {
    	return this.rankingMapper.selectAll();
    }
    
    @Transactional
    public boolean insert(RankingDto param) {
    	List<Ranking> duplEntity = this.rankingRepository.findByCharacterId(param.getCharacterId());
    	
    	for (Ranking r : duplEntity) {
    		int p = Integer.parseInt(r.getPrice());
    		int s = Integer.parseInt(param.getPrice());
    		// -5% ~ +5% 제외
    		if ((p * 0.95) <= s && s <= (p * 1.05)) {
    			return false;
    		}
    	}
    	
    	List<Ranking> entity = this.rankingRepository.findByJobValue(param.getJobValue(), Sort.by(Order.asc("price")));
    	if (entity == null || entity.size() <= 10) {
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
    	
    	return true;
    }
}
