package xyz.dunshow.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import xyz.dunshow.entity.Ranking;

public interface RankingRepository extends JpaRepository<Ranking, Integer>{

	List<Ranking> findByJobValue(String jobValue, Sort sort);
	
	List<Ranking> findByCharacterId(String characterId);
}
