package com.fourkkm.citylife;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.andrnes.modoer.ModoerArea;
import com.fourkkm.citylife.constant.GlobalConfig;

/**
 * 地区管家
 * 
 * @author ShanZha
 * 
 */
public class AreaManager implements IModoerManager<ModoerArea> {

	/** 常量定义，见ModoerArea实体level **/
	/** 国家级 **/
	public static final int AREA_LEVEL_COUNTRY = 1;
	/** 省/州 **/
	public static final int AREA_LEVEL_CITY = 2;
	/** 地区/县 **/
	public static final int AREA_LEVEL_COUNTY = 3;

	private List<ModoerArea> mAreaList;

	public AreaManager() {
		mAreaList = new ArrayList<ModoerArea>();
	}

	/**
	 * 建立ModoerArea关系集
	 * 
	 * @return
	 */
	public Map<String, List<String>> buildAreaRelation() {
		Map<String, List<String>> map = new HashMap<String, List<String>>();
		if (null == mAreaList) {
			return map;
		}
		// 加上“所有地区”项
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
	 * 获取某国家下的所有子Name列表，即省/州列表
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
	 * 获取某省/州的所有子Name列表，即县级列表
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
	 * 根据地区名字获取ModoerArea
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
