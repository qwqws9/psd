package xyz.dunshow.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "option_ability")
@Getter
@Setter
public class OptionAbility extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int optionAbilitySeq;
	@Column
    private int jobDetailSeq;
	@Column
    private String partsName;
	@Column
    private String choiceOption;
	@Column
    private String rate;
}
