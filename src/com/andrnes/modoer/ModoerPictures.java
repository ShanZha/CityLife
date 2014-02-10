package com.andrnes.modoer;

import android.content.Context;
import android.view.ViewGroup;

import com.fourkkm.citylife.R;
import com.zj.app.annotation.Lazy;
import com.zj.app.annotation.Validates;
import com.zj.support.widget.item.ItemSingle;
import com.zj.support.widget.itemview.ItemView;

/** 主题图片表 */
public class ModoerPictures extends ItemSingle {

	private int id;
	/** 相册id */
	private ModoerAlbum albumid;

	/** 城市id */
	private ModoerArea cityId;

	/** 主题id */
	@Lazy(isLazy = true)
	private ModoerSubject sid;

	/** 上传用户id */
	private ModoerMembers uid;

	/** 用户名称 */
	@Validates(info = "用户名不能为空,用户名长度不能超过16个字符", type = "isNull,maxSize16")
	private String username;

	/** 标题 */
	@Validates(info = "标题不能为空,标题长度不能超过60个字符", type = "isNull,maxSize60")
	private String title;

	/** 缩略图 */
	@Validates(info = "缩略图不能为空,缩略图长度不能超过255个字符", type = "isNull,maxSize255")
	private String thumb;

	/** 大图 */
	@Validates(info = "大图不能为空,大图长度不能超过255个字符", type = "isNull,maxSize255")
	private String filename;

	/** 图片宽度 */
	private int width;

	/** 图片高度 */
	private int height;

	/** 大小 */
	private int size;

	/** 评论数量 */
	private String comments;

	/** 链接地址 */
	private String url;

	/** 类型 */
	private int isSort;

	/** 浏览量 */
	private int browse;

	/** 添加时间 */
	private int addtime;

	/** 状态 */
	private int status;

	/** 原始缩略图-暂无用 **/
	private String oldThumb;

	/** 原始大图-暂无用 **/
	private String oldfilename;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public ModoerAlbum getAlbumid() {
		return albumid;
	}

	public void setAlbumid(ModoerAlbum albumid) {
		this.albumid = albumid;
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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getThumb() {
		return thumb;
	}

	public void setThumb(String thumb) {
		this.thumb = thumb;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getIsSort() {
		return isSort;
	}

	public void setIsSort(int isSort) {
		this.isSort = isSort;
	}

	public int getBrowse() {
		return browse;
	}

	public void setBrowse(int browse) {
		this.browse = browse;
	}

	public int getAddtime() {
		return addtime;
	}

	public void setAddtime(int addtime) {
		this.addtime = addtime;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getOldThumb() {
		return oldThumb;
	}

	public void setOldThumb(String oldThumb) {
		this.oldThumb = oldThumb;
	}

	public String getOldfilename() {
		return oldfilename;
	}

	public void setOldfilename(String oldfilename) {
		this.oldfilename = oldfilename;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "ModoerPictures[username = " + username + ",title = " + title
				+ ",thumb = " + thumb + ",size = " + size + ",width = " + width
				+ "height = " + height + " ,url = " + url + ",filename = "
				+ filename + "]";
	}

	@Override
	public ItemView newCell(Context context, ViewGroup root) {
		// TODO Auto-generated method stub
		return this.createCellFromXml(context, R.layout.upload_pic_item, root);
	}
}
