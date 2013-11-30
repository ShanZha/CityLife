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

	/** �ȼ����� **/
	private int point;

	/** �����ֶ�1 **/
	private int point1;

	/** �����ֶ�2 **/
	private int point2;

	/** �����ֶ�3 **/
	private int point3;

	/** �����ֶ�4 **/
	private int point4;

	/** �����ֶ�5 **/
	private int point5;

	/** �����ֶ�6 **/
	private int point6;

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

	public int getPoint() {
		return point;
	}

	public void setPoint(int point) {
		this.point = point;
	}

	public int getPoint1() {
		return point1;
	}

	public void setPoint1(int point1) {
		this.point1 = point1;
	}

	public int getPoint2() {
		return point2;
	}

	public void setPoint2(int point2) {
		this.point2 = point2;
	}

	public int getPoint3() {
		return point3;
	}

	public void setPoint3(int point3) {
		this.point3 = point3;
	}

	public int getPoint4() {
		return point4;
	}

	public void setPoint4(int point4) {
		this.point4 = point4;
	}

	public int getPoint5() {
		return point5;
	}

	public void setPoint5(int point5) {
		this.point5 = point5;
	}

	public int getPoint6() {
		return point6;
	}

	public void setPoint6(int point6) {
		this.point6 = point6;
	}
}
