package com.cfo.stock.web.rest.result;

public class ManagerAccountResult {
	private String userId;  //用户Id	 
	private String accUserId;//好友Id 
	private	long accountId; //账户Id		 
	private	Integer  status;//授权状态
	private Integer type;//类型	 
	private	Integer sort;//排序字段	 
	private boolean  def; //是否默认 	 
	private String	ctime; //添加时间	 
	private String	utime;  //更新时间	 
	private boolean valid; // 是否有效	
	
	private String brokerName;
	private String branchName;
	private String friendName;
	
	 	 	 	 	 
	private String	realName; //真实姓名	 
	private	String  idType ;//证件类型	 
	private String	idNo;  //证件号码	 
	private String  accountStatus; //账户状态  	 
	private	String  brokerId  ;//券商Id	 
	private String	brokerLogo;//券商LOGO	 
	private String	createTime ;//创建时间    
	private String  openTime;//开户成功时间  	 
	private String  tradeableTime  ;//可交易时间    	 
	private String	fundId  ;//资金账号	 
	private int  accountType  ;//开户/转户	 
	private String	szStockAccount;// 深市股东账号	 
	private String	shStockAccount ;//沪市股东账号	 
	private String  completeTime  ;//开户完成时间	 
	private String	lastFailTime ;//最后失败时间	 
	private String	szStackAccountStatus ;//深市账号状态	 
	private String	shStackAccountStatus ;//沪市账号状态
	private String bindButtonFlag;//立即关联按钮是否置灰标识
	
	private String open;
	private String transfer="-1";
	private String tradeWay;
	private String ZQTProtocol="";
	private String ZXZQProtocol="";
	
	public String getZQTProtocol() {
		return ZQTProtocol;
	}
	public void setZQTProtocol(String zQTProtocol) {
		ZQTProtocol = zQTProtocol;
	}
	public String getZXZQProtocol() {
		return ZXZQProtocol;
	}
	public void setZXZQProtocol(String zXZQProtocol) {
		ZXZQProtocol = zXZQProtocol;
	}
	public String getTradeWay() {
		return tradeWay;
	}
	public void setTradeWay(String tradeWay) {
		this.tradeWay = tradeWay;
	}
	public String getOpen() {
		return open;
	}
	public void setOpen(String open) {
		this.open = open;
	}
	public String getTransfer() {
		return transfer;
	}
	public void setTransfer(String transfer) {
		this.transfer = transfer;
	}
	public String getBranchName() {
		return branchName;
	}
	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}
	public String getFriendName() {
		return friendName;
	}
	public void setFriendName(String friendName) {
		this.friendName = friendName;
	}
	public String getBrokerName() {
		return brokerName;
	}
	public void setBrokerName(String brokerName) {
		this.brokerName = brokerName;
	}
	public String getTradeableTime() {
		return tradeableTime;
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
	public boolean isValid() {
		return valid;
	}
	public void setValid(boolean valid) {
		this.valid = valid;
	}
	public String getRealName() {
		return realName;
	}
	public void setRealName(String realName) {
		this.realName = realName;
	}
	public String getIdType() {
		return idType;
	}
	public void setIdType(String idType) {
		this.idType = idType;
	}
	public String getIdNo() {
		return idNo;
	}
	public void setIdNo(String idNo) {
		this.idNo = idNo;
	}
	public String getAccountStatus() {
		return accountStatus;
	}
	public void setAccountStatus(String accountStatus) {
		this.accountStatus = accountStatus;
	}
	public String getBrokerId() {
		return brokerId;
	}
	public void setBrokerId(String brokerId) {
		this.brokerId = brokerId;
	}
	public String getBrokerLogo() {
		return brokerLogo;
	}
	public void setBrokerLogo(String brokerLogo) {
		this.brokerLogo = brokerLogo;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getOpenTime() {
		return openTime;
	}
	public void setOpenTime(String openTime) {
		this.openTime = openTime;
	}
	public String isTradeableTime() {
		return tradeableTime;
	}
	public void setTradeableTime(String tradeableTime) {
		this.tradeableTime = tradeableTime;
	}
	public String getFundId() {
		return fundId;
	}
	public void setFundId(String fundId) {
		this.fundId = fundId;
	}
	public int getAccountType() {
		return accountType;
	}
	public void setAccountType(int accountType) {
		this.accountType = accountType;
	}
	public String getSzStockAccount() {
		return szStockAccount;
	}
	public void setSzStockAccount(String szStockAccount) {
		this.szStockAccount = szStockAccount;
	}
	public String getShStockAccount() {
		return shStockAccount;
	}
	public void setShStockAccount(String shStockAccount) {
		this.shStockAccount = shStockAccount;
	}
	public String getCompleteTime() {
		return completeTime;
	}
	public void setCompleteTime(String completeTime) {
		this.completeTime = completeTime;
	}
	public String getLastFailTime() {
		return lastFailTime;
	}
	public void setLastFailTime(String lastFailTime) {
		this.lastFailTime = lastFailTime;
	}
	public String getSzStackAccountStatus() {
		return szStackAccountStatus;
	}
	public void setSzStackAccountStatus(String szStackAccountStatus) {
		this.szStackAccountStatus = szStackAccountStatus;
	}
	public String getShStackAccountStatus() {
		return shStackAccountStatus;
	}
	public void setShStackAccountStatus(String shStackAccountStatus) {
		this.shStackAccountStatus = shStackAccountStatus;
	}
	public String getBindButtonFlag() {
		return bindButtonFlag;
	}
	public void setBindButtonFlag(String bindButtonFlag) {
		this.bindButtonFlag = bindButtonFlag;
	}	

}
