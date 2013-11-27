package com.andrnes.modoer;

import java.io.Serializable;

/** 聚会分类表 */
public class ModoerPartyCategory implements Serializable {

	private static final long serialVersionUID = 1L;
	private int id;
	/** 名称 */
	private String name;

	/** 图片数量 */
	private int num;

	/** 排序 */
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
