package com.andrnes.modoer;

/** ��ǩ�� **/
public class ModoerTaggroup {

	private int id;

	/** ��ǩ������ */
	private String name;

	/** ���������� */
	private String options;

	/** ���� */
	private int sort = 0;

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

	public String getOptions() {
		return options;
	}

	public void setOptions(String options) {
		this.options = options;
	}

	public int getSort() {
		return sort;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}

}
