package com.andrnes.modoer;

import java.io.Serializable;
import java.math.BigDecimal;

import android.content.Context;
import android.view.ViewGroup;

import com.fourkkm.citylife.R;
import com.zj.app.annotation.Lazy;
import com.zj.support.widget.item.ItemSingle;
import com.zj.support.widget.itemview.ItemView;

public class ModoerParty extends ItemSingle implements Serializable{

	private static final long serialVersionUID = 1L;
	private int id;
	/** ����ID */
	@Lazy(isLazy = true)
	private ModoerPartyCategory catid;

	/** ����ID */
	private ModoerArea cityId;

	/** ����id */
	private ModoerArea aid;

	/** �ύ��Ա */
	@Lazy(isLazy = true)
	private ModoerMembers uid;

	/** ������� */
	private String subject;

	/** ����ͼ���� */
	private String thumb;

	/** �û��ǳ� */
	private String username;

	/***/
	private int flag;
	/***/
	private int finer;
	/** ��������ʱ�� */
	private int joinendtime;
	/** ��ʼʱ�� */
	private int begintime;
	/** �ӻ����� */
	private int plannum;

	/** �Ա�����(0-�Ա��ޣ�1��Ů�ԣ�2������) */
	private int sex;
	/** �������� */
	private String age;
	/** Ԥ�Ʒ��� */
	private int price;

	/** ������Ҫ���ֻ�rmb */
	private String applypriceType;

	/** �������� */
	private BigDecimal applyprice;
	/** ����� */
	private String des;

	/** ״̬��1-������0-����ˣ� */
	private int status;

	/** ��ϵ�� */
	private String linkman;

	/** ��ϵ�˵绰 */
	private String contact;

	/** �μ����� */
	private int applynum;

	/** ��ַ */
	private String address;

	/** �ύʱ�� */
	private int dateline;

	/** ����ʱ�� */
	private int endtime;

	/** ����� */
	private int pageview;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public ModoerPartyCategory getCatid() {
		return catid;
	}

	public void setCatid(ModoerPartyCategory catid) {
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

	public ModoerMembers getUid() {
		return uid;
	}

	public void setUid(ModoerMembers uid) {
		this.uid = uid;
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

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	public int getFiner() {
		return finer;
	}

	public void setFiner(int finer) {
		this.finer = finer;
	}

	public int getJoinendtime() {
		return joinendtime;
	}

	public void setJoinendtime(int joinendtime) {
		this.joinendtime = joinendtime;
	}

	public int getBegintime() {
		return begintime;
	}

	public void setBegintime(int begintime) {
		this.begintime = begintime;
	}

	public int getPlannum() {
		return plannum;
	}

	public void setPlannum(int plannum) {
		this.plannum = plannum;
	}

	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public String getApplypriceType() {
		return applypriceType;
	}

	public void setApplypriceType(String applypriceType) {
		this.applypriceType = applypriceType;
	}

	public BigDecimal getApplyprice() {
		return applyprice;
	}

	public void setApplyprice(BigDecimal applyprice) {
		this.applyprice = applyprice;
	}

	public String getDes() {
		return des;
	}

	public void setDes(String des) {
		this.des = des;
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

	public int getApplynum() {
		return applynum;
	}

	public void setApplynum(int applynum) {
		this.applynum = applynum;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
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

	@Override
	public ItemView newCell(Context context, ViewGroup root) {
		// TODO Auto-generated method stub
		return this.createCellFromXml(context, R.layout.party_list_item, root);
	}
}
