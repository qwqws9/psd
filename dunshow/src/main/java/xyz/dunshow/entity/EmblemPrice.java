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
@Table(name = "emblem_price")
@Getter
@Setter
public class EmblemPrice extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int emblemPriceSeq;
	@Column
    private String emblemName;
	@Column
    private String price;
}
