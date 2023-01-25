package com.example.todo.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.todo.model.TodoEntity;

@Repository
public interface TodoRepository extends JpaRepository<TodoEntity, String> {	
	// ?1은 메서드의 매개변수의 순서 위치이다.
//	@Query("select * from TodoEntity t where t.userId = ?1")
//	List<TodoEntity> findByUserIdQuery(String userId);
	
	//git원본
	@Query("SELECT t FROM TodoEntity t WHERE t.userId = ?1")
	TodoEntity findByUserIdQuery(String userId);
	
	
	
//	@Query("select * from TodoEntity t where t.userId = ?")
	List<TodoEntity> findByUserId(String userId);
	
	//git원본(@Query없음)
	//List<TodoEntity> findByUserId(String userId);

	  
}
