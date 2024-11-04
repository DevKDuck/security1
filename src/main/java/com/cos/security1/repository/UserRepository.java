package com.cos.security1.repository;

import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;

import com.cos.security1.model.User;

//CRUD 함수 를 가지는 JPARepository가 들고 있음
//@Repository 라는 어노테이션 없이도 IOC됨.JPARepository 상속했기 때문임
//IOC (제어의 역전)
//Dependency Injection (DI)
//객체 생성 및 관리에 대한 책임을 IoC 컨테이너가 가지며, 필요한 객체를 직접 생성하거나 외부에서 주입받는 방식
public interface UserRepository extends JpaRepositoryImplementation<User, Integer>{

}
