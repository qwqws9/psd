package xyz.dunshow.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import xyz.dunshow.entity.Job;

public interface JobRepository extends JpaRepository<Job, Integer>{

}
