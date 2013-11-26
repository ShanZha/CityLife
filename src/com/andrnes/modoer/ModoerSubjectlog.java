package com.andrnes.modoer;

/** 点评报错表 **/
public class ModoerSubjectlog {

	private int id;
	/** 主题id */
	private ModoerSubject sid;

	/** 创建者UID */
	private ModoerMembers uid;

	/** 用户昵称 */
	private String username;

	/** email */
	private String email;

	/** 是否地图报错（1是，0否） */
	private int ismappoint;

	/** 补充内容 */
	private String upcontent;

	/** 是否审核通过 */
	private int disposal;

	/** 提交时间 */
	private int posttime;

	/** 处理说明 */
	private String upremark;

	/** 审核时间 */
	private int disposaltime;

	/** 是否给补充人积分 */
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
