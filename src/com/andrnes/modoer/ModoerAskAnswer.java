package com.andrnes.modoer;

import java.io.Serializable;
import java.util.Map;

import com.zj.app.annotation.Lazy;
import com.zj.app.annotation.Validates;

/** �ʴ� **/
public class ModoerAskAnswer implements Serializable {

	private static final long serialVersionUID = 1L;
	private int id;
	/** �������� */
	@Lazy(isLazy = true)
	private ModoerAskCategory catid;

	/** �����ʴ� */
	@Lazy(isLazy = true)
	private ModoerAsks askid;

	/** �����û� */
	@Lazy(isLazy = true)
	private ModoerMembers uid;

	/** �û��ǳ� */
	@Validates(info = "�û��ǳƲ���Ϊ��,�û��ǳƳ��Ȳ��ܳ���32���ַ�", type = "isNull,maxSize32")
	private String username;

	/** ���� */
	private int goodrate;

	/** ���� */
	private int badrate;

	/** ��ѻش������ */
	private String feel;

	/** �ο���ַ */
	@Validates(info = "�ο���ַ���Ȳ��ܳ���100���ַ�", type = "maxSize100")
	private String brief;

	/** ip */
	@Validates(info = "IP����Ϊ��,IP���Ȳ��ܳ���15���ַ�", type = "isNull,maxSize15")
	private String ip;

	/** �ش�ʱ�� */
	private int dateline;

	/** ״̬��0��Ч1 ��Ч �� */
	private int status;

	/** �������� */
	@Validates(info = "�������ݲ���Ϊ��,�������ݳ��Ȳ��ܳ���255���ַ�", type = "isNull,maxSize255")
	private String content;

	private Map errors;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public ModoerAskCategory getCatid() {
		return catid;
	}

	public void setCatid(ModoerAskCategory catid) {
		this.catid = catid;
	}

	public ModoerAsks getAskid() {
		return askid;
	}

	public void setAskid(ModoerAsks askid) {
		this.askid = askid;
	}

	public ModoerMembers getUid() {
		return uid;
	}

	public void setUid(ModoerMembers uid) {
		this.uid = uid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public int getGoodrate() {
		return goodrate;
	}

	public void setGoodrate(int goodrate) {
		this.goodrate = goodrate;
	}

	public int getBadrate() {
		return badrate;
	}

	public void setBadrate(int badrate) {
		this.badrate = badrate;
	}

	public String getFeel() {
		return feel;
	}

	public void setFeel(String feel) {
		this.feel = feel;
	}

	public String getBrief() {
		return brief;
	}

	public void setBrief(String brief) {
		this.brief = brief;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getDateline() {
		return dateline;
	}

	public void setDateline(int dateline) {
		this.dateline = dateline;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Map getErrors() {
		return errors;
	}

	public void setErrors(Map errors) {
		this.errors = errors;
	}
}
