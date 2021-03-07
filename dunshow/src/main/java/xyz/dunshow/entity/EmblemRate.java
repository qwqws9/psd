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
@Table(name = "emblem_rate")
@Getter
@Setter
public class EmblemRate extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int emblemRateSeq;
	@Column
    private int jobDetailSeq;
	@Column
    private String emblemId;
	@Column
    private String emblemName;
	@Column
	private String rate;
	@Column
	private String emblemColor;
}
