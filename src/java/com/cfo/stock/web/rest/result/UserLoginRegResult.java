package com.cfo.stock.web.rest.result;

import com.cfo.stock.web.rest.vo.UserBaseVo;

public class UserLoginRegResult extends Result<UserBaseVo> {

	private String userid;
	// 用户唯一码
	private String uniquecode;
	// 是否为员工，0：不是，1：是
	private Integer companyuser; 
	// 注册时间 yyyy-MM-dd HH:mm:ss
	private String regtime; 
	// 1:已注册，-2:冻结,-1:未激活
	private Integer userstatus; 
	 // 登录失败次数
	private Integer failtimes;
	// 登录冻结剩余秒数
	private Integer frozenremainseconds; 
	// 最后成功登录时间 yyyy-MM-dd HH:mm:ss
	private String lastsuccesstime; 
	
	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getUniquecode() {
		return uniquecode;
	}

	public void setUniquecode(String uniquecode) {
		this.uniquecode = uniquecode;
	}

	public Integer getCompanyuser() {
		return companyuser;
	}

	public void setCompanyuser(Integer companyuser) {
		this.companyuser = companyuser;
	}

	public String getRegtime() {
		return regtime;
	}

	public void setRegtime(String regtime) {
		this.regtime = regtime;
	}

	public Integer getUserstatus() {
		return userstatus;
	}

	public void setUserstatus(Integer userstatus) {
		this.userstatus = userstatus;
	}

	public Integer getFailtimes() {
		return failtimes;
	}

	public void setFailtimes(Integer failtimes) {
		this.failtimes = failtimes;
	}

	public Integer getFrozenremainseconds() {
		return frozenremainseconds;
	}

	public void setFrozenremainseconds(Integer frozenremainseconds) {
		this.frozenremainseconds = frozenremainseconds;
	}

	public String getLastsuccesstime() {
		return lastsuccesstime;
	}

	public void setLastsuccesstime(String lastsuccesstime) {
		this.lastsuccesstime = lastsuccesstime;
	}

	@Override
	public UserBaseVo parse() {
		return new UserBaseVo(userid, uniquecode, companyuser, regtime,
				userstatus, failtimes);
	}
}
