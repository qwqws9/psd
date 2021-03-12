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
@Table(name = "ranking")
@Getter
@Setter
public class Ranking extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int rankingSeq;
	@Column
    private String serverName;
	@Column
    private String characterName;
	@Column
    private String characterId;
	@Column
    private String jobValue;
	@Column
    private String price;
}
