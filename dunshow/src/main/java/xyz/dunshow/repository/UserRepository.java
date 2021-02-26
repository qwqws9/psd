package xyz.dunshow.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import xyz.dunshow.entity.User;

public interface UserRepository extends JpaRepository<User, Integer>{

	User findBySub(String sub);
}
