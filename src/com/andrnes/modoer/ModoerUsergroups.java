package com.andrnes.modoer;

import java.io.Serializable;

/** ��������� */
public class ModoerUsergroups implements Serializable {

	private static final long serialVersionUID = 1L;
	private int id;
	/** �û�������('member','special','system') */
	private String grouptype;

	/** �û������� */
	private String groupname;

	/** ���ַ�Χ */
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
