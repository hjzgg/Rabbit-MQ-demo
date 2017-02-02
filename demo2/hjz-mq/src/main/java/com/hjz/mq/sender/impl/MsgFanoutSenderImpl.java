package com.hjz.mq.sender.impl;

import javax.annotation.Resource;

import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import com.hjz.mq.common.MqMessage;
import com.hjz.mq.sender.MsgFanoutSender;
@Component
public class MsgFanoutSenderImpl implements MsgFanoutSender {

	@Resource(name = "myRabbitAdmin")
	private RabbitAdmin rabbitAdmin;
	@Resource(name = "myRabbitTemplate")
	private RabbitTemplate rabbitTemplate;

	@Override
	public void sendMessage(MqMessage message, String exchage) {
		// 发送消息
		rabbitTemplate.convertAndSend(exchage, "", message);
		System.out.println("发送fanout消息routingKey=" + exchage + "内容为：" + message.getBody());

	}

}
