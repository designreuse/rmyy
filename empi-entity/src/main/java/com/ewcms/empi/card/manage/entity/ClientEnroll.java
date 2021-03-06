package com.ewcms.empi.card.manage.entity;

import java.util.Calendar;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

import com.alibaba.fastjson.annotation.JSONField;
import com.ewcms.common.entity.BaseEntity;

/**
 * 客户端注册
 * <ul>
 * <li>ip：ip地址</li>
 * <li>mac：网卡物理地址</li>
 * <li>department：所属科室</li>
 * <li>createDate:注册时间</li>
 * <li>userName：用户名</li>
 * <li>password:密码</li>
 * <li>hapiVersion:客户端hapi版本</li>
 * <li>auth:客户端是否需要认证</li>
 * </ul>
 *@author zhoudongchu
 */
@Entity
@Table(name = "card_client_enroll")

public class ClientEnroll extends BaseEntity<Long> {
	private static final long serialVersionUID = 934101109141213565L;
	@Column(name = "ip",nullable = false,unique = true)
	private String ip;
	@Column(name = "mac",nullable = false)
	private String mac;
	@Column(name = "department")
	private String department;
	@Column(name = "user_name",nullable = false,unique = true)
	private String userName;
	@Column(name = "password",nullable = false)
	private String password;
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "create_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate = Calendar.getInstance().getTime();	
	@Enumerated(EnumType.STRING)
	@Column(name = "hapi_version")
	private HapiVersion hapiVersion = HapiVersion.v6;
	@Column(name = "is_auth")
	private Boolean auth = Boolean.TRUE;
	
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getMac() {
		return mac;
	}
	public void setMac(String mac) {
		this.mac = mac;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public HapiVersion getHapiVersion() {
		return hapiVersion;
	}
	public void setHapiVersion(HapiVersion hapiVersion) {
		this.hapiVersion = hapiVersion;
	}
	public String getHapiVersionInfo(){
		return hapiVersion == null ? HapiVersion.v6.getInfo() : hapiVersion.getInfo();
	}
	public String getUserName() {
		return userName;
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
	public Boolean getAuth() {
		return auth;
	}
	public void setAuth(Boolean auth) {
		this.auth = auth;
	}
}
