package com.cfo.stock.web.rest.vo;

import java.io.Serializable;
import java.math.BigDecimal;

@SuppressWarnings("serial")
public class FundInfoVo implements Serializable {

	// 当前余额
	protected BigDecimal currentBalance;
	// 可用资金
	protected BigDecimal enableBalance;
	// 可取金额
	protected BigDecimal fetchBalance;
	// 冻结资金
	protected BigDecimal frozenBalance;
	// 解冻资金
	protected BigDecimal unfrozenBalance;
	// 总资金余额（即当前金额）
	protected BigDecimal fundBalance;
		
	//盈亏金额
	protected BigDecimal incomeBalance;
	// 股票资产 (证券市值)
	protected BigDecimal assetPrice;
	// 盈利数量
	protected BigDecimal profitAmount;
	// 亏损数量
	protected BigDecimal deficitAmount;
	// 持平数量数量
	protected BigDecimal fairAmount;
	
	//今日盈亏比例	
	private String percent;

	public String getPercent() {
		return percent;
	}

	public void setPercent(String percent) {
		this.percent = percent;
	}

	public BigDecimal getCurrentBalance() {
		return currentBalance;
	}

	public void setCurrentBalance(BigDecimal currentBalance) {
		this.currentBalance = currentBalance;
	}

	public BigDecimal getEnableBalance() {
		return enableBalance;
	}

	public void setEnableBalance(BigDecimal enableBalance) {
		this.enableBalance = enableBalance;
	}

	public BigDecimal getFetchBalance() {
		return fetchBalance;
	}

	public void setFetchBalance(BigDecimal fetchBalance) {
		this.fetchBalance = fetchBalance;
	}

	public BigDecimal getFrozenBalance() {
		return frozenBalance;
	}

	public void setFrozenBalance(BigDecimal frozenBalance) {
		this.frozenBalance = frozenBalance;
	}

	public BigDecimal getUnfrozenBalance() {
		return unfrozenBalance;
	}

	public void setUnfrozenBalance(BigDecimal unfrozenBalance) {
		this.unfrozenBalance = unfrozenBalance;
	}

	public BigDecimal getFundBalance() {
		return fundBalance;
	}

	public void setFundBalance(BigDecimal fundBalance) {
		this.fundBalance = fundBalance;
	}

	public BigDecimal getIncomeBalance() {
		return incomeBalance;
	}

	public void setIncomeBalance(BigDecimal incomeBalance) {
		this.incomeBalance = incomeBalance;
	}

	public BigDecimal getAssetPrice() {
		return assetPrice;
	}

	public void setAssetPrice(BigDecimal assetPrice) {
		this.assetPrice = assetPrice;
	}

	public BigDecimal getProfitAmount() {
		return profitAmount;
	}

	public void setProfitAmount(BigDecimal profitAmount) {
		this.profitAmount = profitAmount;
	}

	public BigDecimal getDeficitAmount() {
		return deficitAmount;
	}

	public void setDeficitAmount(BigDecimal deficitAmount) {
		this.deficitAmount = deficitAmount;
	}

	public BigDecimal getFairAmount() {
		return fairAmount;
	}

	public void setFairAmount(BigDecimal fairAmount) {
		this.fairAmount = fairAmount;
	}
	
	
}
