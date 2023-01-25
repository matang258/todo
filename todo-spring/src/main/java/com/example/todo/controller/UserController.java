package com.example.todo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.todo.dto.ResponseDTO;
import com.example.todo.dto.UserDTO;
import com.example.todo.model.UserEntity;
import com.example.todo.security.TokenProvider;
import com.example.todo.service.UserService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/auth")
public class UserController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private TokenProvider tokenProvider;
	
	//(패스워드 암호화 적용) Bean으로 작성해도 됨.(p288)
	private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	
	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@RequestBody UserDTO userDTO) {
		try {
			
			if(userDTO == null || userDTO.getPassword() == null) {
				throw new RuntimeException("Invalid Password value.");
			}
			// 요청을 이용해 저장할 유저 만들기
			UserEntity user = UserEntity.builder()
				.username(userDTO.getUsername())
				//.password(userDTO.getPassword())				
				.password(passwordEncoder.encode(userDTO.getPassword())) //패스워드 암호화 적용
				.build();
			
			//서비스를 이용해 리포지터리에 유저 저장
			UserEntity registeredUser = userService.create(user);
			
			//TodoDTO는 List<TodoDTO> dtos = entities.stream().map(TodoDTO:: new).collect(Collectors.toList());를
			//사용했는데 User는 리스트가 아니라서 그냥 생성해서 저장하는 듯
			//그리고 responseUserDTO는 class 따로 안만들고 그냥 UserDTO사용하네
			//TodoController의 (5)와 동일
			UserDTO responseUserDTO = UserDTO.builder()
					.id(registeredUser.getId())
					.username(registeredUser.getUsername())
					.password(registeredUser.getPassword()) //password는 없어서 항상 body로 null을 리턴해서 내가 추가함
					.build();
			//(6)번은 없는듯(ResponseDTO 사용하지 않고 UserDTO그대로 반환함)
			//(7)
			return ResponseEntity.ok().body(responseUserDTO);
		} catch (Exception e) {
			// 유저 정보는 항상 하나이므로 리스트로 만들어야 하는 ResponseDTO를 사용하지 않고 그냥 UserDTO리턴.
			ResponseDTO responseDTO = ResponseDTO.builder().error(e.getMessage()).build();
//			ResponseDTO responseDTO = ResponseDTO.builder()
//					.error(e.getMessage())
//					.build();
			
			return ResponseEntity
					.badRequest()
					.body(responseDTO);
		}
	}
	
	@PostMapping("/signin")
	public ResponseEntity<?> authenticate(@RequestBody UserDTO userDTO){
		/*
		UserEntity user = userService.getByCredentials(
				userDTO.getUsername(),
				userDTO.getPassword());*/
		//패스워드 암호화 적용
		UserEntity user = userService.getByCredentials( //이름과 암호가 일치하면 DB에서 UserEntity가져옴
				userDTO.getUsername(),
				userDTO.getPassword(),
				passwordEncoder);
		
		if(user != null) {
			// 토큰생성(TokenProvider사용) p262
			final String token = tokenProvider.create(user); //토큰 생성은 로그인시에만 딱 한번 생성한다(내가작성)
			//signup (5)와 동일
			final UserDTO responseUserDTO = UserDTO.builder() //signup과 다르게 final이 붙음
					.username(user.getUsername())
					.password(user.getPassword()) //password는 없어서 항상 body로 null을 리턴해서 내가 추가함
					.id(user.getId())
					.token(token) //p262에서 만든 token추가
					.build();
			/*
			//signup (5)와 동일
			final UserDTO responseUserDTO = UserDTO.builder() //signup과 다르게 final이 붙음
					.username(user.getUsername())
					.id(user.getId())
					.build();
			*/
			//signup (7)과 동일
			return ResponseEntity.ok().body(responseUserDTO);
			//이후 ApiService.js의 
			//export function signin(userDTO){ 를 실행하며
			//localStorage.setItem("ACCESS_TOKEN", response.token)을 통해 여기서 만든 새로운 토큰을 브라우저에 저장함(내가적음)
			
		} else {
			ResponseDTO responseDTO = ResponseDTO.builder()
					.error("Login faild.")
					.build();
			return ResponseEntity
					.badRequest()
					.body(responseDTO);
		}
	}
	
	
	
	
	
	
}
