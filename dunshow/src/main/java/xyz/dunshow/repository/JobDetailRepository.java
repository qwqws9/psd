package xyz.dunshow.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import xyz.dunshow.entity.JobDetail;

public interface JobDetailRepository extends JpaRepository<JobDetail, Integer>{

}
