package com.andrnes.modoer;

import com.zj.app.annotation.Lazy;

/** �����ٱ��� */
public class ModoerReviewOpt {

	private int id;
	/** ��������ID */
	@Lazy(isLazy = true)
	private ModoerReviewOptGroup gid;

	/** �������ֶ��� */
	private String flag;

	/** ���� */
	private String name;

	/** ���� */
	private int listorder;

	/** �Ƿ����� */
	private int enable;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public ModoerReviewOptGroup getGid() {
		return gid;
	}

	public void setGid(ModoerReviewOptGroup gid) {
		this.gid = gid;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getListorder() {
		return listorder;
	}

	public void setListorder(int listorder) {
		this.listorder = listorder;
	}

	public int getEnable() {
		return enable;
	}

	public void setEnable(int enable) {
		this.enable = enable;
	}
}
