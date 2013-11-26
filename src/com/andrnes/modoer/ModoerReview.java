package com.andrnes.modoer;

import java.io.Serializable;

import android.content.Context;
import android.view.ViewGroup;

import com.fourkkm.citylife.R;
import com.zj.app.annotation.Lazy;
import com.zj.support.widget.item.ItemSingle;
import com.zj.support.widget.itemview.ItemView;

/** ������ */
public class ModoerReview extends ItemSingle implements Serializable {

	private static final long serialVersionUID = 1L;
	private int id;
	/** ���������ʾ */
	private String idtype;

	/** ��������id */
	@Lazy(isLazy = true)
	private ModoerSubject sid;

	/** ����id */
	@Lazy(isLazy = true)
	private ModoerArea cityId;

	/** �����û� */
	@Lazy(isLazy = true)
	private ModoerMembers uid;

	/** �û��ǳ� */
	private String username;

	/** ״̬ 1:���� */
	private int status;

	/** �����������ID */
	private ModoerCategory pcatid;

	/** ������1 */
	private int sort1;

	/** ������2 */
	private int sort2;

	/** ������3 */
	private int sort3;

	/** ������4 */
	private int sort4;

	/** ������5 */
	private int sort5;

	/** ������6 */
	private int sort6;

	/** ������7 */
	private int sort7;

	/** ������8 */
	private int sort8;

	/** �����ֶ� */
	private int price;

	/** �Ƿ���� 2-���� 0-һ�� */
	private int best;

	/** ����ʱ�� */
	private int posttime;

	/** �������� */
	private String subject;

	/** �������� */
	private String title;

	/** �������� */
	private String content;

	/** ��ǩ�� */
	private String taggroup;

	/** ͼƬ�� */
	private String pictures;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getIdtype() {
		return idtype;
	}

	public void setIdtype(String idtype) {
		this.idtype = idtype;
	}

	public ModoerSubject getSid() {
		return sid;
	}

	public void setSid(ModoerSubject sid) {
		this.sid = sid;
	}

	public ModoerArea getCityId() {
		return cityId;
	}

	public void setCityId(ModoerArea cityId) {
		this.cityId = cityId;
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

	public ModoerCategory getPcatid() {
		return pcatid;
	}

	public void setPcatid(ModoerCategory pcatid) {
		this.pcatid = pcatid;
	}

	public int getSort1() {
		return sort1;
	}

	public void setSort1(int sort1) {
		this.sort1 = sort1;
	}

	public int getSort2() {
		return sort2;
	}

	public void setSort2(int sort2) {
		this.sort2 = sort2;
	}

	public int getSort3() {
		return sort3;
	}

	public void setSort3(int sort3) {
		this.sort3 = sort3;
	}

	public int getSort4() {
		return sort4;
	}

	public void setSort4(int sort4) {
		this.sort4 = sort4;
	}

	public int getSort5() {
		return sort5;
	}

	public void setSort5(int sort5) {
		this.sort5 = sort5;
	}

	public int getSort6() {
		return sort6;
	}

	public void setSort6(int sort6) {
		this.sort6 = sort6;
	}

	public int getSort7() {
		return sort7;
	}

	public void setSort7(int sort7) {
		this.sort7 = sort7;
	}

	public int getSort8() {
		return sort8;
	}

	public void setSort8(int sort8) {
		this.sort8 = sort8;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public int getBest() {
		return best;
	}

	public void setBest(int best) {
		this.best = best;
	}

	public int getPosttime() {
		return posttime;
	}

	public void setPosttime(int posttime) {
		this.posttime = posttime;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getTaggroup() {
		return taggroup;
	}

	public void setTaggroup(String taggroup) {
		this.taggroup = taggroup;
	}

	public String getPictures() {
		return pictures;
	}

	public void setPictures(String pictures) {
		this.pictures = pictures;
	}

	@Override
	public ItemView newCell(Context context, ViewGroup root) {
		// TODO Auto-generated method stub
		return this.createCellFromXml(context, R.layout.review_list_item, root);
	}

}
