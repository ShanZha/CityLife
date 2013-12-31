package com.andrnes.modoer;

/** 标签组 **/
public class ModoerTaggroup {

	private int id;

	/** 标签组名称 */
	private String name;

	/** 标题组内容 */
	private String options;

	/** 排序 */
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
