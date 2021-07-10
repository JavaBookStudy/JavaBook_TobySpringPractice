package com.taxol.chapter3_2;

import static org.hamcrest.CoreMatchers.either;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


/*
 * list 2-25
 * 모든 오브젝트가 중복이 되지 않는 다는 것을 확인
 * 
 * list 2-27 
 * 스프링 테스트 컨텍스트는 테스트 개수에 상관없이 한 개만 만 만들어지는지 테스트
*/
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="/junit.xml")
public class JUnitTest {
	
	@Autowired
	ApplicationContext context;
	
	//static JUnitTest testObject;
	static Set<JUnitTest> testObjects = new HashSet<>();
	static ApplicationContext contextObject = null;
	
	@Test
	public void test1() {
		//assertThat(this, is(not(sameInstance(testObject))));
		assertThat(testObjects, not(hasItem(this)));
		testObjects.add(this);
		
		assertThat(contextObject == null || contextObject == this.context , is(true));
		contextObject = this.context;
	}
	
	@Test
	public void test2() {
		assertThat(testObjects, not(hasItem(this)));
		testObjects.add(this);
		
		assertTrue(contextObject == null || contextObject == this.context);
		contextObject = this.context;
	}
	
	@Test
	public void test3() {
		assertThat(testObjects, not(hasItem(this)));
		testObjects.add(this);
		
		assertThat(contextObject, either(is(nullValue())).or(is(this.context)));
		contextObject = this.context;
	}
}
