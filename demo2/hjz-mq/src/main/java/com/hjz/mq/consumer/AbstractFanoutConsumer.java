package com.hjz.mq.consumer;

import java.io.UnsupportedEncodingException;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Message;
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

/**
 * topic 类型消费者基类
 * 
 */
public abstract class AbstractFanoutConsumer implements InitializingBean, DisposableBean, ChannelAwareMessageListener {
	protected static Logger logger = LoggerFactory.getLogger(AbstractFanoutConsumer.class);
	@Resource(name = "myRabbitAdmin")
	private RabbitAdmin rabbitAdmin;
	@Resource(name = "myRabbitTemplate")
	private RabbitTemplate rabbitTemplate;
	//private static final String EXCHANGE_NAME = "topicExchange";
	private SimpleMessageListenerContainer container;

	@Override
	public void destroy() throws Exception {
		logger.debug("关闭监听...");
		if (container != null) {
			container.stop();
		}
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		// 声明 queue
		// Queue queue = new Queue("liwei-spring-queue");
		// rabbitAdmin.declareQueue(queue);
		Queue queue = rabbitAdmin.declareQueue();
		// 声明交换器
		FanoutExchange exchange = new FanoutExchange(getExchangeName());
		rabbitAdmin.declareExchange(exchange);

		//String routingKey = "foo.*";
		Binding binding = BindingBuilder.bind(queue).to(exchange);
		rabbitAdmin.declareBinding(binding);
		/**
		 * 设置监听和容器
		 */
		container = new SimpleMessageListenerContainer();
		container.setConnectionFactory(rabbitTemplate.getConnectionFactory());
		// container.
		// Listener listener =new Listener();
		// MessageListenerAdapter adapter = new
		// MessageListenerAdapter(listener);
		// container.setMessageListener(adapter);
		container.setMessageListener(this);
		container.setQueueNames(queue.getName());
		System.out.println("fanout 消费者绑定队列名" + queue.getName());
		container.start();

	}

	private class Listener {
		@SuppressWarnings("unused")
		public void handleMessage(byte[] bytes) throws UnsupportedEncodingException {
			String msg = new String(bytes, "UTF-8");
			System.out.println("Object 内部的方法：" + msg);
		}
	}

	@Override
	public void onMessage(Message message, Channel channel) throws Exception {
		String msg = new String(message.getBody(), "UTF-8");
		MqMessage mqMessage = JSON.parseObject(msg, MqMessage.class);
		if (mqMessage == null || mqMessage.getBody() == null) {
			logger.error("消息体为空，舍弃！");
			return;
		}
		doConsumeMsg(mqMessage);
		System.out.println("msg：" + msg);
	};

	protected abstract void doConsumeMsg(MqMessage mqMessage);

	protected abstract String getExchangeName();

}
