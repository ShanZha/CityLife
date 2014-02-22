package com.fourkkm.citylife;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.andrnes.modoer.ModoerAskCategory;
import com.fourkkm.citylife.constant.GlobalConfig;

/**
 * �������ܼ�
 * 
 * @author ShanZha
 * 
 */
public class AskCategoryManager implements IModoerManager<ModoerAskCategory> {

	/** ���������ʵ����ModoerAskCategory use_area�ֶ� **/
	/** ������� **/
	private static final int ASK_CATEGORY_LEVEL_PARENT = 0;
	/** �Ӽ���� **/
	private static final int ASK_CATEGORY_LEVEL_CHILD = 1;
	private List<ModoerAskCategory> mAskCategoryList;

	public AskCategoryManager() {
		mAskCategoryList = new ArrayList<ModoerAskCategory>();
	}

	/**
	 * �����ʴ���������ƹ�ϵ
	 * 
	 * @return
	 */
	public Map<String, List<String>> buildAskCategoryRelation() {
		Map<String, List<String>> map = new HashMap<String, List<String>>();
		if (null == mAskCategoryList) {
			return null;
		}
		// ���ϡ��������͡���
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
	 * ��ȡĳ����������Name�б�
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
	 * ��NameΪKey�ҵ�һ��ModoerCategory
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
	 * ��ȡ��һ���������б�����
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
