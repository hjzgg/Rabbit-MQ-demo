package com.hjz.rabbit.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.hjz.rabbit.api.MQSender;
import com.hjz.rabbit.common.MqMessage;

@RunWith(value = SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:applicationContext.xml", "classpath*:applicationContext-mq.xml"})
public class TestQueue{
    @Autowired
    private MQSender mqSender;

    final String queueName = "test_queue_routing_key";

    @Test
    public void send(){
    	MqMessage message = new MqMessage();
    	message.setBody("大家好，我是hjzgg!!!");
    	mqSender.sendMessage(message, queueName);
    }
}