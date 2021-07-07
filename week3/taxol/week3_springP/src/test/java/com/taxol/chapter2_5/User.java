package com.taxol.chapter2_5;

/*
 * List 2-10
 * JUnit Test를 위해 파라미터가 있는 생성자를 추가
*/
public class User {
	String id;
	String name;
	String password;
	
	public User(String id, String name, String password) {
		super();
		this.id = id;
		this.name = name;
		this.password = password;
	}
	
	public User() {	}	// 자바빈의 규약으로, 생성자를 명시적으로 추가했을 때는 파라미터가 없는 디폴트 생성자도 함께 정의하자

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
}