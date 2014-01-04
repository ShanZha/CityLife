package com.andrnes.modoer;

import android.content.Context;
import android.view.ViewGroup;

import com.fourkkm.citylife.R;
import com.zj.support.widget.item.ItemSingle;
import com.zj.support.widget.itemview.ItemView;

public class ModoerTag extends ItemSingle {

	private int id;
	private int parentId;
	private String name;
	private boolean isChecked;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getParentId() {
		return parentId;
	}

	public void setParentId(int parentId) {
		this.parentId = parentId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isChecked() {
		return isChecked;
	}

	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}

	@Override
	public ItemView newCell(Context context, ViewGroup root) {
		// TODO Auto-generated method stub
		return this.createCellFromXml(context, R.layout.review_tag_cb, root);
	}

}
