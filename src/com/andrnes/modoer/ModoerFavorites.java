package com.andrnes.modoer;

import com.zj.app.annotation.Lazy;

/** 收藏表 **/
public class ModoerFavorites {

	private int id;
	/** 用户uid */
	@Lazy(isLazy = true)
	private ModoerMembers uid;

	/** 用户名 */
	private String username;

	/** 主题id */
	@Lazy(isLazy = true)
	private ModoerSubject sid;

	/** 收藏时间 */
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
