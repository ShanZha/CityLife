package com.andrnes.modoer;

import java.io.Serializable;

/** ��������� */
public class ModoerReviewOptGroup implements Serializable {

	private static final long serialVersionUID = 1L;
	private int id;
	/** ���� */
	private String name;

	/** ˵������ */
	private String des;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDes() {
		return des;
	}

	public void setDes(String des) {
		this.des = des;
	}
}
