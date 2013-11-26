package com.andrnes.modoer;

import java.io.Serializable;
import java.math.BigDecimal;

import com.zj.app.annotation.Lazy;

/** �û��� **/
public class ModoerMembers implements Serializable {

	private static final long serialVersionUID = 1L;
	private int id;
	/** ��Ա��ID */
	@Lazy(isLazy = true)
	private ModoerUsergroups groupid;

	/** �û��� */
	private String email;

	/** �û����� */
	private String password;

	/** �û����� */
	private String username;

	/** �ֽ� */
	private BigDecimal rmb;

	/** ����Ϣ */
	private String newmsg;

	/** ע��ʱ�� */
	private int regdate;

	/** �ȼ��������� */
	private int subjects;

	/** �������� */
	private int reviews;

	/** �ظ����� */
	private int responds;

	/** �ϴ�ͼƬ���� */
	private int pictures;

	/** �յ��ʻ����� */
	private int flowers;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public ModoerUsergroups getGroupid() {
		return groupid;
	}

	public void setGroupid(ModoerUsergroups groupid) {
		this.groupid = groupid;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public BigDecimal getRmb() {
		return rmb;
	}

	public void setRmb(BigDecimal rmb) {
		this.rmb = rmb;
	}

	public String getNewmsg() {
		return newmsg;
	}

	public void setNewmsg(String newmsg) {
		this.newmsg = newmsg;
	}

	public int getRegdate() {
		return regdate;
	}

	public void setRegdate(int regdate) {
		this.regdate = regdate;
	}

	public int getSubjects() {
		return subjects;
	}

	public void setSubjects(int subjects) {
		this.subjects = subjects;
	}

	public int getReviews() {
		return reviews;
	}

	public void setReviews(int reviews) {
		this.reviews = reviews;
	}

	public int getResponds() {
		return responds;
	}

	public void setResponds(int responds) {
		this.responds = responds;
	}

	public int getPictures() {
		return pictures;
	}

	public void setPictures(int pictures) {
		this.pictures = pictures;
	}

	public int getFlowers() {
		return flowers;
	}

	public void setFlowers(int flowers) {
		this.flowers = flowers;
	}
}
