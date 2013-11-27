package com.andrnes.modoer;

import java.io.Serializable;

/** 点评项组表 */
public class ModoerUsergroups implements Serializable {

	private static final long serialVersionUID = 1L;
	private int id;
	/** 用户组类型('member','special','system') */
	private String grouptype;

	/** 用户组名称 */
	private String groupname;

	/** 积分范围 */
	private int point;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getGrouptype() {
		return grouptype;
	}

	public void setGrouptype(String grouptype) {
		this.grouptype = grouptype;
	}

	public String getGroupname() {
		return groupname;
	}

	public void setGroupname(String groupname) {
		this.groupname = groupname;
	}

	public int getPoint() {
		return point;
	}

	public void setPoint(int point) {
		this.point = point;
	}
}
