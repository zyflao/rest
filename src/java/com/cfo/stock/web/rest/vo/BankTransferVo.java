package com.cfo.stock.web.rest.vo;

import java.math.BigDecimal;

import com.jrj.stocktrade.api.common.AssetProp;
import com.jrj.stocktrade.api.common.BusinessType;
import com.jrj.stocktrade.api.common.MoneyType;
import com.jrj.stocktrade.api.common.SourceFlag;

public class BankTransferVo{
	
	private String branchNo; // 分支机构
	private String bankNo; // 银行代码
	private String bankName;//银行名称
	private String transName; // 转换机名字
	private long entrustNo;//委托编号
	private BusinessType businessType;//业务类型
	private SourceFlag sourceFlag; // 发起方
	private MoneyType moneyType; // 币种类别
	//private String clientAccount;//存管自己账号 <-- 没有该字段
	//private String bankAccount;//存管账号 <-- 没有该字段
	private BigDecimal occurBalance; // 发生金额
	//private BigDecimal clearBalance; // 清算金额 <-- 没有该字段
	private long entrustTime;
//	private TransferStatus entrustStatus;
	private int errorNo; // 错误代码
	private String cancelInfo; // 废单原因
	private String bankErrorInfo; // 银行错误原因
	private String positionStr; // 定位串
	private String remark;
	private AssetProp assetProp;
	
	private BigDecimal postBalance; // <-- 多余字段
	//private String clientId; // <-- 多余字段
	private long initDate;
	
	private EntrustStatusVo entrustStatus;

		
	public String getBranchNo() {
		return branchNo;
	}
	public void setBranchNo(String branchNo) {
		this.branchNo = branchNo;
	}
	public String getBankNo() {
		return bankNo;
	}
	public void setBankNo(String bankNo) {
		this.bankNo = bankNo;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String getTransName() {
		return transName;
	}
	public void setTransName(String transName) {
		this.transName = transName;
	}
	public long getEntrustNo() {
		return entrustNo;
	}
	public void setEntrustNo(long entrustNo) {
		this.entrustNo = entrustNo;
	}
	public BusinessType getBusinessType() {
		return businessType;
	}
	public void setBusinessType(BusinessType businessType) {
		this.businessType = businessType;
	}
	public SourceFlag getSourceFlag() {
		return sourceFlag;
	}
	public void setSourceFlag(SourceFlag sourceFlag) {
		this.sourceFlag = sourceFlag;
	}
	public MoneyType getMoneyType() {
		return moneyType;
	}
	public void setMoneyType(MoneyType moneyType) {
		this.moneyType = moneyType;
	}
	public BigDecimal getOccurBalance() {
		return occurBalance;
	}
	public void setOccurBalance(BigDecimal occurBalance) {
		this.occurBalance = occurBalance;
	}
	public long getEntrustTime() {
		return entrustTime;
	}
	public void setEntrustTime(long entrustTime) {
		this.entrustTime = entrustTime;
	}
	public int getErrorNo() {
		return errorNo;
	}
	public void setErrorNo(int errorNo) {
		this.errorNo = errorNo;
	}
	public String getCancelInfo() {
		return cancelInfo;
	}
	public void setCancelInfo(String cancelInfo) {
		this.cancelInfo = cancelInfo;
	}
	public String getBankErrorInfo() {
		return bankErrorInfo;
	}
	public void setBankErrorInfo(String bankErrorInfo) {
		this.bankErrorInfo = bankErrorInfo;
	}
	public String getPositionStr() {
		return positionStr;
	}
	public void setPositionStr(String positionStr) {
		this.positionStr = positionStr;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public AssetProp getAssetProp() {
		return assetProp;
	}
	public void setAssetProp(AssetProp assetProp) {
		this.assetProp = assetProp;
	}
	public BigDecimal getPostBalance() {
		return postBalance;
	}
	public void setPostBalance(BigDecimal postBalance) {
		this.postBalance = postBalance;
	}
	public long getInitDate() {
		return initDate;
	}
	public void setInitDate(long initDate) {
		this.initDate = initDate;
	}
	public EntrustStatusVo getEntrustStatus() {
		return entrustStatus;
	}
	public void setEntrustStatus(EntrustStatusVo entrustStatus) {
		this.entrustStatus = entrustStatus;
	}		
}
