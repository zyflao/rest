package com.cfo.stock.web.rest.vo;
/**
 * 用户基础信息
 * @author yuanlong.wang
 *
 */
public class UserBaseVo extends BaseVo{
	//用户ID 也有可以能用户名
	private String userid;
	//用户唯一号 
	private String uniquecode;
	//开户状态
	private Integer openstatus;
	// 是否为员工，0：不是，1：是
	private Integer companyuser; 
	private String regtime; // 注册时间 yyyy-MM-dd HH:mm:ss
	private Integer userstatus; // 1:已注册，-2:冻结,-1:未激活
	private Integer failtimes; // 登录失败次数
	private Integer frozenremainseconds; // 登录冻结剩余秒数
	private String lastsuccesstime; // 最后成功登录时间 yyyy-MM-dd HH:mm:ss
	//身份证号
	private String idnumber;
	//绑定券商状态
	private int bindstatus;
	
	public UserBaseVo() {
		// TODO Auto-generated constructor stub
	}
	

	public UserBaseVo(String userid, String uniquecode, Integer openstatus,
			Integer failtimes, Integer frozenremainseconds) {
		super();
		this.userid = userid;
		this.uniquecode = uniquecode;
		this.openstatus = openstatus;
		this.failtimes = failtimes;
		this.frozenremainseconds = frozenremainseconds;
	}

	public UserBaseVo(String userid, String uniquecode, int openstatus,
			String idnumber, int bindstatus) {
		super();
		this.userid = userid;
		this.uniquecode = uniquecode;
		this.openstatus = openstatus;
		this.idnumber = idnumber;
		this.bindstatus = bindstatus;
	}
	public UserBaseVo(String userid, String uniquecode, int openstatus,
			String idnumber, int bindstatus,Integer failtimes) {
		super();
		this.userid = userid;
		this.uniquecode = uniquecode;
		this.openstatus = openstatus;
		this.idnumber = idnumber;
		this.bindstatus = bindstatus;
		this.failtimes=failtimes;
	}


	public UserBaseVo(String userid, String regtime, Integer userstatus) {
		super();
		this.userid = userid;
		this.regtime = regtime;
		this.userstatus = userstatus;
	}


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


	public Integer getOpenstatus() {
		return openstatus;
	}


	public void setOpenstatus(Integer openstatus) {
		this.openstatus = openstatus;
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


	public String getIdnumber() {
		return idnumber;
	}


	public void setIdnumber(String idnumber) {
		this.idnumber = idnumber;
	}


	public int getBindstatus() {
		return bindstatus;
	}


	public void setBindstatus(int bindstatus) {
		this.bindstatus = bindstatus;
	}
	
	
}
