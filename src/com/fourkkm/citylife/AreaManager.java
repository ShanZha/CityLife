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
	private static final int AREA_LEVEL_COUNTRY = 1;
	/** 省/州 **/
	private static final int AREA_LEVEL_CITY = 2;
	/** 地区/县 **/
	private static final int AREA_LEVEL_COUNTY = 3;

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
		List<String> all = new ArrayList<String>();
		all.add(GlobalConfig.FloatingStr.STR_ALL_AREA);
		map.put(GlobalConfig.FloatingStr.STR_ALL_AREA, all);
		for (int i = 0; i < mAreaList.size(); i++) {
			ModoerArea area = mAreaList.get(i);
			if (AREA_LEVEL_CITY == area.getLevel()) {
				map.put(area.getName(), this.getAreaChild(area.getId()));
			}
		}
		return map;
	}

	/**
	 * 获取某省/州的所有子Name列表
	 * 
	 * @param parentId
	 * @return
	 */
	private List<String> getAreaChild(int parentId) {
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
