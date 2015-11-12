package com.cfo.stock.web.rest.vo;

public class JRJLoginVo {
	private String userName;
	private String UID; //金融界ID
	private String token;
	private String userId; //证券通ID
	
	
	public JRJLoginVo(String userName, String uID) {
		super();
		this.userName = userName;
		UID = uID;
	}
	
	public JRJLoginVo(String userName, String uID,String userId) {
		this.userName = userName;
		this.UID = uID;
		this.userId = userId;
	}
	
	public String getUserId() {
		return userId;
	}


	public void setUserId(String userId) {
		this.userId = userId;
	}


	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUID() {
		return UID;
	}
	public void setUID(String uID) {
		UID = uID;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	
	
	
}
