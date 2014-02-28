package com.fourkkm.citylife;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.andrnes.modoer.ModoerArea;
import com.fourkkm.citylife.constant.GlobalConfig;

/**
 * �����ܼ�
 * 
 * @author ShanZha
 * 
 */
public class AreaManager implements IModoerManager<ModoerArea> {

	/** �������壬��ModoerAreaʵ��level **/
	/** ���Ҽ� **/
	public static final int AREA_LEVEL_COUNTRY = 1;
	/** ʡ/�� **/
	public static final int AREA_LEVEL_CITY = 2;
	/** ����/�� **/
	public static final int AREA_LEVEL_COUNTY = 3;

	private List<ModoerArea> mAreaList;

	public AreaManager() {
		mAreaList = new ArrayList<ModoerArea>();
	}

	/**
	 * ����ModoerArea��ϵ��
	 * 
	 * @return
	 */
	public Map<String, List<String>> buildAreaRelation() {
		Map<String, List<String>> map = new HashMap<String, List<String>>();
		if (null == mAreaList) {
			return map;
		}
		// ���ϡ����е�������
//		List<String> all = new ArrayList<String>();
//		all.add(GlobalConfig.FloatingStr.STR_ALL_AREA);
//		map.put(GlobalConfig.FloatingStr.STR_ALL_CHILD, all);
		for (int i = 0; i < mAreaList.size(); i++) {
			ModoerArea area = mAreaList.get(i);
			if (AREA_LEVEL_CITY == area.getLevel()) {
				map.put(area.getName(), this.getAreaCountyChild(area.getId()));
			}
		}
		return map;
	}

	/**
	 * ��ȡĳ�����µ�������Name�б���ʡ/���б�
	 * 
	 * @param parentId
	 * @return
	 */
	public List<String> getAreaCityChild(int parentId) {
		List<String> childNames = new ArrayList<String>();
		if (null == mAreaList) {
			return childNames;
		}
		for (int i = 0; i < mAreaList.size(); i++) {
			ModoerArea area = mAreaList.get(i);
			if (AREA_LEVEL_CITY == area.getLevel()) {
				ModoerArea parent = area.getPid();
				if (null != parent && parentId == parent.getId()) {
					childNames.add(area.getName());
				}
			}
		}
		return childNames;
	}

	/**
	 * ��ȡĳʡ/�ݵ�������Name�б����ؼ��б�
	 * 
	 * @param parentId
	 * @return
	 */
	public List<String> getAreaCountyChild(int parentId) {
		List<String> childNames = new ArrayList<String>();
		if (null == mAreaList) {
			return childNames;
		}
		for (int i = 0; i < mAreaList.size(); i++) {
			ModoerArea area = mAreaList.get(i);
			if (AREA_LEVEL_COUNTY == area.getLevel()) {
				ModoerArea parent = area.getPid();
				if (null != parent && parentId == parent.getId()) {
					childNames.add(area.getName());
				}
			}
		}
		return childNames;
	}

	/**
	 * ���ݵ������ֻ�ȡModoerArea
	 * 
	 * @param name
	 * @return
	 */
	public ModoerArea getSubjectAreaByName(String name) {
		if (null == mAreaList) {
			return null;
		}
		for (int i = 0; i < mAreaList.size(); i++) {
			ModoerArea area = mAreaList.get(i);
			if (name.equals(area.getName())) {
				return area;
			}
		}
		return null;
	}
	
	public boolean isSecondLevel(int level){
		return AREA_LEVEL_CITY == level;
	}
	
	public boolean isThirdLevel(int level){
		return AREA_LEVEL_COUNTY == level;
	}

	@Override
	public void add(ModoerArea e) {
		// TODO Auto-generated method stub
		mAreaList.add(e);
	}

	@Override
	public void remove(ModoerArea e) {
		// TODO Auto-generated method stub
		try {
			mAreaList.remove(mAreaList.indexOf(e));
		} catch (Exception ex) {
		}
	}

	@Override
	public void clear() {
		if (null != mAreaList) {
			mAreaList.clear();
		}
	}

	@Override
	public void destroy() {
		this.clear();
		mAreaList = null;
	}

}
