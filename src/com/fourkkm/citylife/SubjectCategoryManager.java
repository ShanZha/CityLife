package com.fourkkm.citylife;

import java.util.ArrayList;
import java.util.List;

import com.andrnes.modoer.ModoerCategory;

/**
 * 主题分类管理
 * 
 * @author ShanZha
 * 
 */
public class SubjectCategoryManager implements IModoerManager<ModoerCategory> {

	/** 常量定义见实体ModoerCategory level字段 **/
	private static final int LEVEL_FIRST = 1;
	private static final int LEVEL_SECOND = 2;
	private static final int LEVEL_THIRD = 3;
	private List<ModoerCategory> mCategoryList;

	public SubjectCategoryManager() {
		mCategoryList = new ArrayList<ModoerCategory>();
	}

	public List<String> getFirstLevelNames() {
		List<String> names = new ArrayList<String>();
		for (int i = 0; i < mCategoryList.size(); i++) {
			ModoerCategory category = mCategoryList.get(i);
			if (LEVEL_FIRST == category.getLevel()) {
				names.add(category.getName());
			}
		}
		return names;
	}

	public List<String> getNamesByParentId(int parentId) {
		List<String> names = new ArrayList<String>();
		for (int i = 0; i < mCategoryList.size(); i++) {
			ModoerCategory category = mCategoryList.get(i);
			ModoerCategory parent = category.getPid();
			if (null == parent) {
				continue;
			}
			if (parentId == parent.getId()) {
				names.add(category.getName());
			}
		}
		return names;
	}

	public ModoerCategory getCategoryByName(String name) {
		for (int i = 0; i < mCategoryList.size(); i++) {
			ModoerCategory category = mCategoryList.get(i);
			if (name.equals(category.getName())) {
				return category;
			}
		}
		return null;
	}

	@Override
	public void add(ModoerCategory e) {
		// TODO Auto-generated method stub
		mCategoryList.add(e);
	}

	@Override
	public void remove(ModoerCategory e) {
		// TODO Auto-generated method stub
		try {
			mCategoryList.remove(mCategoryList.indexOf(e));
		} catch (Exception ex) {
		}
	}

	@Override
	public void clear() {
		if (null != mCategoryList) {
			mCategoryList.clear();
		}
	}

	@Override
	public void destroy() {
		this.clear();
		mCategoryList = null;
	}
}
