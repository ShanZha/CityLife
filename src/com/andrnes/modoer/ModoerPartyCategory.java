package com.andrnes.modoer;

import java.io.Serializable;

/** �ۻ����� */
public class ModoerPartyCategory implements Serializable {

	private static final long serialVersionUID = 1L;
	private int id;
	/** ���� */
	private String name;

	/** ͼƬ���� */
	private int num;

	/** ���� */
	private int listorder;

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

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public int getListorder() {
		return listorder;
	}

	public void setListorder(int listorder) {
		this.listorder = listorder;
	}
}
