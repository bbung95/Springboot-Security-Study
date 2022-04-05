package com.cos.jwt.config;

import com.cos.jwt.filter.MyFilter1;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CorsFilter corsFilter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
//////////// jwt 기본 설정
//        http.csrf().disable();
//        http.sessionManagement()
//                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // session 방식 사용안함 //
//            .and()
//                .formLogin().disable() // form login 사용안함
//                .httpBasic().disable() //
//                .authorizeRequests()
//                .antMatchers("/api/v1/user/**")
//                .access("hasRole('ROLE_USER') or hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
//                .antMatchers("/api/v1/manager/**")
//                .access("hasRole(hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
//                .antMatchers("/api/v1/admin/**")
//                .access("hasRole('ROLE_ADMIN')")
//                .anyRequest().permitAll();
//////////////

        //http.addFilterBefore(new MyFilter1(), BasicAuthenticationFilter.class);
        //security에 filter등록, BasicAuthenticationFilter 전에 실행 (Spring의 필터종류를 알아야함)
        //security의 필터는 Myfilter보다 무조건적으로 우선실행된다.

        http.csrf().disable();
        http.sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // session 방식 사용안함
                .and()
                .addFilter(corsFilter) // 모든 요청은 filter를 거치게된다. Controller에 @CrossOrigin(인증X), 시큐리티 핉터에 등록 인증(O)
                .formLogin().disable() // form login 사용안함
                .httpBasic().disable() //
                .authorizeRequests()
                .antMatchers("/api/v1/user/**")
                .access("hasRole('ROLE_USER') or hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
                .antMatchers("/api/v1/manager/**")
                .access("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
                .antMatchers("/api/v1/admin/**")
                .access("hasRole('ROLE_ADMIN')")
                .anyRequest().permitAll();
    }
}
