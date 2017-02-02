package com.hjz.rabbit.web;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.hjz.mq.common.MqConfig;
import com.hjz.mq.common.MqMessage;
import com.hjz.mq.sender.MsgSender;
import com.hjz.mq.util.ReturnCode;

@Controller
@RequestMapping("producer")
public class MQProducer {
	
	@Autowired
	private MsgSender mqSender;
	
	@RequestMapping("process")
	@ResponseBody
	public JSONObject producer() {
		JSONObject ans = new JSONObject();
		try {
			String msg = "随机消息 " + UUID.randomUUID().toString();
			MqMessage message = new MqMessage();
			message.setBody(msg);
			mqSender.sendMessage(message, MqConfig.MQ_QUEUE_ADD);
			ans.put("code", ReturnCode.SUCCESS.getValue());
			ans.put("data", msg);
		} catch(Exception e) {
			ans.put("code", ReturnCode.FAILURE.getValue());
			ans.put("msg", e.getMessage().matches(ReturnCode.REGEX_CHINESE.getValue()) ? e.getMessage() : "內部錯誤");
		}
		return ans;
	}
}
