package com.andrnes.modoer;

import com.zj.app.annotation.Lazy;

/** 主题申请表 **/
public class ModoerSubjectapply {

	private int id;

	/** 城市ID */
	@Lazy(isLazy = true)
	private ModoerSubject sid;

	/** 创建者UID */
	@Lazy(isLazy = true)
	private ModoerMembers uid;

	/** 创建者昵称 */
	private String username;

	/** 状态 */
	private int status;

	/** 申请人 */
	private String applyname;

	/** 申请人联系方式 */
	private String contact;

	/** 上传的图片 */
	private String pic;

	/** 备注 */
	private String content;

	/** 申请时间 */
	private String dateline;

	/** 审核人 */
	private String checker;

	/** 审核备注 */
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
