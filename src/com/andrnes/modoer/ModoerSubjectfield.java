package com.andrnes.modoer;

/** 主题字段表 **/
public class ModoerSubjectfield {

	private int id;
	/** 模型ID */
	private int modelid;

	/** 附表名 */
	private String tablename;

	/** 附表字段名 */
	private String fieldname;

	/** 标题 */
	private String title;

	/** 单位 */
	private String unit;

	/** 描述 */
	private String note;

	/** 字段类型 */
	private String type;

	/** 排序字段 */
	private int listorder;

	/** 允许空值 */
	private int allownull;

	/** 验证失败提示信息 */
	private String errmsg;

	/** 是否禁用 */
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
