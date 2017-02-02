package com.hjz.mq.consumer;

import javax.annotation.Resource;

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import com.alibaba.fastjson.JSON;
import com.hjz.mq.common.MqMessage;
import com.rabbitmq.client.Channel;

public abstract class BaseConsumer implements  InitializingBean,ChannelAwareMessageListener,DisposableBean {
	protected static Logger logger = LoggerFactory.getLogger(BaseConsumer.class); 
	@Resource(name="myRabbitAdmin")
	private RabbitAdmin rabbitAdmin;
	@Resource(name="myRabbitTemplate")
	private RabbitTemplate rabbitTemplate;
	//@Resource(name="myListenerContainer")
	private SimpleMessageListenerContainer listenerContainer;
	
	private static final String EXCHANGE_NAME = "directExchange";
	
	public static final String DEFAULT_CHARSET = "UTF-8";
	
	public void afterPropertiesSet(){
		String[] queueNames = this.getQueueNames();
		if(ArrayUtils.isEmpty(queueNames)){
	    	//throw new RuntimeException("请设置需要消费的队列");
			logger.warn("没有设置需要消费的队列");
			return;
	    }
		// 声明交换器
		/*DirectExchange exchange = new DirectExchange(EXCHANGE_NAME);
		rabbitAdmin.declareExchange(exchange);

		Binding binding = BindingBuilder.bind(queue).to(exchange).with(getRoutingKey());
		rabbitAdmin.declareBinding(binding);*/
		//声明一个监听容器
		listenerContainer = new SimpleMessageListenerContainer();
		listenerContainer.setConnectionFactory(rabbitTemplate.getConnectionFactory());
		Queue[] queues = new Queue[queueNames.length];
		//注册监听
		for(int i=0,len=queueNames.length;i<len;i++){
			String queueName = queueNames[i];
			Queue queue = new Queue(queueName,true,false,false);
			rabbitAdmin.declareQueue(queue);
			queues[i] = queue;
		}
		listenerContainer.addQueues(queues);
		listenerContainer.setPrefetchCount(1);
		listenerContainer.setAcknowledgeMode(AcknowledgeMode.MANUAL);
		listenerContainer.setMessageListener(this);
		listenerContainer.start();
	}
	
	public void destroy(){
		logger.debug("关闭监听...");
		if(listenerContainer!=null){
			listenerContainer.stop();
		}
	}
	
	@Override
	public void onMessage(Message message, Channel channel) throws Exception {
		String msg = new String(message.getBody(), DEFAULT_CHARSET);
		logger.debug("接收到消息："+msg);
		MessageProperties msgProps = message.getMessageProperties();
		//确认接收到消息
		channel.basicAck(msgProps.getDeliveryTag(), false);
		try{
			MqMessage mqMessage = JSON.parseObject(msg, MqMessage.class);
			if(mqMessage==null || mqMessage.getBody() == null){
				logger.error("消息体为空，舍弃！");
				return;
			}
			doConsumeMsg(mqMessage);
			logger.debug("消息消费完成");
		}catch(Exception ex){
			logger.error("消息消费失败:",ex);
		}
	}
	protected abstract void doConsumeMsg(MqMessage mqMessage);
	
	protected abstract String[] getQueueNames();
	
//	protected abstract String[] getRoutingKey();
}
