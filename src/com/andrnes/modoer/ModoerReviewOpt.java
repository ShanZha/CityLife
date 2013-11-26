package com.andrnes.modoer;

import com.zj.app.annotation.Lazy;

/** 点评举报表 */
public class ModoerReviewOpt {

	private int id;
	/** 点评项组ID */
	@Lazy(isLazy = true)
	private ModoerReviewOptGroup gid;

	/** 点评项字段名 */
	private String flag;

	/** 名称 */
	private String name;

	/** 排序 */
	private int listorder;

	/** 是否启用 */
	private int enable;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public ModoerReviewOptGroup getGid() {
		return gid;
	}

	public void setGid(ModoerReviewOptGroup gid) {
		this.gid = gid;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
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

	public int getEnable() {
		return enable;
	}

	public void setEnable(int enable) {
		this.enable = enable;
	}
}
