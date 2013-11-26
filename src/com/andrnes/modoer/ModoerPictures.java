package com.andrnes.modoer;

import com.zj.app.annotation.Lazy;

/** ����ͼƬ�� */
public class ModoerPictures {

	private int id;
	/** ���id */
	private ModoerAlbum albumid;

	/** ����id */
	private ModoerArea cityId;

	/** ����id */
	@Lazy(isLazy = true)
	private ModoerSubject sid;

	/** �ϴ��û�id */
	private ModoerMembers uid;

	/** �û����� */
	private String username;

	/** ���� */
	private String title;

	/** ����ͼ */
	private String thumb;

	/** �ļ��� */
	private String filename;

	/** ͼƬ��� */
	private int width;

	/** ͼƬ�߶� */
	private int height;

	/** ��С */
	private int size;

	/** �������� */
	private String comments;

	/** ���ӵ�ַ */
	private String url;

	/** ���� */
	private int isSort;

	/** ����� */
	private int browse;

	/** ���ʱ�� */
	private int addtime;

	/** ״̬ */
	private int status;

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

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "ModoerPictures[username = " + username + ",title = " + title
				+ ",thumb = " + thumb + ",size = " + size + ",width = " + width
				+ "height = " + height + " ,url = " + url + ",filename = "
				+ filename + "]";
	}
}
