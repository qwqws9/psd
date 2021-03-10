package xyz.dunshow.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import xyz.dunshow.entity.OptionAbility;

public interface OptionAbilityRepository extends JpaRepository<OptionAbility, Integer>{

	OptionAbility findByChoiceOption(String choiceOption);
}
