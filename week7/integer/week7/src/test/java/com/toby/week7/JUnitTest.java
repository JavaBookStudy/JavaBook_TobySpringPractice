package com.toby.week7;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class JUnitTest {
    static Set<JUnitTest> testObjects = new HashSet<>();

    /**
     * object 중복 여부 확인하는 코드
     */
    @Test
    public void test1(){
        assertThat(testObjects, is(not(hasItem(this))));
        testObjects.add(this);
    }

    @Test
    public void test2(){
        assertThat(testObjects, is(not(hasItem(this))));
        testObjects.add(this);
    }

    @Test
    public void test3(){
        assertThat(testObjects, is(not(hasItem(this))));
        testObjects.add(this);
    }
}
