package com.example.todo.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.todo.dto.ResponseDTO;
import com.example.todo.dto.TodoDTO;
import com.example.todo.model.TodoEntity;
import com.example.todo.service.TodoService;

@RestController
@RequestMapping("todo")
public class TodoController {
	
	@Autowired
	private TodoService service;
	
	//testTodo 메서드 작성하기(내가 TestController.java에 testResponseEntity보고 만든 것)
	@GetMapping("/todoResponseEntity")
	public ResponseEntity<?> todoControllerResponseEntity(){
		List<String> list = new ArrayList<>();
		list.add("todo ResponseEntity");
		
		ResponseDTO<String> response = ResponseDTO.<String>builder().data(list).build();
		return ResponseEntity.badRequest().body(response);
	}
	
	
	@GetMapping("/test")
	public ResponseEntity<?> testTodo(){
		String str = service.testService(); //테스트 서비스 사용
		List<String> list = new ArrayList<>();
		list.add(str);
		
		ResponseDTO<String> response = ResponseDTO.<String>builder().data(list).build();
		return ResponseEntity.ok().body(response);
		
	}
	
	@PostMapping
	public ResponseEntity<?> createTodo(@AuthenticationPrincipal String userId, @RequestBody TodoDTO dto){
		try{
			// String temporaryUserId = "temporary-user"; //temporary user id.
			// (1) TodoEntity로 변환한다.
			TodoEntity entity = TodoDTO.toEntity(dto);
			
			// (2) id를 null로 초기화한다. 생성 당시에는 id가 없어야 하기 때문이다.
			entity.setId(null);
			
			// (3) 임시 유저 아이디를 설정해준다. 이 부분은 4장 인증과 인가에서 수정할 예정이다. 
			//지금은 인증과 인가 기능이 없으므로(한 유저 temporary-user)만 로그인 없이 사용 가능한 애플리케이션인 셈이다.
			// entity.setUserId(temporaryUserId);
			// (3) Authentication Bearer Token을 통해 받은 userId를 넘긴다.
			entity.setUserId(userId); //실수로 entity.setId(userId);로 했더니 userId가 null로 데이터베이스에 저장되어서 저장 후 리턴되는 데이터가 userId가 없다보니 전체가 다 나옴
			
			// (4) 서비스를 이용해 Todo엔티티를 생성한다.
			List<TodoEntity> entities = service.create(entity);
			
			// (5) 자바 스트링을 이용해 리턴된 엔티티 리스트를 TodoDTO리스트로 변환한다..
			List<TodoDTO> dtos = entities.stream().map(TodoDTO:: new).collect(Collectors.toList());
			
			// (6) 변환된 TodoDTO리스트를 이용해 ResponseDTO를 초기화한다
			ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();
			
			// (7) ResponseDTO를 리턴한다.
			return ResponseEntity.ok().body(response);
			
			
		}catch(Exception e) {
			// (8) 혹시 예외가 나는 경우 dto대신 error에 메세지를 넣어 리턴한다.
			String error = e.getMessage();
			ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().error(error).build();
			return ResponseEntity.badRequest().body(response);
		}
	}
	
	@GetMapping
	public ResponseEntity<?> retrieveTodoList(@AuthenticationPrincipal String userId){
		// String temporaryUserId = "temporary-user"; //temporary user id.
		
		// (1) 서비스 메서드의 retrieve 메서드를 사용해 Todo 리스트를 가져온다.
		// List<TodoEntity> entities = service.retrieve(temporaryUserId);
		List<TodoEntity> entities = service.retrieve(userId);
		
		// (2) 자바 스트림을 이용해 리턴된 엔티티 리스트를 TodoDTO 리스트로 변환한다.
		List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());
		
		// (6) 변환된 TodoDTO리스트를 이용해 ResponseDTO를 초기화 한다.
		ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();
		
		// (7) ResponseDTO를 리턴한다.
		return ResponseEntity.ok().body(response);
		
	}
	
	@PutMapping
	public ResponseEntity<?> updateTodo(@AuthenticationPrincipal String userId, @RequestBody TodoDTO dto){
		// String temporaryUserId = "temporary-user"; //tempoaray user id.
		
		// (1) dto를 entity로 변환한다.
		TodoEntity entity = TodoDTO.toEntity(dto);
		
		// (2) id를 temporaryUserId로 초기화한다. 여기는 4강 인증과 인가에서 수정할 예정이다.
		// entity.setUserId(temporaryUserId);
		// (2) Authentication Bearer Token을 통해 받은 userId를 넘긴다. 
		entity.setUserId(userId);
		
		// (3) 서비스를 이용해 entity를 업데이트 한다.
		List<TodoEntity> entities = service.update(entity);
		
		// (4) 자바 스트림을 이용해 리턴된 엔티티 리스트를 TodoDTO 리스트로 변환한다.
		List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());
		
		// (5) 변환된 TodoDTO 리스트를 이용해 ResponseDTO를 초기화 한다.
		ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();
		
		// (6) ResponseDTO를 리턴한다.
		return ResponseEntity.ok().body(response);
		
	}
	
	@DeleteMapping
	public ResponseEntity<?> deleteTodo(@AuthenticationPrincipal String userId, @RequestBody TodoDTO dto){
		try {
			// String temporaryUserId = "temporary-user"; //tempoaray user id.
			
			// (1) TodoEntity로 변환한다.
			TodoEntity entity = TodoDTO.toEntity(dto);
			
			// (2) 임시 유저 아이디를 설정해준다. 이 부분은 4장 인증과 인가에서 수정할 예정이다. 
			// 지금은 인증과 인가 기능이 없으므로(한 유저 temporary-user)만 로그인 없이 사용 가능한 애플리케이션인 셈이다.
			// entity.setUserId(temporaryUserId);
			entity.setUserId(userId);
			
			// (3) 서비스를 이용해 entity를 삭제한다.
			List<TodoEntity> entities = service.delete(entity);
			
			// (4) 자바 스트림을 이용해 리턴된 엔티티 리스트를 TodoDTO 리스트로 변환한다.
			List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());
			
			// (5) 변환된 TodoDTO 리스트를 이용해 ResponseDTO를 초기화 한다.
			ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();
			
			// (6) ResponseDTO를 리턴한다.
			return ResponseEntity.ok().body(response);
			
		} catch(Exception e) {
			// (8) 혹시 예외가 나는 경우 dto대신 error에 메세지를 넣어 리턴한다.
			String error = e.getMessage();
			ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().error(error).build();
			return ResponseEntity.badRequest().body(response);
			
		}
	}
	
}
