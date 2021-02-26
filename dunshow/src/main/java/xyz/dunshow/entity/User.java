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
@Table(name = "usr")
@Getter
@Setter
public class User extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int usrSeq;
	@Column
	private String email;
	@Column
	private String sub;
	@Column
	private String role;
	@Column(updatable = false)
	private String regDt;
	@Column(length = 1)
	private String useYn;
}
