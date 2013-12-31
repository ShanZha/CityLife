package com.andrnes.modoer;

/** 标识表 */
public class ModoerAttList implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private int id;
	/** 名称 */
	private String name;

	/** 图标 */
	private String icon;

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

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public int getListorder() {
		return listorder;
	}

	public void setListorder(int listorder) {
		this.listorder = listorder;
	}

}
