package com.andrnes.modoer;

import java.io.Serializable;

import com.zj.app.annotation.Lazy;

/** �������� */
public class ModoerCategory implements Serializable {

	private static final long serialVersionUID = 1L;
	private int id;
	/** ģ��ID */
	private int modelid;

	/** �ϼ�����id */
	@Lazy(isLazy = true)
	private ModoerCategory pid;

	/** ������������ID */
	@Lazy(isLazy = true)
	private ModoerReviewOptGroup reviewOptGid;

	/** �ȼ���1-3�� */
	private int level;

	/** ���� */
	private String name;

	/** �������� */
	private int total;

	/** ������Ϣ */
	private String config;

	/** ���� */
	private int listorder;

	/** '�Ƿ����(1) */
	private int enabled = 1;

	/** �����ӷ���ID���� */
	private String subcats;

	/** �������ӷ���ID���� */
	private String nonuseSubcats;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getModelid() {
		return modelid;
	}

	public void setModelid(int modelid) {
		this.modelid = modelid;
	}

	public ModoerCategory getPid() {
		return pid;
	}

	public void setPid(ModoerCategory pid) {
		this.pid = pid;
	}

	public ModoerReviewOptGroup getReviewOptGid() {
		return reviewOptGid;
	}

	public void setReviewOptGid(ModoerReviewOptGroup reviewOptGid) {
		this.reviewOptGid = reviewOptGid;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public String getConfig() {
		return config;
	}

	public void setConfig(String config) {
		this.config = config;
	}

	public int getListorder() {
		return listorder;
	}

	public void setListorder(int listorder) {
		this.listorder = listorder;
	}

	public int getEnabled() {
		return enabled;
	}

	public void setEnabled(int enabled) {
		this.enabled = enabled;
	}

	public String getSubcats() {
		return subcats;
	}

	public void setSubcats(String subcats) {
		this.subcats = subcats;
	}

	public String getNonuseSubcats() {
		return nonuseSubcats;
	}

	public void setNonuseSubcats(String nonuseSubcats) {
		this.nonuseSubcats = nonuseSubcats;
	}
}
