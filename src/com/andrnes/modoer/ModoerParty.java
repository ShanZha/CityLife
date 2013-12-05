package com.andrnes.modoer;

import java.io.Serializable;
import java.math.BigDecimal;

import android.content.Context;
import android.view.ViewGroup;

import com.fourkkm.citylife.R;
import com.zj.app.annotation.Lazy;
import com.zj.app.annotation.Validates;
import com.zj.support.widget.item.ItemSingle;
import com.zj.support.widget.itemview.ItemView;

public class ModoerParty extends ItemSingle implements Serializable{

	private static final long serialVersionUID = 1L;
	private int id;
	/** 分类ID */
	@Lazy(isLazy = true)
	private ModoerPartyCategory catid;

	/** 城市ID */
	private ModoerArea cityId;

	/** 地区id */
	private ModoerArea aid;

	/** 提交会员 */
	@Lazy(isLazy = true)
	private ModoerMembers uid;

	/** 分类标题 */
	@Validates(info = "主题不能为空,主题长度不能超过60个字符", type = "isNull,maxSize60")
	private String subject;

	/** 缩略图连接 */
	@Validates(info = "缩略图不能为空", type = "isNull")
	private String thumb;

	/** 用户昵称 */
	@Validates(info = "用户姓名不能为空,用户名长度不能超过16个字符", type = "isNull,maxSize16")
	private String username;

	/***/
	private int flag;
	/***/
	private int finer;
	/** 报名结束时间 */
	private int joinendtime;
	/** 开始时间 */
	private int begintime;
	/** 计划人数 */
	@Validates(info = "计划人数不能为空", type = "isNull")
	private int plannum;

	/** 性别限制(0-性别不限，1限女性，2限男性) */
	private int sex;
	/** 年龄限制 */
	private String age;
	/** 预计费用 */
	private int price;

	/** 报名需要积分或rmb */
	private String applypriceType;

	/** 报名费用 */
	private BigDecimal applyprice;
	/** 活动描述 */
	@Validates(info = "描述不能为空,描述长度不能超过255个字符", type = "isNull,maxSize255")
	private String des;

	/** 状态（1-正常，0-待审核） */
	private int status;

	/** 联系人 */
	@Validates(info = "联系人不能为空,联系人长度不能超过20个字符", type = "isNull,maxSize20")
	private String linkman;

	/** 联系人电话 */
	@Validates(info = "联系方式不能为空,联系方式长度不能超过100个字符", type = "isNull,maxSize100")
	private String contact;

	/** 参加人数 */
	private int applynum;

	/** 地址 */
	@Validates(info = "聚会地址不能为空,聚会地址长度不能超过255个字符", type = "isNull,maxSize255")
	private String address;

	/** 提交时间 */
	private int dateline;

	/** 过期时间 */
	private int endtime;

	/** 浏览量 */
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
