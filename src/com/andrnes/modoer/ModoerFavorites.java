package com.andrnes.modoer;

/** �ղر� **/
public class ModoerFavorites {

	private int id;
	/** �û�uid */
	private ModoerMembers uid;

	/** �û��� */
	private String username;

	/** ����id */
	private ModoerSubject sid;

	/** �ղ�ʱ�� */
	private int addtime;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public ModoerSubject getSid() {
		return sid;
	}

	public void setSid(ModoerSubject sid) {
		this.sid = sid;
	}

	public int getAddtime() {
		return addtime;
	}

	public void setAddtime(int addtime) {
		this.addtime = addtime;
	}
}