package com.andrnes.modoer;

/** �ۻ������ **/
public class ModoerPartyApply {

	private int id;
	/** ����ۻ�id */
	private ModoerParty partyid;

	private ModoerMembers uid;

	/** �û��� */
	private String username;

	/** ��ϵ�� */
	private String linkman;

	/** ��ϵ��ʽ */
	private String contact;

	/** �Ա� */
	private int sex;

	/** ״̬ */
	private int status;

	/** ����ʱ�� */
	private int dateline;

	/** �Ƿ�μ� */
	private int isjoin;

	/** ��ע˵�� */
	private String comment;

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
}
