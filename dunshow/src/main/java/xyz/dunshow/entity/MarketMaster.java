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
@Table(name = "market_mst")
@Getter
@Setter
public class MarketMaster extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int marketMstSeq;
	@Column
	private String title;
	@Column
    private int jobDetailSeq;
	@Column
    private String price;
	@Column
	private String emblemCode;
	@Column
	private String jobValue;
}
