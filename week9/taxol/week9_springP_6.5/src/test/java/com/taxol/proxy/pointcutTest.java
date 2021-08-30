package com.taxol.proxy;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.lang.annotation.Target;

import org.junit.Test;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.context.annotation.Bean;

public class pointcutTest {
	@Test
    public void methodSignaturePointcut() throws SecurityException, NoSuchMethodException {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression("execution(public int com.taxol.proxy.pointcut.Target.*(int,int) " +
                "throws java.lang.RuntimeException)"); // Target 클래스 minus() 메소드 시그니처

        // Target.minus() 메소드 시그니처가 맞는지 확인하기 위해서는 클래스 필터와 메소드 매처를 각각 비교해 보면 된다.
        // 해당 Target 클래스의 minus() 메소드는 시그니처의 부합하기 때문에 true이다.
        assertThat(pointcut.getClassFilter().matches(Target.class) && pointcut.getMethodMatcher().matches(
                      Target.class.getMethod("minus", int.class, int.class), null), is(true));

        // Target.plus() 메소드 시그니처가 맞는지 확인하기 위해서는 클래스 필터와 메소드 매처를 각각 비교해 보면 된다.
        // 해당 Target 클래스의 plus() 메소드는 minus()메소드가 아니므로 false 이다.
        assertThat(pointcut.getClassFilter().matches(Target.class) && pointcut.getMethodMatcher().matches(
                     Target.class.getMethod("plus", int.class, int.class),null), is(false));

        // Bean.method() 메소드 시그니처가 맞는지 확인하기 위해서는 클래스 필터와 메소드 매처를 각각 비교해 보면 된다.
        // Target 클래스가 아닌 Bean 클래스이므로 false 이다.
        assertThat(pointcut.getClassFilter().matches(Bean.class) && pointcut.getMethodMatcher().matches(
                    Target.class.getMethod("method"), null), is(false));

    }
}
