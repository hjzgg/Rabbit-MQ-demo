package com.hjz.mq.sender.impl;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import com.hjz.mq.common.MqMessage;
import com.hjz.mq.sender.MsgSender;

public class MsgSenderImpl implements MsgSender {
	private Logger logger = LoggerFactory.getLogger(MsgSenderImpl.class);
	@Resource(name="myRabbitAdmin")
	private RabbitAdmin rabbitAdmin;
	@Resource(name="myRabbitTemplate")
	private RabbitTemplate rabbitTemplate;
	private static final String defaultRouteKey = "";
	private static final String utf8Encode = "UTF-8";
	
	private static Set<String> queueHashSet=new HashSet<String>();
	
	/**
	 * appcode：exchange
	 * businessCode：routekey
	 */
	@Override
	public void sendMessage(MqMessage message, String queueName) {
		if(message==null){
			logger.warn("消息发送失败：消息为null");
			return;
		}
		if(!queueHashSet.contains(queueName)){
			Queue queue = new Queue(queueName,true,false,false);
			rabbitAdmin.declareQueue(queue);
			//添加到集合中用于判断
			queueHashSet.add(queueName);
		}
		if(message.getId()==null){
			message.setId(UUID.randomUUID().toString());			
		}
		rabbitTemplate.convertAndSend(queueName, message);
	}
}
