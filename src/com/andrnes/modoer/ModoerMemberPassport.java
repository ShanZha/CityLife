package com.andrnes.modoer;

/** �û��������ʺŰ󶨱� */
public class ModoerMemberPassport {

	private int id;
	/** ���û�UID */
	private String psuid;

	/** ���û�UID */
	private ModoerMembers uid;

	/** ������ */
	private String psname;

	private String accessToken;

	private int expired;

	private int isbind;

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

	public int getExpired() {
		return expired;
	}

	public void setExpired(int expired) {
		this.expired = expired;
	}

	public int getIsbind() {
		return isbind;
	}

	public void setIsbind(int isbind) {
		this.isbind = isbind;
	}
}
