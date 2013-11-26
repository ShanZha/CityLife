package com.andrnes.modoer;

import java.io.Serializable;

import com.zj.app.annotation.Lazy;

/** 问答类型 **/
public class ModoerAskCategory implements Serializable {

	private static final long serialVersionUID = 1L;
	private int id;
	/** 上级分类 */
	@Lazy(isLazy = true)
	private ModoerAskCategory pid;

	/** 名称 */
	private String name;

	/** 排序 */
	private int listorder;

	/** 数量 */
	private int total;

	/** 分类级别 0:父级 1:子级 */
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
