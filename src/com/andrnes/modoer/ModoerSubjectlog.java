package com.andrnes.modoer;

/** ��������� **/
public class ModoerSubjectlog {

	private int id;
	/** ����id */
	private ModoerSubject sid;

	/** ������UID */
	private ModoerMembers uid;

	/** �û��ǳ� */
	private String username;

	/** email */
	private String email;

	/** �Ƿ��ͼ����1�ǣ�0�� */
	private int ismappoint;

	/** �������� */
	private String upcontent;

	/** �Ƿ����ͨ�� */
	private int disposal;

	/** �ύʱ�� */
	private int posttime;

	/** ����˵�� */
	private String upremark;

	/** ���ʱ�� */
	private int disposaltime;

	/** �Ƿ�������˻��� */
	private int updatePoint;

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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getIsmappoint() {
		return ismappoint;
	}

	public void setIsmappoint(int ismappoint) {
		this.ismappoint = ismappoint;
	}

	public String getUpcontent() {
		return upcontent;
	}

	public void setUpcontent(String upcontent) {
		this.upcontent = upcontent;
	}

	public int getDisposal() {
		return disposal;
	}

	public void setDisposal(int disposal) {
		this.disposal = disposal;
	}

	public int getPosttime() {
		return posttime;
	}

	public void setPosttime(int posttime) {
		this.posttime = posttime;
	}

	public String getUpremark() {
		return upremark;
	}

	public void setUpremark(String upremark) {
		this.upremark = upremark;
	}

	public int getDisposaltime() {
		return disposaltime;
	}

	public void setDisposaltime(int disposaltime) {
		this.disposaltime = disposaltime;
	}

	public int getUpdatePoint() {
		return updatePoint;
	}

	public void setUpdatePoint(int updatePoint) {
		this.updatePoint = updatePoint;
	}
}
