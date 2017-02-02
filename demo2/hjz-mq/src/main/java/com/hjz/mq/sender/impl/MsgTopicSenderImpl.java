package com.hjz.mq.sender.impl;

import javax.annotation.Resource;

import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import com.hjz.mq.common.MqMessage;
import com.hjz.mq.sender.MsgTopicSender;
@Component
public class MsgTopicSenderImpl implements MsgTopicSender{

	
	@Resource(name="myRabbitAdmin")
	private RabbitAdmin rabbitAdmin;
	@Resource(name="myRabbitTemplate")
	private RabbitTemplate rabbitTemplate;
	private static final String EXCHANGE_NAME = "topicExchange";
    
    @Override
	public void sendMessage(MqMessage message, String routingKey) {
        // 发送消息
        rabbitTemplate.convertAndSend(EXCHANGE_NAME,routingKey,message);
        System.out.println("发送topic消息routingKey="+routingKey+"内容为："+message.getBody());
	}

}
