package com.hjz.rabbit.web;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.hjz.mq.util.CloneUtils;
import com.hjz.mq.util.ReturnCode;

@Controller
@RequestMapping("consumer")
public class MQConsumer {
	
	@Autowired
    private ServletContext application;
	
	@SuppressWarnings("unchecked")
	@RequestMapping("process")
	@ResponseBody
	public JSONObject consumer() {
		JSONObject ans = new JSONObject();
		try {
			if(application.getAttribute("MQ_MESSAGE") == null) {
				application.setAttribute("MQ_MESSAGE", new ArrayList<Object>());
			}
			List<Object> data = (List<Object>) application.getAttribute("MQ_MESSAGE");
			ans.put("code", ReturnCode.SUCCESS.getValue());
			ans.put("data", CloneUtils.clone(data));
			data.clear();
		} catch(Exception e) {
			e.printStackTrace();
			ans.put("code", ReturnCode.FAILURE.getValue());
			ans.put("msg", e.getMessage().matches(ReturnCode.REGEX_CHINESE.getValue()) ? e.getMessage() : "内部异常");
		}
		return ans;
	}
}
