package com.example.todo.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.todo.dto.ResponseDTO;
import com.example.todo.dto.TestRequestBodyDTO;

@RestController //@Controller + @ResponseBody
@RequestMapping("test") //리소스
public class TestController {

//	@GetMapping("test/testGetMapping")
//	@GetMapping("/test/testGetMapping")
	@GetMapping("/testGetMapping")
	public String testController() {
		return "Hello World testGetMapping";
	}
	
	@GetMapping("/{id}") //http://localhost:8081/test/123
	public String testControllerWithPathVariable(@PathVariable(required = false) int id){
		return "Hello World ID" + id;
	}
	
	@GetMapping("/testRequestParam") //http://localhost:8081/test/testRequestParam?id=123
	public String testControllerRequestParam(@RequestParam(required = false) int id) {
		return "Hello World ID"+id;
	}
	
	@GetMapping("testRequestBody")
	public String testControllerRequestBody(@RequestBody TestRequestBodyDTO testRequestBodyDTO) {
		return "Hello World ID"+testRequestBodyDTO.getId()+" Message : "+ testRequestBodyDTO.getMessage();
	}
	
	@GetMapping("/testResponseBody")
	public ResponseDTO<String> testContorllerResponseBody(){
		List<String> list = new ArrayList<>();
		list.add("Hello World! I'm ResponseDTO");
		list.add("bye");
		ResponseDTO<String> response = ResponseDTO.<String>builder().data(list).build();
		return response;
	}
	
	@GetMapping("/testResponseEntity")
	public ResponseEntity<?> testControllerResponseEntity(){
		List<String> list = new ArrayList<>();
		list.add("Hello World I'm ResponseEntity. And you got 400!");
		ResponseDTO<String> response = ResponseDTO.<String>builder().data(list).build();
		//http status를 400으로 설정
		return ResponseEntity.badRequest().body(response);
//		return ResponseEntity.ok().body(response);
	}
	
}
