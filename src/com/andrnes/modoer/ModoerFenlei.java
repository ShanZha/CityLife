package com.andrnes.modoer;

import com.zj.app.annotation.Lazy;

/** ������Ϣ�� */
public class ModoerFenlei {

	private int id;

	/** ����ID */
	@Lazy(isLazy = true)
	private ModoerFenlei catid;

	/** ����ID */
	private ModoerArea cityId;

	/** ����id */
	private ModoerArea aid;

	/** ������� */
	private ModoerSubject sid;

	/** ������� */
	private String subject;

	/** ����ͼ���� */
	private String thumb;

	/** �ύ��Ա */
	private ModoerMembers uid;

	/** �û��ǳ� */
	private String username;

	/** ״̬��1-������0-����ˣ� */
	private int status;

	/** ��ϵ�� */
	private String linkman;

	/** ��ϵ�˵绰 */
	private String contact;

	/** �������� */
	private String email;

	/** ���칤�� */
	private String im;

	/** ��ַ */
	private String address;

	/** ���� */
	private String content;

	/** �ύʱ�� */
	private int dateline;

	/** ����ʱ�� */
	private int endtime;

	/** ����� */
	private int pageview;

	/***/
	private int comments;

	/** ������ɫ */
	private String color;

	/** ������ɫ����ʱ�� */
	private int colorEndtime;

	/** �Ƿ��ö� */
	private int top;

	/** �ö�����ʱ�� */
	private int topEndtime;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public ModoerFenlei getCatid() {
		return catid;
	}

	public void setCatid(ModoerFenlei catid) {
		this.catid = catid;
	}

	public ModoerArea getCityId() {
		return cityId;
	}

	public void setCityId(ModoerArea cityId) {
		this.cityId = cityId;
	}

	public ModoerArea getAid() {
		return aid;
	}

	public void setAid(ModoerArea aid) {
		this.aid = aid;
	}

	public ModoerSubject getSid() {
		return sid;
	}

	public void setSid(ModoerSubject sid) {
		this.sid = sid;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getThumb() {
		return thumb;
	}

	public void setThumb(String thumb) {
		this.thumb = thumb;
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

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getLinkman() {
		return linkman;
	}

	public void setLinkman(String linkman) {
		this.linkman = linkman;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getIm() {
		return im;
	}

	public void setIm(String im) {
		this.im = im;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getDateline() {
		return dateline;
	}

	public void setDateline(int dateline) {
		this.dateline = dateline;
	}

	public int getEndtime() {
		return endtime;
	}

	public void setEndtime(int endtime) {
		this.endtime = endtime;
	}

	public int getPageview() {
		return pageview;
	}

	public void setPageview(int pageview) {
		this.pageview = pageview;
	}

	public int getComments() {
		return comments;
	}

	public void setComments(int comments) {
		this.comments = comments;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public int getColorEndtime() {
		return colorEndtime;
	}

	public void setColorEndtime(int colorEndtime) {
		this.colorEndtime = colorEndtime;
	}

	public int getTop() {
		return top;
	}

	public void setTop(int top) {
		this.top = top;
	}

	public int getTopEndtime() {
		return topEndtime;
	}

	public void setTopEndtime(int topEndtime) {
		this.topEndtime = topEndtime;
	}
}
