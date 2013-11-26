package com.andrnes.modoer;

import android.content.Context;
import android.view.ViewGroup;

import com.fourkkm.citylife.R;
import com.zj.app.annotation.Lazy;
import com.zj.support.widget.item.ItemSingle;
import com.zj.support.widget.itemview.ItemView;

/** ͼƬ�ֻ��� */
public class ModoerBcastr extends ItemSingle {

	private int id;
	/** �����ϼ�ID��0Ϊһ�����������У� */
	@Lazy(isLazy = true)
	private ModoerArea city;

	/** ���� */
	private String groupname;

	/** �Ƿ���ʾ */
	private int available;

	/** ���� */
	private String itemtitle;

	/** ���� */
	private String itemUrl;

	/** ͼƬ���� */
	private String link;

	/** ���� */
	private int orders = 0;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public ModoerArea getCity() {
		return city;
	}

	public void setCity(ModoerArea city) {
		this.city = city;
	}

	public String getGroupname() {
		return groupname;
	}

	public void setGroupname(String groupname) {
		this.groupname = groupname;
	}

	public int getAvailable() {
		return available;
	}

	public void setAvailable(int available) {
		this.available = available;
	}

	public String getItemtitle() {
		return itemtitle;
	}

	public void setItemtitle(String itemtitle) {
		this.itemtitle = itemtitle;
	}

	public String getItemUrl() {
		return itemUrl;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public void setItemUrl(String itemUrl) {
		this.itemUrl = itemUrl;
	}

	public int getOrders() {
		return orders;
	}

	public void setOrders(int orders) {
		this.orders = orders;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "ModoerBcastr[itemtitle = " + itemtitle + ",itemUrl = "
				+ itemUrl + ",link = " + link + ",available = " + available
				+ "]";
	}

	@Override
	public ItemView newCell(Context context, ViewGroup root) {
		// TODO Auto-generated method stub
		return this
				.createCellFromXml(context, R.layout.main_gallery_item, root);
	}

}
