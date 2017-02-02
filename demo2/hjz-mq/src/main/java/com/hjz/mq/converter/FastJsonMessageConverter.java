package com.hjz.mq.converter;

import java.io.UnsupportedEncodingException;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.converter.AbstractMessageConverter;
import org.springframework.amqp.support.converter.MessageConversionException;

import com.alibaba.fastjson.JSON;
import com.hjz.mq.common.MqConfig;

public class FastJsonMessageConverter extends AbstractMessageConverter {

	// private static final Logger LOGGER =
	// LoggerFactory.getLogger(FastJsonMessageConverter.class);
	public static final String DEFAULT_CHARSET = "UTF-8";
	private volatile String defaultCharset = "UTF-8";

	public FastJsonMessageConverter() {
		super();
	}

	public void setDefaultCharset(String defaultCharset) {
		this.defaultCharset = (defaultCharset != null ? defaultCharset : "UTF-8");
	}

	@Override
	protected Message createMessage(Object objectToConvert, MessageProperties messageProperties) {
		byte[] bytes = null;
		try {
			String jsonString = JSON.toJSONString(objectToConvert);

			bytes = jsonString.getBytes(this.defaultCharset);
		} catch (UnsupportedEncodingException e) {
			throw new MessageConversionException("Failed to convert Message content", e);
		}
		messageProperties.setContentType(MessageProperties.CONTENT_TYPE_JSON);
		messageProperties.setContentEncoding(this.defaultCharset);
		if (bytes != null) {
			messageProperties.setContentLength(bytes.length);
		}
		return new Message(bytes, messageProperties);
	}

	@Override
	public Object fromMessage(Message message) throws MessageConversionException {
		String json = "";
		try {
			json = new String(message.getBody(), defaultCharset);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}		
		return JSON.parse(json);
	}
	
	public static void main(String[] args){
		MqConfig config = new MqConfig();
		config.setHost("localhost");
		
		System.out.println(JSON.toJSONString(config)); 
	}
}
