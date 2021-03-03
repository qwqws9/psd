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
@Table(name = "job_detail")
@Getter
@Setter
public class JobDetail extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int jobDetailSeq;
	@Column
    private String firstJob;
	@Column
    private String secondJob;
	@Column
    private String thirdJob;
	@Column
	private String fourthJob;
	@Column
    private String jobValue;
//	@ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "jobValue", insertable = false, updatable = false)
//    private Job job;
//	@OneToMany(fetch = FetchType.LAZY)
//	@JoinColumn(name = "jobDetailSeq", insertable = false, updatable = false)
//	private List<Emblem> emblem;
}
