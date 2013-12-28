package com.andrnes.modoer;

import java.io.Serializable;
import java.util.Map;

import com.zj.app.annotation.Lazy;
import com.zj.app.annotation.Validates;

/** 问答 **/
public class ModoerAskAnswer implements Serializable {

	private static final long serialVersionUID = 1L;
	private int id;
	/** 所属分类 */
	@Lazy(isLazy = true)
	private ModoerAskCategory catid;

	/** 所属问答 */
	@Lazy(isLazy = true)
	private ModoerAsks askid;

	/** 所属用户 */
	@Lazy(isLazy = true)
	private ModoerMembers uid;

	/** 用户昵称 */
	@Validates(info = "用户昵称不能为空,用户昵称长度不能超过32个字符", type = "isNull,maxSize32")
	private String username;

	/** 好评 */
	private int goodrate;

	/** 差评 */
	private int badrate;

	/** 最佳回答的评论 */
	private String feel;

	/** 参考地址 */
	@Validates(info = "参考地址长度不能超过100个字符", type = "maxSize100")
	private String brief;

	/** ip */
	@Validates(info = "IP不能为空,IP长度不能超过15个字符", type = "isNull,maxSize15")
	private String ip;

	/** 回答时间 */
	private int dateline;

	/** 状态（0无效1 有效 ） */
	private int status;

	/** 问题内容 */
	@Validates(info = "问题内容不能为空,问题内容长度不能超过255个字符", type = "isNull,maxSize255")
	private String content;

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

	public ModoerAsks getAskid() {
		return askid;
	}

	public void setAskid(ModoerAsks askid) {
		this.askid = askid;
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

	public int getGoodrate() {
		return goodrate;
	}

	public void setGoodrate(int goodrate) {
		this.goodrate = goodrate;
	}

	public int getBadrate() {
		return badrate;
	}

	public void setBadrate(int badrate) {
		this.badrate = badrate;
	}

	public String getFeel() {
		return feel;
	}

	public void setFeel(String feel) {
		this.feel = feel;
	}

	public String getBrief() {
		return brief;
	}

	public void setBrief(String brief) {
		this.brief = brief;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getDateline() {
		return dateline;
	}

	public void setDateline(int dateline) {
		this.dateline = dateline;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Map getErrors() {
		return errors;
	}

	public void setErrors(Map errors) {
		this.errors = errors;
	}
}
