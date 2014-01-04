package com.andrnes.modoer;

import java.util.Map;

import com.zj.app.annotation.Lazy;

/** 用户第三方帐号绑定表 */
public class ModoerMemberPassport {

	private int id;
	/** 绑定用户UID */
	private String psuid;

	/** 绑定用户UID */
	@Lazy(isLazy = true)
	private ModoerMembers uid;

	/** 绑定种类 [weibo, qq, taobao, google, qzone, renren] */
	private String psname;

	private String accessToken;

	private long expired;

	private int isbind;
	
	private Map errors;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPsuid() {
		return psuid;
	}

	public void setPsuid(String psuid) {
		this.psuid = psuid;
	}

	public ModoerMembers getUid() {
		return uid;
	}

	public void setUid(ModoerMembers uid) {
		this.uid = uid;
	}

	public String getPsname() {
		return psname;
	}

	public void setPsname(String psname) {
		this.psname = psname;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public long getExpired() {
		return expired;
	}

	public void setExpired(long expired) {
		this.expired = expired;
	}

	public int getIsbind() {
		return isbind;
	}

	public void setIsbind(int isbind) {
		this.isbind = isbind;
	}

	public Map getErrors() {
		return errors;
	}

	public void setErrors(Map errors) {
		this.errors = errors;
	}
}
