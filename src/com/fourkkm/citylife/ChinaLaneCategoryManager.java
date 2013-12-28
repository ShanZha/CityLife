package com.fourkkm.citylife;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.andrnes.modoer.ModoerFenleiCategory;
import com.fourkkm.citylife.constant.GlobalConfig;

public class ChinaLaneCategoryManager implements
		IModoerManager<ModoerFenleiCategory> {

	private List<ModoerFenleiCategory> mChinaLaneCategoryList;

	public ChinaLaneCategoryManager() {
		mChinaLaneCategoryList = new ArrayList<ModoerFenleiCategory>();
	}
	
	/**
	 * �Ƿ��и������
	 * 
	 * @param category
	 * @return
	 */
	public boolean hasParent(ModoerFenleiCategory category) {
		if (null == category) {
			return false;
		}
		return category.getPid() != null && category.getPid().getId() != 0;
	}
	
	/**
	 * ������������������ƹ�ϵ
	 * 
	 * @return
	 */
	public Map<String, List<String>> buildLaneCategoryRelation() {
		Map<String, List<String>> map = new HashMap<String, List<String>>();
		if (null == mChinaLaneCategoryList) {
			return map;
		}
		for (int i = 0; i < mChinaLaneCategoryList.size(); i++) {
			ModoerFenleiCategory category = mChinaLaneCategoryList.get(i);
			if (this.hasParent(category)) {
				continue;
			}
			map.put(category.getName(),
					this.getLaneCategoryChild(category.getId()));
		}
		// �����롰ȫ�����ѡ�Ĭ��Ҳ�Ǵ���
		List<String> allCategoryList = new ArrayList<String>();
		allCategoryList.add(GlobalConfig.FloatingStr.STR_ALL_CATEGOTY);
		map.put(GlobalConfig.FloatingStr.STR_ALL_CATEGOTY, allCategoryList);
		return map;
	}

	/**
	 * ��ȡĳ����������Name�б�
	 * 
	 * @param parentId
	 * @return
	 */
	public List<String> getLaneCategoryChild(int parentId) {
		List<String> childNames = new ArrayList<String>();
		if (null == mChinaLaneCategoryList) {
			return childNames;
		}
		for (int i = 0; i < mChinaLaneCategoryList.size(); i++) {
			ModoerFenleiCategory category = mChinaLaneCategoryList.get(i);
			if (!this.hasParent(category)) {
				continue;
			}
			if (parentId == category.getPid().getId()) {
				childNames.add(category.getName());
			}
		}
		return childNames;
	}

	/**
	 * ���ݷ��������ҵ���Ӧ�����
	 * 
	 * @param name
	 * @return
	 */
	public ModoerFenleiCategory getLaneCategoryByName(String name) {
		if (null == mChinaLaneCategoryList) {
			return null;
		}
		for (int i = 0; i < mChinaLaneCategoryList.size(); i++) {
			ModoerFenleiCategory category = mChinaLaneCategoryList.get(i);
			if (name.equals(category.getName())) {
				return category;
			}
		}
		return null;
	}


	@Override
	public void add(ModoerFenleiCategory e) {
		// TODO Auto-generated method stub
		mChinaLaneCategoryList.add(e);
	}

	@Override
	public void remove(ModoerFenleiCategory e) {
		// TODO Auto-generated method stub
		try {
			mChinaLaneCategoryList.remove(mChinaLaneCategoryList.indexOf(e));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
		if (null != mChinaLaneCategoryList) {
			mChinaLaneCategoryList.clear();
		}
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		this.clear();
		mChinaLaneCategoryList = null;
	}

}
