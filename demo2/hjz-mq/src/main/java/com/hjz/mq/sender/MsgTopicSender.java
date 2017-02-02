package com.hjz.mq.sender;

import com.hjz.mq.common.MqMessage;

public interface MsgTopicSender {

	/**
	 * 
	 * @param message
	 * @param routingKey
	 */
	void sendMessage(MqMessage message, String routingKey);
}
