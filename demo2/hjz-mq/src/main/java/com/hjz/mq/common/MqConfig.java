package com.hjz.mq.common;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;

public class MqConfig implements Serializable {
	private static final long serialVersionUID = -1183790956010312948L;
	
	public static final String MQ_QUEUE_ADD = "MQ_QUEUE_ADD";
	public static final String MQ_QUEUE_UPDATE = "MQ_QUEUE_UPDATE";
	public static final String MQ_QUEUE_SAVE = "MQ_QUEUE_SAVE";
	
	@Value("${mq.host:127.0.0.1}")
	private String host;
	@Value("${mq.port:5672}")
	private int port;
	@Value("${mq.username:guest}")
	private String userName;
	@Value("${mq.password:guest}")
	private String password;

	private String defaultRouteKey;

	public String getUserName() {
		return userName;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getDefaultRouteKey() {
		if (StringUtils.isEmpty(defaultRouteKey)) {
			defaultRouteKey = "my-route-key";
		}
		return defaultRouteKey;
	}

	public void setDefaultRouteKey(String defaultRouteKey) {
		this.defaultRouteKey = defaultRouteKey;
	}
}
