package com.andrnes.modoer;

/** '����Ϣ�� */
public class ModoerPmsgs {

	private int id;
	/** ������uid */
	private ModoerMembers senduid;

	/** ������uid */
	private ModoerMembers recvuid;

	/** ���� */
	private String content;

	/** ���� */
	private String subject;

	/** ����ʱ�� */
	private int posttime;

	/** �Ƿ��¶���(1-��) */
	private int isNew;

	/** ˫��ɾ����� */
	private int delflag;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public ModoerMembers getSenduid() {
		return senduid;
	}

	public void setSenduid(ModoerMembers senduid) {
		this.senduid = senduid;
	}

	public ModoerMembers getRecvuid() {
		return recvuid;
	}

	public void setRecvuid(ModoerMembers recvuid) {
		this.recvuid = recvuid;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public int getPosttime() {
		return posttime;
	}

	public void setPosttime(int posttime) {
		this.posttime = posttime;
	}

	public int getIsNew() {
		return isNew;
	}

	public void setIsNew(int isNew) {
		this.isNew = isNew;
	}

	public int getDelflag() {
		return delflag;
	}

	public void setDelflag(int delflag) {
		this.delflag = delflag;
	}

}
