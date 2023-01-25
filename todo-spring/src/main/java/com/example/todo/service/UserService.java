package com.example.todo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.todo.model.UserEntity;
import com.example.todo.persistence.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;
	
	public UserEntity create(final UserEntity userEntity) {
		if(userEntity == null || userEntity.getUsername() == null) {
			throw new RuntimeException("Invalid arguments");
		}
		
		final String username = userEntity.getUsername();
		
		if(userRepository.existsByUsername(username)) {
			log.warn("Username already exists {}", username);
			throw new RuntimeException("Usernmae already exists");
		}
		
		return userRepository.save(userEntity);
			
	}
	
	/*
	public UserEntity getByCredentials(final String username, final String password) {
		return userRepository.findByUsernameAndPassword(username, password);
	}*/
	
	//p288
	public UserEntity getByCredentials(final String username, final String password, final PasswordEncoder encoder) {
		final UserEntity originalUser = userRepository.findByUsername(username);
		
		// matches 메서드를 이용해 패스워드가 같은지 확인
		if(originalUser != null && encoder.matches(password, originalUser.getPassword())){ //기존에 문자열 password그대로 넘기면 Encoded password does not look like BCrypt 에러발생
			return originalUser;
		}
		
		/*if(originalUser != null && password.equals(originalUser.getPassword())){ //테스트(추후삭제)
			return originalUser; 
		}*/
		return null;
			
	}
	
	
}

