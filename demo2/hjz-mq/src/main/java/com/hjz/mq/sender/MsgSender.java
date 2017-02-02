package com.hjz.mq.sender;

import com.hjz.mq.common.MqMessage;

public interface MsgSender {
	/**
	 * 发送消息
	 * @param message 消息内容
	 * @param appCode 应用编码
	 * @param bussinessCode 业务编码
	 */
	void sendMessage(MqMessage message, String queueName);
}
