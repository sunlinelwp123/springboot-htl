package com.open.boot.user.model;

import javax.persistence.*;
import java.util.Date;

@Table(name = "htl_user")
public class HtlUser {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	/**
	 * 用户名
	 */
	@Column(name = "user_name")
	private String userName;

	/**
	 * 真实姓名
	 */
	@Column(name = "real_name")
	private String realName;

	/**
	 * 微信用户名
	 */
	@Column(name = "nick_name")
	private String nickName;

	/**
	 * 密码
	 */
	private String password;

	/**
	 * 性别
	 */
	private String sex;

	/**
	 * 角色类型
	 */
	private String role;

	/**
	 * 邮箱
	 */
	private String email;

	/**
	 * 电话
	 */
	private String tel;

	/**
	 * 地址
	 */
	private String address;

	/**
	 * 头像地址
	 */
	private String photourl;

	/**
	 * 创建时间
	 */
	@Column(name = "create_date")
	private Date createDate;

	/**
	 * 用户类型（1企业2个人）
	 */
	@Column(name = "user_type")
	private String userType;

	/**
	 * 编辑时间
	 */
	@Column(name = "edit_date")
	private Date editDate;

	/**
	 * 所在地-行政区划字典表
	 */
	@Column(name = "area_code")
	private String areaCode;

	/**
	 * 人员证件类型字典表
	 */
	@Column(name = "id_card_type")
	private String idCardType;

	@Column(name = "id_card_number")
	private String idCardNumber;
	/**
	 * 登录token
	 */
	@Transient
	private String token;
	/**
	 * 注冊部門
	 */
	@Column(name = "branch_id")
	private String branchId;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhotourl() {
		return photourl;
	}

	public void setPhotourl(String photourl) {
		this.photourl = photourl;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public Date getEditDate() {
		return editDate;
	}

	public void setEditDate(Date editDate) {
		this.editDate = editDate;
	}

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	public String getIdCardType() {
		return idCardType;
	}

	public void setIdCardType(String idCardType) {
		this.idCardType = idCardType;
	}

	public String getIdCardNumber() {
		return idCardNumber;
	}

	public void setIdCardNumber(String idCardNumber) {
		this.idCardNumber = idCardNumber;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getBranchId() {
		return branchId;
	}

	public void setBranchId(String branchId) {
		this.branchId = branchId;
	}

	@Override
	public String toString() {
		return "HtlUser [id=" + id + ", userName=" + userName + ", realName=" + realName + ", nickName=" + nickName
				+ ", password=" + password + ", sex=" + sex + ", role=" + role + ", email=" + email + ", tel=" + tel
				+ ", address=" + address + ", photourl=" + photourl + ", createDate=" + createDate + ", userType="
				+ userType + ", editDate=" + editDate + ", areaCode=" + areaCode + ", idCardType=" + idCardType
				+ ", idCardNumber=" + idCardNumber + ", token=" + token + ", branchId=" + branchId + "]";
	}

}
