package com.cos.security1.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cos.security1.Model.User;

// 유저 CRUD 함수를 JpaRepository가 들고있음
// @Repositoty라는 어노테이션이 없어도 loc가 된다. 이유는 JpaRepository를 상속했기 때문
public interface UserRepository extends JpaRepository<User, Integer>{
	
	// findBy 규칙 => Username 문법
	// SELECT * FROM user WHERE username = #{username};
	public User findByUsername(String username); // JPA Query Method
	
}
