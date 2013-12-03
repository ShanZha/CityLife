package com.andrnes.modoer;

import java.io.Serializable;

import com.zj.app.annotation.Lazy;

/** ������ */
public class ModoerArea implements Serializable {

	private static final long serialVersionUID = 1L;
	private int id;
	/** �����ϼ�ID��0Ϊһ�����������У� */
	@Lazy(isLazy = true)
	private ModoerArea pid;
	/** ���ж������������Ŀ¼ */
	private String domain;
	/** ����ĸ��д */
	private String initial;
	/** ���� */
	private String name;
	/** ����� */
	private String mappoint;
	/** �ȼ���1�����ң�2��ʡ/�ݣ�3����/�� */
	private int level;
	/** ���� */
	private int listorder = 0;
	/** ��վģ�壨��level=1ʹ�ã� */
	private int templateid = 0;
	/** �Ƿ����� */
	private int enabled = 1;
	/** ������Ϣ */
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
