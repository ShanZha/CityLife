package com.andrnes.modoer;

/** '短信息表 */
public class ModoerPmsgs {

	private int id;
	/** 发送者uid */
	private ModoerMembers senduid;

	/** 接受者uid */
	private ModoerMembers recvuid;

	/** 内容 */
	private String content;

	/** 标题 */
	private String subject;

	/** 发送时间 */
	private int posttime;

	/** 是否新短信(1-是) */
	private int isNew;

	/** 双边删除标记 */
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
