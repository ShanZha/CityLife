package com.andrnes.modoer;

public class ModoerFenleiCategory {

	private int id;
	/** 上级分类ID */
	private ModoerFenleiCategory pid;

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
