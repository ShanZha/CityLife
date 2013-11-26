package com.andrnes.modoer;

import com.zj.app.annotation.Lazy;

/** ��������� **/
public class ModoerSubjectapply {

	private int id;

	/** ����ID */
	@Lazy(isLazy = true)
	private ModoerSubject sid;

	/** ������UID */
	@Lazy(isLazy = true)
	private ModoerMembers uid;

	/** �������ǳ� */
	private String username;

	/** ״̬ */
	private int status;

	/** ������ */
	private String applyname;

	/** ��������ϵ��ʽ */
	private String contact;

	/** �ϴ���ͼƬ */
	private String pic;

	/** ��ע */
	private String content;

	/** ����ʱ�� */
	private String dateline;

	/** ����� */
	private String checker;

	/** ��˱�ע */
	private String returned;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public ModoerSubject getSid() {
		return sid;
	}

	public void setSid(ModoerSubject sid) {
		this.sid = sid;
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

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getApplyname() {
		return applyname;
	}

	public void setApplyname(String applyname) {
		this.applyname = applyname;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getDateline() {
		return dateline;
	}

	public void setDateline(String dateline) {
		this.dateline = dateline;
	}

	public String getChecker() {
		return checker;
	}

	public void setChecker(String checker) {
		this.checker = checker;
	}

	public String getReturned() {
		return returned;
	}

	public void setReturned(String returned) {
		this.returned = returned;
	}
}
