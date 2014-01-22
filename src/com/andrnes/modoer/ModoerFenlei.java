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

/** 分类信息表 */
public class ModoerFenlei extends ItemSingle implements Serializable {

	private static final long serialVersionUID = 1L;

	private int id;

	/** 分类ID */
	@Lazy(isLazy = true)
	private ModoerFenleiCategory catid;

	/** 城市ID */
	@Lazy(isLazy = true)
	private ModoerArea cityId;

	/** 地区id */
	@Lazy(isLazy = true)
	private ModoerArea aid;

	/** 相关主题 */
	private ModoerSubject sid;

	/** 分类标题 */
	@Validates(info = "标题不能为空,标题长度不能超过60个字符", type = "isNull,maxSize60")
	private String subject;

	/** 缩略图连接 */
	@Validates(info = "缩略图不能为空,缩略图长度不能超过255个字符", type = "isNull,maxSize255")
	private String thumb;

	/** 提交会员 */
	@Lazy(isLazy = true)
	private ModoerMembers uid;

	/** 用户昵称 */
	@Validates(info = "用户昵称不能为空,用户昵称不能超过16个字符", type = "isNull,maxSize16")
	private String username;

	/** 状态（1-正常，0-待审核） */
	private int status;

	/** 联系人 */
	@Validates(info = "联系人不能为空,联系人长度不能超过20个字符", type = "isNull,maxSize20")
	private String linkman;

	/** 联系人电话 */
	@Validates(info = "联系方式不能为空,联系方式不能超过100个字符", type = "isNull,maxSize100")
	private String contact;

	/** 电子邮箱 */
	private String email;

	/** 聊天工具 */
	private String im;

	/** 地址 */
	private String address;

	/** 内容 */
	@Validates(info = "内容不能为空,内容不能超过255个字符", type = "isNull,maxSize255")
	private String content;

	/** 提交时间 */
	private int dateline;

	/** 过期时间 */
	private int endtime;

	/** 浏览量 */
	private int pageview;

	/** 备注 **/
	private int comments;

	/** 标题颜色 */
	private String color;

	/** 标题颜色结束时间 */
	private int colorEndtime;

	/** 是否置顶 */
	private int top;

	/** 置顶结束时间 */
	private int topEndtime;

	/** 图片集合 */
	private String pictures;

	/** 图片集合json */
	private String picturesJson;

	/** 图片数量 */
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
