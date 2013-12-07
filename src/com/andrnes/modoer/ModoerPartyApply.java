package com.andrnes.modoer;

import java.util.Map;

import com.zj.app.annotation.Validates;

/** 聚会申请表 **/
public class ModoerPartyApply {

	private int id;
	/** 申请聚会id */
	private ModoerParty partyid;

	private ModoerMembers uid;

	/** 用户名 */
	@Validates(info = "用户名不能为空,用户名不能超过20个字符", type = "isNull,maxSize20")
	private String username;

	/** 联系人 */
	@Validates(info = "联系人不能为空,联系人不能超过20个字符", type = "isNull,maxSize20")
	private String linkman;

	/** 联系方式 */
	@Validates(info = "联系方式不能为空,联系方式不能超过255个字符", type = "isNull,maxSize255")
	private String contact;

	/** 性别 */
	private int sex;

	/** 状态 */
	private int status;

	/** 申请时间 */
	private int dateline;

	/** 是否参加 */
	private int isjoin;

	/** 备注说明 */
	@Validates(info = "备注不能超过255个字符", type = "maxSize255")
	private String comment;

	private Map errors;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public ModoerParty getPartyid() {
		return partyid;
	}

	public void setPartyid(ModoerParty partyid) {
		this.partyid = partyid;
	}

	public ModoerMembers getUid() {
		return uid;
	}

	public void setUid(ModoerMembers uid) {
		this.uid = uid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getLinkman() {
		return linkman;
	}

	public void setLinkman(String linkman) {
		this.linkman = linkman;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getDateline() {
		return dateline;
	}

	public void setDateline(int dateline) {
		this.dateline = dateline;
	}

	public int getIsjoin() {
		return isjoin;
	}

	public void setIsjoin(int isjoin) {
		this.isjoin = isjoin;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Map getErrors() {
		return errors;
	}

	public void setErrors(Map errors) {
		this.errors = errors;
	}
}
