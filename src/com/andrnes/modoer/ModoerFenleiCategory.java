package com.andrnes.modoer;

import java.io.Serializable;

public class ModoerFenleiCategory implements Serializable {

	private static final long serialVersionUID = 1L;
	private int id;
	/** �ϼ�����ID */
	private ModoerFenleiCategory pid;

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

	public ModoerFenleiCategory getPid() {
		return pid;
	}

	public void setPid(ModoerFenleiCategory pid) {
		this.pid = pid;
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
