package com.cfo.stock.web.rest.vo;

import java.math.BigDecimal;

import com.jrj.stocktrade.api.common.EntrustBs;
import com.jrj.stocktrade.api.common.EntrustProp;
import com.jrj.stocktrade.api.common.EntrustStatus;
import com.jrj.stocktrade.api.common.EntrustType;
import com.jrj.stocktrade.api.common.ExchangeType;

/**
 * 委托
 * @history  
 * <PRE>  
 * ---------------------------------------------------------  
 * VERSION       DATE            BY       CHANGE/COMMENT  
 * ---------------------------------------------------------  
 * v1.0         2014-4-17    		iriyadays     create  
 * ---------------------------------------------------------  
 * </PRE> 
 *
 */
public class EntrustVo {
	private long initDate; // 发生日期
	private long batchNo; // 委托批号
	private long entrustNo; // 委托编号
	private ExchangeType exchangeType; // 交易类别
	private String stockCode; // 证券代码
	private EntrustBs entrustBs; // 买卖方向
	private BigDecimal entrustPrice; // 委托价格
	private BigDecimal entrustAmount; // 委托数量
	private BigDecimal businessAmount; // 成交数量
	private BigDecimal businessPrice; // 成交价格
	private long reportNo; // 申请编号
	private long reportTime; // 申报时间
	private EntrustType entrustType; // 委托类别
	private EntrustStatus entrustStatus; // 委托状态
	private long entrustTime; // 委托时间
	private long entrustDate; // 委托日期
	private EntrustProp entrustProp; // 委托属性
	private String stockName; // 证券名称
	private String cancelInfo; // 废单原因
	private BigDecimal withdrawAmount; // 撤单数量
	private String positionStr; // 定位串
	//~---------------
	/**
	 * 返回发生日期
	 * @return
	 */
	public long getInitDate() {
		return initDate;
	}
	/**
	 * 返回委托批号
	 * @return
	 */
	public long getBatchNo() {
		return batchNo;
	}
	/**
	 * 返回委托编号
	 * @return
	 */
	public long getEntrustNo() {
		return entrustNo;
	}
	/**
	 * 返回交易类别
	 * @return
	 */
	public ExchangeType getExchangeType() {
		return exchangeType;
	}
	/**
	 * 返回证券代码
	 * @return
	 */
	public String getStockCode() {
		return stockCode;
	}
	/**
	 * 返回委托方向
	 * @return
	 */
	public EntrustBs getEntrustBs() {
		return entrustBs;
	}
	/**
	 * 返回委托价格
	 * @return
	 */
	public BigDecimal getEntrustPrice() {
		return entrustPrice;
	}
	/**
	 * 返回委托数量
	 * @return
	 */
	public BigDecimal getEntrustAmount() {
		return entrustAmount;
	}
	/**
	 * 返回成交数量
	 * @return
	 */
	public BigDecimal getBusinessAmount() {
		return businessAmount;
	}
	/**
	 * 返回成交价格
	 * @return
	 */
	public BigDecimal getBusinessPrice() {
		return businessPrice;
	}
	/**
	 * 返回申报编号
	 * @return
	 */
	public long getReportNo() {
		return reportNo;
	}
	/**
	 * 返回申报时间
	 * @return
	 */
	public long getReportTime() {
		return reportTime;
	}
	/**
	 * 返回委托类别
	 * @return
	 */
	public EntrustType getEntrustType() {
		return entrustType;
	}
	/**
	 * 返回委托状态
	 * @return
	 */
	public EntrustStatus getEntrustStatus() {
		return entrustStatus;
	}
	/**
	 * 返回委托时间
	 * @return
	 */
	public long getEntrustTime() {
		return entrustTime;
	}
	/**
	 * 返回委托日期
	 * @return
	 */
	public long getEntrustDate() {
		return entrustDate;
	}
	/**
	 * 返回委托属性
	 * @return
	 */
	public EntrustProp getEntrustProp() {
		return entrustProp;
	}
	/**
	 * 返回证券名称
	 * @return
	 */
	public String getStockName() {
		return stockName;
	}
	/**
	 * 返回废单原因
	 * @return
	 */
	public String getCancelInfo() {
		return cancelInfo;
	}
	/**
	 * 返回撤单数量
	 * @return
	 */
	public BigDecimal getWithdrawAmount() {
		return withdrawAmount;
	}
	/**
	 * 返回定位串
	 * @return
	 */
	public String getPositionStr() {
		return positionStr;
	}
	//~------------
	public void setInitDate(long initDate) {
		this.initDate = initDate;
	}
	public void setBatchNo(long batchNo) {
		this.batchNo = batchNo;
	}
	public void setEntrustNo(long entrustNo) {
		this.entrustNo = entrustNo;
	}
	public void setExchangeType(ExchangeType exchangeType) {
		this.exchangeType = exchangeType;
	}
	public void setStockCode(String stockCode) {
		this.stockCode = stockCode;
	}
	public void setEntrustBs(EntrustBs entrustBs) {
		this.entrustBs = entrustBs;
	}
	public void setEntrustPrice(BigDecimal entrustPrice) {
		this.entrustPrice = entrustPrice;
	}
	public void setEntrustAmount(BigDecimal entrustAmount) {
		this.entrustAmount = entrustAmount;
	}
	public void setBusinessAmount(BigDecimal businessAmount) {
		this.businessAmount = businessAmount;
	}
	public void setBusinessPrice(BigDecimal businessPrice) {
		this.businessPrice = businessPrice;
	}
	public void setReportNo(long reportNo) {
		this.reportNo = reportNo;
	}
	public void setReportTime(long reportTime) {
		this.reportTime = reportTime;
	}
	public void setEntrustType(EntrustType entrustType) {
		this.entrustType = entrustType;
	}
	public void setEntrustStatus(EntrustStatus entrustStatus) {
		this.entrustStatus = entrustStatus;
	}
	public void setEntrustTime(long entrustTime) {
		this.entrustTime = entrustTime;
	}
	public void setEntrustDate(long entrustDate) {
		this.entrustDate = entrustDate;
	}
	public void setEntrustProp(EntrustProp entrustProp) {
		this.entrustProp = entrustProp;
	}
	public void setStockName(String stockName) {
		this.stockName = stockName;
	}
	public void setCancelInfo(String cancelInfo) {
		this.cancelInfo = cancelInfo;
	}
	public void setWithdrawAmount(BigDecimal withdrawAmount) {
		this.withdrawAmount = withdrawAmount;
	}
	public void setPositionStr(String positionStr) {
		this.positionStr = positionStr;
	}
}
