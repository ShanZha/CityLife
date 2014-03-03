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

/** 问答 **/
public class ModoerAsks extends ItemSingle implements Serializable {

	private static final long serialVersionUID = 1L;
	private int id;
	/** 所属分类 */
	@Lazy(isLazy = true)
	private ModoerAskCategory catid;

	/** 所属城市 */
	@Lazy(isLazy = true)
	private ModoerArea cityId;

	/** 所属用户 */
	@Lazy(isLazy = true)
	private ModoerMembers uid;

	/** 用户昵称 */
	@Validates(info = "用户昵称不能为空,用户昵称长度不能超过200个字符", type = "isNull,maxSize200")
	private String author;

	/** 标题 */
	@Validates(info = "标题不能为空,标题长度不能超过80个字符", type = "isNull,maxSize80")
	private String subject;
	/** 关键字 */
	@Validates(info = "关键字不能为空,关键字长度不能超过100个字符", type = "isNull,maxSize100")
	private String keywords;
	/** 悬赏积分 */
	private int reward;
	/** 提问时间 */
	private int dateline;
	/** 过期时间 */
	private int expiredtime;
	/** 解决时间 */
	private int solvetime;
	/** 最佳答案 */
	@Lazy(isLazy = true)
	private ModoerAskAnswer bestanswer;
	/** 状态（0待解决1 已解决 ） */
	private int success;
	/** 状态（0申请中 1 已审核 ） */
	private int status;

	/** 排序 */
	private int listorder = 0;

	/** 浏览数 */
	private int views;
	/** 回复数 */
	private int replies;
	/** 问题内容 */
	@Validates(info = "问题内容不能为空,问题内容长度不能超过255个字符", type = "isNull,maxSize255")
	private String content;
	/** 问题扩展 */
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
