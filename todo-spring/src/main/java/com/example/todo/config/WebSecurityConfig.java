package com.example.todo.config;




import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.web.filter.CorsFilter;

import com.example.todo.security.JwtAuthenticationFilter;

import lombok.extern.slf4j.Slf4j;




@EnableWebSecurity
@Slf4j
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{

	@Autowired
	private JwtAuthenticationFilter jwtAuthenticationFilter;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		//http 시큐리티 빌더
		http.cors() // WebMvcConfig에서 이미 설정했으므로 기본 cors 설정.
			.and()
			.csrf() //csrf는 현재 사용하지 않으므로 disable
				.disable()
			.httpBasic() // token을 사용하므로 basic인증 disable
				.disable()
			.sessionManagement() // session 기반이 아님을 선언
			 .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and()
			.authorizeRequests() // /와 /auth/**경로는 인증 안 해도 됨.
				.antMatchers("/", "/auth/**").permitAll()
			.anyRequest() //
				.authenticated();
		
		// filter 등록.
		// 매 요청마다
		// CorsFilter 실행한 후에
		// jwtAuthenticationFilter 실행한다.
		http.addFilterAfter(jwtAuthenticationFilter, CorsFilter.class);
		//CorsFilter는 import org.springframework.web.filter.CorsFilter를 import한다
		
		//http.addFilterAfter(jwtAuthenticationFilter, null); 로 보냈더니 아래 에러가 발생함
		//Error creating bean with name 'springSecurityFilterChain' defined in class path resource
		
		//Security관련은 TokenProvider, JwtAuthenticationFilter, WebSecurityConfig까지 다 만들어야 작동한다
		//TokenProvider, JwtAuthenticationFilter만 만들고 동작시키면 body리턴이 없음
			
	}
	
	
}
