package com.hjz.mq.sender;

import com.hjz.mq.common.MqMessage;

public interface MsgFanoutSender {

	/**
	 * 
	 * @param message
	 * @param routingKey
	 */
	void sendMessage(MqMessage message, String exchage);
}
