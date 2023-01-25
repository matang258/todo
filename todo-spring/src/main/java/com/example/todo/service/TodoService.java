package com.example.todo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.todo.model.TodoEntity;
import com.example.todo.persistence.TodoRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TodoService {

	@Autowired
	private TodoRepository repository;
	
	//test
	public String testService() {
		//TodoEntity 생성
		TodoEntity entity = TodoEntity.builder().title("My first todo item").build();
		//TodoEntity 저장
		repository.save(entity);
		//TodoEntity 검색
		TodoEntity saveEntity = repository.findById(entity.getId()).get();
		
		//return "Test Servcie";
		return saveEntity.getTitle();
	}
	
	//p141
	public List<TodoEntity> create(final TodoEntity entity) {
		//Validations		
		//validate(entity); //@@p286테스트 할때 userID가 null이기 때문에 Exception을 던져서 잠시 주석
		
		repository.save(entity);
		
		log.info("Entity id : {} is saved.", entity.getId());
		log.info("Entity userId : {} is saved.", entity.getUserId()); //추가해봄

		/*//내가 테스트 해본 것
		 * List<TodoEntity> list = new ArrayList<>();
		 * list.add(repository.findByUserId(entity.getUserId()).get(2)); return list;
		 */
		
		return repository.findByUserId(entity.getUserId());
	}
	
	private void validate(final TodoEntity entity) {
		if(entity == null) {
			log.warn("Entity cannot be null.");
			throw new RuntimeException("Entity cannot be null");
		}
		
		if(entity.getUserId() == null) {
			log.warn("Unknown user.");
			throw new RuntimeException("Unknown user");
		}
	}
	
	//p147
	public List<TodoEntity> retrieve(final String userId) {
		return repository.findByUserId(userId);
	}
	
	
	//p150
	public List<TodoEntity> update(final TodoEntity entity){
		// (1) 저장할 엔티티가 유효한지 확인한다. 이 메서드는 2.3.1 Create Todo에서 구현했다.
		validate(entity);
		// (2) 넘겨받은 엔티티 id를 이용해 TodoEntity를 가져온다. 존재하지 않는 엔티티는 업데이트 할 수 없기 때문이다.
		final Optional<TodoEntity> original = repository.findById(entity.getId());
		
		// [1.Optional과 Lambda 사용]
		original.ifPresent(todo -> {
			// (3) 반환된 TodoEntity가 존재하면 값을 새 entity의 값으로 덮어 씌운다.
			todo.setTitle(entity.getTitle());
			todo.setDone(entity.isDone());

			// (4) 데이터베이스에 새 값을 저장한다.
			repository.save(todo);
		});
		// [2.Optional과 Lambda를 사용하지 않은 코드(p151 ,예제 2-51)]
		/*
		if(original.isPresent()) {
			// (3) 반환된 TodoEntity가 존재하면 값을 새 entity의 값으로 덮어 씌운다.
			final TodoEntity todo = original.get();
			todo.setTitle(entity.getTitle());
			todo.setDone(entity.isDone());
			
			// (4) 데이터베이스에 새 값을 저장한다.
			repository.save(todo);
		}*/
		
		// 2.3.2 Retrieve Todo(p147)에서 만든 메서드를 이용해 유저의 모든 Todo 리스트를 리턴한다.
		return retrieve(entity.getUserId()); //id가 없는거라도 여기 때문에 전체 entity가 리턴된다. 
											//id값 자체를 안넘기면 500번 에러(위에서 나는 듯)(이클립스에서도 java.lang.IllegalArgumentException: The given id must not be null! 에러 발생)
	}
	
	//p154
	public List<TodoEntity> delete(final TodoEntity entity){
		// (1) 저장할 엔티티가 유효한지 확인한다. 이 메서드는 2.3.1 Create Todo에서 구현했다.
		validate(entity);
		
		try {
			// (2)엔티티를 삭제한다.
			repository.delete(entity);
		} catch(Exception e) {
			// (3) exception 발생 시 id와 exception을 로깅한다.
			log.error("error deleting entity", entity.getId(), e);
			
			// (4) 컨트롤러로 exception을 날린다. 데이터베이스 내부 로직을 캡슐화하기 위해 e를 리턴하지 않고 새 exception 오브젝트를 리턴한다.
			throw new RuntimeException("error deleting entity" + entity.getId());
		}
		// (5) 새 Todo리스트를 가져와 리턴한다.
		return retrieve(entity.getUserId());
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
