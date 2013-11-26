package com.andrnes.modoer;

import java.io.Serializable;

import com.zj.app.annotation.Lazy;

/** �ʴ����� **/
public class ModoerAskCategory implements Serializable {

	private static final long serialVersionUID = 1L;
	private int id;
	/** �ϼ����� */
	@Lazy(isLazy = true)
	private ModoerAskCategory pid;

	/** ���� */
	private String name;

	/** ���� */
	private int listorder;

	/** ���� */
	private int total;

	/** ���༶�� 0:���� 1:�Ӽ� */
	private int use_area;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public ModoerAskCategory getPid() {
		return pid;
	}

	public void setPid(ModoerAskCategory pid) {
		this.pid = pid;
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

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public int getUse_area() {
		return use_area;
	}

	public void setUse_area(int use_area) {
		this.use_area = use_area;
	}
}
