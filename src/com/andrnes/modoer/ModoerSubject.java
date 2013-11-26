package com.andrnes.modoer;

import java.math.BigDecimal;

import android.content.Context;
import android.view.ViewGroup;

import com.fourkkm.citylife.R;
import com.zj.app.annotation.Lazy;
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

	/** 名称 */
	private String name;

	/** 子名称 */
	private String subname;

	/** 平均分 */
	private BigDecimal avgsort;

	/** 平分1 */
	private BigDecimal sort1;

	/** 平分2 */
	private BigDecimal sort2;

	/** 平分3 */
	private BigDecimal sort3;

	/** 平分4 */
	private BigDecimal sort4;

	/** 平分5 */
	private BigDecimal sort5;

	/** 平分6 */
	private BigDecimal sort6;

	/** 平分7 */
	private BigDecimal sort7;

	/** 平分8 */
	private BigDecimal sort8;

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
	private String creator;

	/** 创建时间 */
	private int addtime;

	/** 缩略图 */
	private String thumb;

	/** 状态 */
	private int status;

	/** 地图坐标 */
	private BigDecimal mapLng;

	/** 地图坐标 */
	private BigDecimal mapLat;

	/** 简介 */
	private String description;

	/** 地址 */
	private String address;

	/** 电话 */
	private String tel;

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

	public BigDecimal getAvgsort() {
		return avgsort;
	}

	public void setAvgsort(BigDecimal avgsort) {
		this.avgsort = avgsort;
	}

	public BigDecimal getSort1() {
		return sort1;
	}

	public void setSort1(BigDecimal sort1) {
		this.sort1 = sort1;
	}

	public BigDecimal getSort2() {
		return sort2;
	}

	public void setSort2(BigDecimal sort2) {
		this.sort2 = sort2;
	}

	public BigDecimal getSort3() {
		return sort3;
	}

	public void setSort3(BigDecimal sort3) {
		this.sort3 = sort3;
	}

	public BigDecimal getSort4() {
		return sort4;
	}

	public void setSort4(BigDecimal sort4) {
		this.sort4 = sort4;
	}

	public BigDecimal getSort5() {
		return sort5;
	}

	public void setSort5(BigDecimal sort5) {
		this.sort5 = sort5;
	}

	public BigDecimal getSort6() {
		return sort6;
	}

	public void setSort6(BigDecimal sort6) {
		this.sort6 = sort6;
	}

	public BigDecimal getSort7() {
		return sort7;
	}

	public void setSort7(BigDecimal sort7) {
		this.sort7 = sort7;
	}

	public BigDecimal getSort8() {
		return sort8;
	}

	public void setSort8(BigDecimal sort8) {
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

	public BigDecimal getMapLng() {
		return mapLng;
	}

	public void setMapLng(BigDecimal mapLng) {
		this.mapLng = mapLng;
	}

	public BigDecimal getMapLat() {
		return mapLat;
	}

	public void setMapLat(BigDecimal mapLat) {
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

	@Override
	public ItemView newCell(Context context, ViewGroup root) {
		// TODO Auto-generated method stub
		return this.createCellFromXml(context, R.layout.subject_list_item, root);
	}
}
