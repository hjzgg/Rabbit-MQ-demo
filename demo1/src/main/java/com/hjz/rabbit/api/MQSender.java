package com.hjz.rabbit.api;

import com.hjz.rabbit.common.MqMessage;

public interface MQSender {
    /**
     * 发送消息到指定队列
     * @param message
     * @param queueName
     */
	void sendMessage(MqMessage message, String queueName);
}