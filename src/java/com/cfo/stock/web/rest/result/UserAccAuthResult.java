package com.cfo.stock.web.rest.result;

public class UserAccAuthResult{
	private String ctime;
	private String utime;
	private String userId;
	private String accUserId;
	private long accountId;
	private Integer status;
	private Integer type;
	private Integer sort;
	private boolean def;
	private boolean valid;
	private String brokerId;
	private String brokerName;
	private String relationUserId;//关系用户ID
	private String brokerLogo;//券商LOGO	  
 	private String realName;//真实姓名	 
 	private String fundId;//资金账号
 	private String brokerType;//券商类型  "0"=自接               "1"=恒生/ITN
 	private String tradeWay;
 	
	public String getTradeWay() {
		return tradeWay;
	}

	public void setTradeWay(String tradeWay) {
		this.tradeWay = tradeWay;
	}

	public String getBrokerType() {
		return brokerType;
	}

	public void setBrokerType(String brokerType) {
		this.brokerType = brokerType;
	}


	public String getBrokerId() {
		return brokerId;
	}


	public void setBrokerId(String brokerId) {
		this.brokerId = brokerId;
	}


	public String getRelationUserId() {
		return relationUserId;
	}


	public void setRelationUserId(String relationUserId) {
		this.relationUserId = relationUserId;
	}


	public String getBrokerLogo() {
		return brokerLogo;
	}


	public void setBrokerLogo(String brokerLogo) {
		this.brokerLogo = brokerLogo;
	}




	public String getBrokerName() {
		return brokerName;
	}




	public void setBrokerName(String brokerName) {
		this.brokerName = brokerName;
	}




	public String getRealName() {
		return realName;
	}




	public void setRealName(String realName) {
		this.realName = realName;
	}




	public String getFundId() {
		return fundId;
	}




	public void setFundId(String fundId) {
		this.fundId = fundId;
	}




	public UserAccAuthResult(String userId, String accUserId, long accountId,
			Integer status, Integer type, Integer sort) {
		super();
		this.userId = userId;
		this.accUserId = accUserId;
		this.accountId = accountId;
		this.status = status;
		this.type = type;
		this.sort = sort;
	}




	public String getUserId() {
		return userId;
	}




	public void setUserId(String userId) {
		this.userId = userId;
	}




	public String getAccUserId() {
		return accUserId;
	}




	public void setAccUserId(String accUserId) {
		this.accUserId = accUserId;
	}




	public long getAccountId() {
		return accountId;
	}




	public void setAccountId(long accountId) {
		this.accountId = accountId;
	}




	public Integer getStatus() {
		return status;
	}




	public void setStatus(Integer status) {
		this.status = status;
	}




	public Integer getType() {
		return type;
	}




	public void setType(Integer type) {
		this.type = type;
	}




	public Integer getSort() {
		return sort;
	}




	public void setSort(Integer sort) {
		this.sort = sort;
	}




	public boolean isDef() {
		return def;
	}




	public void setDef(boolean def) {
		this.def = def;
	}




	public boolean isValid() {
		return valid;
	}




	public void setValid(boolean valid) {
		this.valid = valid;
	}




	public String getCtime() {
		return ctime;
	}

	public void setCtime(String ctime) {
		this.ctime = ctime;
	}

	public String getUtime() {
		return utime;
	}

	public void setUtime(String utime) {
		this.utime = utime;
	}

	
}
