package com.hjz.rabbit.common;

import java.io.Serializable;

public class MqMessage implements Serializable {
	private static final long serialVersionUID = -6791105187137215924L;
	
	private String id;
	private String type;
	//保留参数
	private Object params;
	private Object body;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Object getBody() {
		return body;
	}
	public void setBody(Object body) {
		this.body = body;
	}
	public Object getParams() {
		return params;
	}
	public void setParams(Object params) {
		this.params = params;
	}
	
}