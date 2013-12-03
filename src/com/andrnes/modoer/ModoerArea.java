package com.andrnes.modoer;

import java.io.Serializable;

import com.zj.app.annotation.Lazy;

/** 地区表 */
public class ModoerArea implements Serializable {

	private static final long serialVersionUID = 1L;
	private int id;
	/** 地区上级ID，0为一级地区（城市） */
	@Lazy(isLazy = true)
	private ModoerArea pid;
	/** 城市二级域名或城市目录 */
	private String domain;
	/** 首字母缩写 */
	private String initial;
	/** 名称 */
	private String name;
	/** 坐标点 */
	private String mappoint;
	/** 等级（1：国家，2：省/州，3：县/区 */
	private int level;
	/** 排序 */
	private int listorder = 0;
	/** 主站模板（仅level=1使用） */
	private int templateid = 0;
	/** 是否启用 */
	private int enabled = 1;
	/** 配置信息 */
	private String config;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public ModoerArea getPid() {
		return pid;
	}

	public void setPid(ModoerArea pid) {
		this.pid = pid;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getInitial() {
		return initial;
	}

	public void setInitial(String initial) {
		this.initial = initial;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMappoint() {
		return mappoint;
	}

	public void setMappoint(String mappoint) {
		this.mappoint = mappoint;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getListorder() {
		return listorder;
	}

	public void setListorder(int listorder) {
		this.listorder = listorder;
	}

	public int getTemplateid() {
		return templateid;
	}

	public void setTemplateid(int templateid) {
		this.templateid = templateid;
	}

	public int getEnabled() {
		return enabled;
	}

	public void setEnabled(int enabled) {
		this.enabled = enabled;
	}

	public String getConfig() {
		return config;
	}

	public void setConfig(String config) {
		this.config = config;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "ModoerArea[" + "domain = " + domain + ",initial = " + initial
				+ ",name = " + name + ",level = " + level + ",config= "
				+ config + "]";
	}
}
