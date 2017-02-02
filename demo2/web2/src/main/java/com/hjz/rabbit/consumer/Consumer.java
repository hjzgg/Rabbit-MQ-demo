package com.hjz.rabbit.consumer;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.hjz.mq.common.MqConfig;
import com.hjz.mq.common.MqMessage;
import com.hjz.mq.consumer.BaseConsumer;

@Component
public class Consumer extends BaseConsumer {
	
	@Autowired
    private ServletContext application;
	
//    private ServletContext application = ContextLoader.getCurrentWebApplicationContext().getServletContext();
	
	@SuppressWarnings("unchecked")
	@Override
	protected void doConsumeMsg(MqMessage mqMessage) {
		if(application.getAttribute("MQ_MESSAGE") == null) {
			application.setAttribute("MQ_MESSAGE", new ArrayList<Object>());
		}
		((List<Object>)application.getAttribute("MQ_MESSAGE")).add(mqMessage.getBody());
	}

	@Override
	protected String[] getQueueNames() {
		return new String[] {MqConfig.MQ_QUEUE_ADD, MqConfig.MQ_QUEUE_SAVE, MqConfig.MQ_QUEUE_UPDATE};
	}
}
