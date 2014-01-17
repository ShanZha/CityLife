package com.andrnes.modoer;

import java.util.Map;

import android.content.Context;
import android.view.ViewGroup;

import com.fourkkm.citylife.R;
import com.zj.app.annotation.Lazy;
import com.zj.app.annotation.Validates;
import com.zj.support.widget.item.ItemSingle;
import com.zj.support.widget.itemview.ItemView;

/** 主题表 **/
public class ModoerSubject extends ItemSingle implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private int id;

	/** 个性目录或二级域名 */
	private String domain;

	/** 城市ID */
	@Lazy(isLazy = true)
	private ModoerArea cityId;

	/** 地区ID */
	@Lazy(isLazy = true)
	private ModoerArea aid;

	/** 主分类ID */
	@Lazy(isLazy = true)
	private ModoerCategory pid;

	/** 子分类ID */
	@Lazy(isLazy = true)
	private ModoerCategory catid;

	/** 图标属性一，（每张图以逗号（,）分割） **/
	private String cShopattsReplace;

	/** 图标属性二，（每张图以逗号（,）分割） **/
	private String cShopatts2Replace;

	/** 名称 */
	@Validates(info = "店铺名称不能为空,店铺名称长度不能超过60个字符", type = "isNull,maxSize60")
	private String name;

	/** 子名称 */
	private String subname;

	/** 平均分 */
	private double avgsort;

	/** 平分1 */
	private double sort1;

	/** 平分2 */
	private double sort2;

	/** 平分3 */
	private double sort3;

	/** 平分4 */
	private double sort4;

	/** 平分5 */
	private double sort5;

	/** 平分6 */
	private double sort6;

	/** 平分7 */
	private double sort7;

	/** 平分8 */
	private double sort8;

	/** 平均消费 */
	private int avgprice;

	/** 是否喜欢 */
	private int best;

	/** 点评数 */
	private int reviews;

	/** 留言数量 */
	private int guestbooks;

	/** 图片数量 */
	private int pictures;

	/** 浏览数量 */
	private int pageviews;

	/** 推荐度 */
	private int finer;

	/** 等级 */
	private int level;

	/** 管理员昵称 */
	private String owner;

	/** 创建者UID */
	@Lazy(isLazy = true)
	private ModoerMembers cuid;

	/** 创建者昵称 */
	@Validates(info = "创建者昵称不能为空,创建者昵称长度不能超过20个字符", type = "isNull,maxSize20")
	private String creator;

	/** 创建时间 */
	private int addtime;

	/** 缩略图 */
	@Validates(info = "缩略图不能为空,缩略图长度不能超过255个字符", type = "isNull,maxSize255")
	private String thumb;

	/** 状态 */
	private int status;

	/** 地图坐标 */
	private double mapLng;

	/** 地图坐标 */
	private double mapLat;

	/** 简介 */
	private String description;

	/** 地址 */
	private String address;

	/** 电话 */
	private String tel;

	private Map errors;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
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

	public ModoerCategory getPid() {
		return pid;
	}

	public void setPid(ModoerCategory pid) {
		this.pid = pid;
	}

	public ModoerCategory getCatid() {
		return catid;
	}

	public void setCatid(ModoerCategory catid) {
		this.catid = catid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSubname() {
		return subname;
	}

	public void setSubname(String subname) {
		this.subname = subname;
	}

	public double getAvgsort() {
		return avgsort;
	}

	public void setAvgsort(double avgsort) {
		this.avgsort = avgsort;
	}

	public double getSort1() {
		return sort1;
	}

	public void setSort1(double sort1) {
		this.sort1 = sort1;
	}

	public double getSort2() {
		return sort2;
	}

	public void setSort2(double sort2) {
		this.sort2 = sort2;
	}

	public double getSort3() {
		return sort3;
	}

	public void setSort3(double sort3) {
		this.sort3 = sort3;
	}

	public double getSort4() {
		return sort4;
	}

	public void setSort4(double sort4) {
		this.sort4 = sort4;
	}

	public double getSort5() {
		return sort5;
	}

	public void setSort5(double sort5) {
		this.sort5 = sort5;
	}

	public double getSort6() {
		return sort6;
	}

	public void setSort6(double sort6) {
		this.sort6 = sort6;
	}

	public double getSort7() {
		return sort7;
	}

	public void setSort7(double sort7) {
		this.sort7 = sort7;
	}

	public double getSort8() {
		return sort8;
	}

	public void setSort8(double sort8) {
		this.sort8 = sort8;
	}

	public int getAvgprice() {
		return avgprice;
	}

	public void setAvgprice(int avgprice) {
		this.avgprice = avgprice;
	}

	public int getBest() {
		return best;
	}

	public void setBest(int best) {
		this.best = best;
	}

	public int getReviews() {
		return reviews;
	}

	public void setReviews(int reviews) {
		this.reviews = reviews;
	}

	public int getGuestbooks() {
		return guestbooks;
	}

	public void setGuestbooks(int guestbooks) {
		this.guestbooks = guestbooks;
	}

	public int getPictures() {
		return pictures;
	}

	public void setPictures(int pictures) {
		this.pictures = pictures;
	}

	public int getPageviews() {
		return pageviews;
	}

	public void setPageviews(int pageviews) {
		this.pageviews = pageviews;
	}

	public int getFiner() {
		return finer;
	}

	public void setFiner(int finer) {
		this.finer = finer;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public ModoerMembers getCuid() {
		return cuid;
	}

	public void setCuid(ModoerMembers cuid) {
		this.cuid = cuid;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public int getAddtime() {
		return addtime;
	}

	public void setAddtime(int addtime) {
		this.addtime = addtime;
	}

	public String getThumb() {
		return thumb;
	}

	public void setThumb(String thumb) {
		this.thumb = thumb;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public double getMapLng() {
		return mapLng;
	}

	public void setMapLng(double mapLng) {
		this.mapLng = mapLng;
	}

	public double getMapLat() {
		return mapLat;
	}

	public void setMapLat(double mapLat) {
		this.mapLat = mapLat;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public Map getErrors() {
		return errors;
	}

	public void setErrors(Map errors) {
		this.errors = errors;
	}

	public String getCShopattsReplace() {
		return cShopattsReplace;
	}

	public void setCShopattsReplace(String cShopattsReplace) {
		this.cShopattsReplace = cShopattsReplace;
	}

	public String getCShopatts2Replace() {
		return cShopatts2Replace;
	}

	public void setCShopatts2Replace(String cShopatts2Replace) {
		this.cShopatts2Replace = cShopatts2Replace;
	}

	@Override
	public ItemView newCell(Context context, ViewGroup root) {
		// TODO Auto-generated method stub
		return this
				.createCellFromXml(context, R.layout.subject_list_item, root);
	}
}
