package com.andrnes.modoer;

import com.zj.app.annotation.Lazy;

/** 相册表 */
public class ModoerAlbum {

	private int id;
	/** 城市ID */
	@Lazy(isLazy = true)
	private ModoerArea cityId;

	/** 主题ID */
	@Lazy(isLazy = true)
	private ModoerSubject sid;

	/** 名称 */
	private String name;

	/** 封面 */
	private String thumb;

	/** 描述 */
	private String des;

	/** 最后更新时间Unix时间戳 */
	private int lastupdate;

	/** 图片数量 */
	private int num;

	/** 浏览量 */
	private int pageview;

	/** 评论数 */
	private int comments;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public ModoerArea getCityId() {
		return cityId;
	}

	public void setCityId(ModoerArea cityId) {
		this.cityId = cityId;
	}

	public ModoerSubject getSid() {
		return sid;
	}

	public void setSid(ModoerSubject sid) {
		this.sid = sid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getThumb() {
		return thumb;
	}

	public void setThumb(String thumb) {
		this.thumb = thumb;
	}

	public String getDes() {
		return des;
	}

	public void setDes(String des) {
		this.des = des;
	}

	public int getLastupdate() {
		return lastupdate;
	}

	public void setLastupdate(int lastupdate) {
		this.lastupdate = lastupdate;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
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
}
