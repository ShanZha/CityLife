package com.andrnes.modoer;

import java.io.Serializable;
import java.util.Map;

import android.content.Context;
import android.view.ViewGroup;

import com.fourkkm.citylife.R;
import com.zj.app.annotation.Lazy;
import com.zj.app.annotation.Validates;
import com.zj.support.widget.item.ItemSingle;
import com.zj.support.widget.itemview.ItemView;

/** ������Ϣ�� */
public class ModoerFenlei extends ItemSingle implements Serializable {

	private static final long serialVersionUID = 1L;

	private int id;

	/** ����ID */
	@Lazy(isLazy = true)
	private ModoerFenleiCategory catid;

	/** ����ID */
	@Lazy(isLazy = true)
	private ModoerArea cityId;

	/** ����id */
	@Lazy(isLazy = true)
	private ModoerArea aid;

	/** ������� */
	private ModoerSubject sid;

	/** ������� */
	@Validates(info = "���ⲻ��Ϊ��,���ⳤ�Ȳ��ܳ���60���ַ�", type = "isNull,maxSize60")
	private String subject;

	/** ����ͼ���� */
	@Validates(info = "����ͼ����Ϊ��,����ͼ���Ȳ��ܳ���255���ַ�", type = "isNull,maxSize255")
	private String thumb;

	/** �ύ��Ա */
	@Lazy(isLazy = true)
	private ModoerMembers uid;

	/** �û��ǳ� */
	@Validates(info = "�û��ǳƲ���Ϊ��,�û��ǳƲ��ܳ���16���ַ�", type = "isNull,maxSize16")
	private String username;

	/** ״̬��1-������0-����ˣ� */
	private int status;

	/** ��ϵ�� */
	@Validates(info = "��ϵ�˲���Ϊ��,��ϵ�˳��Ȳ��ܳ���20���ַ�", type = "isNull,maxSize20")
	private String linkman;

	/** ��ϵ�˵绰 */
	@Validates(info = "��ϵ��ʽ����Ϊ��,��ϵ��ʽ���ܳ���100���ַ�", type = "isNull,maxSize100")
	private String contact;

	/** �������� */
	private String email;

	/** ���칤�� */
	private String im;

	/** ��ַ */
	private String address;

	/** ���� */
	@Validates(info = "���ݲ���Ϊ��,���ݲ��ܳ���255���ַ�", type = "isNull,maxSize255")
	private String content;

	/** �ύʱ�� */
	private int dateline;

	/** ����ʱ�� */
	private int endtime;

	/** ����� */
	private int pageview;

	/** ��ע **/
	private int comments;

	/** ������ɫ */
	private String color;

	/** ������ɫ����ʱ�� */
	private int colorEndtime;

	/** �Ƿ��ö� */
	private int top;

	/** �ö�����ʱ�� */
	private int topEndtime;

	/** ͼƬ���� */
	private String pictures;

	/** ͼƬ����json */
	private String picturesJson;

	/** ͼƬ���� */
	private int picNum;

	private Map errors;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public ModoerFenleiCategory getCatid() {
		return catid;
	}

	public void setCatid(ModoerFenleiCategory catid) {
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

	public Map getErrors() {
		return errors;
	}

	public void setErrors(Map errors) {
		this.errors = errors;
	}

	public String getPictures() {
		return pictures;
	}

	public void setPictures(String pictures) {
		this.pictures = pictures;
	}

	public String getPicturesJson() {
		return picturesJson;
	}

	public void setPicturesJson(String picturesJson) {
		this.picturesJson = picturesJson;
	}

	public int getPicNum() {
		return picNum;
	}

	public void setPicNum(int picNum) {
		this.picNum = picNum;
	}

	@Override
	public ItemView newCell(Context context, ViewGroup root) {
		// TODO Auto-generated method stub
		return this.createCellFromXml(context, R.layout.china_lane_list_item,
				root);
	}
}
