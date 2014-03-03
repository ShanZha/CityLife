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

/** �ʴ� **/
public class ModoerAsks extends ItemSingle implements Serializable {

	private static final long serialVersionUID = 1L;
	private int id;
	/** �������� */
	@Lazy(isLazy = true)
	private ModoerAskCategory catid;

	/** �������� */
	@Lazy(isLazy = true)
	private ModoerArea cityId;

	/** �����û� */
	@Lazy(isLazy = true)
	private ModoerMembers uid;

	/** �û��ǳ� */
	@Validates(info = "�û��ǳƲ���Ϊ��,�û��ǳƳ��Ȳ��ܳ���200���ַ�", type = "isNull,maxSize200")
	private String author;

	/** ���� */
	@Validates(info = "���ⲻ��Ϊ��,���ⳤ�Ȳ��ܳ���80���ַ�", type = "isNull,maxSize80")
	private String subject;
	/** �ؼ��� */
	@Validates(info = "�ؼ��ֲ���Ϊ��,�ؼ��ֳ��Ȳ��ܳ���100���ַ�", type = "isNull,maxSize100")
	private String keywords;
	/** ���ͻ��� */
	private int reward;
	/** ����ʱ�� */
	private int dateline;
	/** ����ʱ�� */
	private int expiredtime;
	/** ���ʱ�� */
	private int solvetime;
	/** ��Ѵ� */
	@Lazy(isLazy = true)
	private ModoerAskAnswer bestanswer;
	/** ״̬��0�����1 �ѽ�� �� */
	private int success;
	/** ״̬��0������ 1 ����� �� */
	private int status;

	/** ���� */
	private int listorder = 0;

	/** ����� */
	private int views;
	/** �ظ��� */
	private int replies;
	/** �������� */
	@Validates(info = "�������ݲ���Ϊ��,�������ݳ��Ȳ��ܳ���255���ַ�", type = "isNull,maxSize255")
	private String content;
	/** ������չ */
	private String extra;

	private int oldreward;

	private Map errors;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public ModoerAskCategory getCatid() {
		return catid;
	}

	public void setCatid(ModoerAskCategory catid) {
		this.catid = catid;
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

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public int getReward() {
		return reward;
	}

	public void setReward(int reward) {
		this.reward = reward;
	}

	public int getDateline() {
		return dateline;
	}

	public void setDateline(int dateline) {
		this.dateline = dateline;
	}

	public int getExpiredtime() {
		return expiredtime;
	}

	public void setExpiredtime(int expiredtime) {
		this.expiredtime = expiredtime;
	}

	public int getSolvetime() {
		return solvetime;
	}

	public void setSolvetime(int solvetime) {
		this.solvetime = solvetime;
	}

	public ModoerAskAnswer getBestanswer() {
		return bestanswer;
	}

	public void setBestanswer(ModoerAskAnswer bestanswer) {
		this.bestanswer = bestanswer;
	}

	public int getSuccess() {
		return success;
	}

	public void setSuccess(int success) {
		this.success = success;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getListorder() {
		return listorder;
	}

	public void setListorder(int listorder) {
		this.listorder = listorder;
	}

	public int getViews() {
		return views;
	}

	public void setViews(int views) {
		this.views = views;
	}

	public int getReplies() {
		return replies;
	}

	public void setReplies(int replies) {
		this.replies = replies;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getExtra() {
		return extra;
	}

	public void setExtra(String extra) {
		this.extra = extra;
	}

	public int getOldreward() {
		return oldreward;
	}

	public void setOldreward(int oldreward) {
		this.oldreward = oldreward;
	}

	public Map getErrors() {
		return errors;
	}

	public void setErrors(Map errors) {
		this.errors = errors;
	}

	@Override
	public ItemView newCell(Context context, ViewGroup root) {
		// TODO Auto-generated method stub
		return this.createCellFromXml(context, R.layout.ask_list_item, root);
	}
}
