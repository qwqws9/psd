package xyz.dunshow.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import xyz.dunshow.entity.OptionAbility;

public interface OptionAbilityRepository extends JpaRepository<OptionAbility, Integer>{

	List<OptionAbility> findByChoiceOptionAndJobDetailSeqIn(String choiceOption, List<Integer> jobDetailSeq);
}
