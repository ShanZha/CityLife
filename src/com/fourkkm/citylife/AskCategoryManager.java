package com.fourkkm.citylife;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.andrnes.modoer.ModoerAskCategory;
import com.fourkkm.citylife.constant.GlobalConfig;

/**
 * 问题类别管家
 * 
 * @author ShanZha
 * 
 */
public class AskCategoryManager implements IModoerManager<ModoerAskCategory> {

	/** 常量定义见实体类ModoerAskCategory use_area字段 **/
	/** 父级类别 **/
	private static final int ASK_CATEGORY_LEVEL_PARENT = 0;
	/** 子级类别 **/
	private static final int ASK_CATEGORY_LEVEL_CHILD = 1;
	private List<ModoerAskCategory> mAskCategoryList;

	public AskCategoryManager() {
		mAskCategoryList = new ArrayList<ModoerAskCategory>();
	}

	/**
	 * 建立问答类别父子名称关系
	 * 
	 * @return
	 */
	public Map<String, List<String>> buildAskCategoryRelation() {
		Map<String, List<String>> map = new HashMap<String, List<String>>();
		if (null == mAskCategoryList) {
			return null;
		}
		// 加上“所有类型”项
		List<String> all = new ArrayList<String>();
		all.add(GlobalConfig.FloatingStr.STR_ALL_CATEGOTY);
		map.put(GlobalConfig.FloatingStr.STR_ALL_CATEGOTY, all);
		for (int i = 0; i < mAskCategoryList.size(); i++) {
			ModoerAskCategory ask = mAskCategoryList.get(i);
			if (ASK_CATEGORY_LEVEL_PARENT == ask.getUse_area()) {
				map.put(ask.getName(), this.getAskCategoryChild(ask.getId()));
			}
		}

		return map;
	}

	/**
	 * 获取某类别的所有子Name列表
	 * 
	 * @param parentId
	 * @return
	 */
	public List<String> getAskCategoryChild(int parentId) {
		if (null == mAskCategoryList) {
			return null;
		}
		List<String> childNames = new ArrayList<String>();
		for (int i = 0; i < mAskCategoryList.size(); i++) {
			ModoerAskCategory ask = mAskCategoryList.get(i);
			if (ASK_CATEGORY_LEVEL_CHILD == ask.getUse_area()) {
				ModoerAskCategory parent = ask.getPid();
				if (null != parent && parentId == parent.getId()) {
					childNames.add(ask.getName());
				}
			}
		}
		return childNames;
	}

	/**
	 * 以Name为Key找到一个ModoerCategory
	 * 
	 * @param name
	 * @return
	 */
	public ModoerAskCategory getAskCategoryByName(String name) {
		if (null == mAskCategoryList) {
			return null;
		}
		for (int i = 0; i < mAskCategoryList.size(); i++) {
			ModoerAskCategory ask = mAskCategoryList.get(i);
			if (name.equals(ask.getName())) {
				return ask;
			}
		}
		return null;
	}

	/**
	 * 获取第一级的所有列表名称
	 * 
	 * @return
	 */
	public List<String> getParentNameList() {
		List<String> names = new ArrayList<String>();
		if (null == mAskCategoryList) {
			return null;
		}
		for (int i = 0; i < mAskCategoryList.size(); i++) {
			ModoerAskCategory category = mAskCategoryList.get(i);
			if (ASK_CATEGORY_LEVEL_PARENT == category.getUse_area()) {
				names.add(category.getName());
			}
		}
		return names;
	}

	public boolean isFirstLevelCategory(int level) {
		return ASK_CATEGORY_LEVEL_PARENT == level;
	}

	@Override
	public void add(ModoerAskCategory e) {
		// TODO Auto-generated method stub
		mAskCategoryList.add(e);
	}

	@Override
	public void remove(ModoerAskCategory e) {
		// TODO Auto-generated method stub
		try {
			mAskCategoryList.remove(mAskCategoryList.indexOf(e));
		} catch (Exception ex) {
		}
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
		if (null != mAskCategoryList) {
			mAskCategoryList.clear();
		}
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		this.clear();
		mAskCategoryList = null;
	}
}
