package com.hjz.rabbit.test;

import org.junit.runner.RunWith;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.hjz.rabbit.consumer.Consumer;
import com.hjz.rabbit.utils.ContextUtils;


@RunWith(value = SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:applicationContext.xml", "classpath*:spring-mvc-config.xml", "classpath*:app-context-mq.xml"})
public class TestWeb{
    @Test
    public void test(){
    	Consumer consumer = ContextUtils.getBean(Consumer.class);
    	System.out.println(consumer == null ? "yes" : "no");
    }
}