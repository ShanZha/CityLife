package com.andrnes.modoer;

/** �����ֶα� **/
public class ModoerSubjectfield {

	private int id;
	/** ģ��ID */
	private int modelid;

	/** ������ */
	private String tablename;

	/** �����ֶ��� */
	private String fieldname;

	/** ���� */
	private String title;

	/** ��λ */
	private String unit;

	/** ���� */
	private String note;

	/** �ֶ����� */
	private String type;

	/** �����ֶ� */
	private int listorder;

	/** �����ֵ */
	private int allownull;

	/** ��֤ʧ����ʾ��Ϣ */
	private String errmsg;

	/** �Ƿ���� */
	private int disable;

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

	public String getTablename() {
		return tablename;
	}

	public void setTablename(String tablename) {
		this.tablename = tablename;
	}

	public String getFieldname() {
		return fieldname;
	}

	public void setFieldname(String fieldname) {
		this.fieldname = fieldname;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getListorder() {
		return listorder;
	}

	public void setListorder(int listorder) {
		this.listorder = listorder;
	}

	public int getAllownull() {
		return allownull;
	}

	public void setAllownull(int allownull) {
		this.allownull = allownull;
	}

	public String getErrmsg() {
		return errmsg;
	}

	public void setErrmsg(String errmsg) {
		this.errmsg = errmsg;
	}

	public int getDisable() {
		return disable;
	}

	public void setDisable(int disable) {
		this.disable = disable;
	}
}
