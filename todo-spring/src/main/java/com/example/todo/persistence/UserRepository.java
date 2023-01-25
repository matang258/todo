package com.example.todo.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.todo.model.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, String> {

	//p250(메서드로 작성된 쿼리)
	UserEntity findByUsername(String username);
	Boolean existsByUsername(String username);
	UserEntity findByUsernameAndPassword(String username, String password);
	
}
