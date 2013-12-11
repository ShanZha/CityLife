package com.fourkkm.citylife.constant;

public interface GlobalConfig {

	// public final static String URL_CONN =
	// "http://www.huaqianjv.com/modoer/androidInterFace/";
	public final static String URL_CONN = "http://115.28.167.199/modoer/androidInterFace/";
	public static final String URL_PIC = "http://www.40000km.com.cn/";
	public static final int MAX = 10;
	public static final int MAX_ALL = 99999999;

	public static final int CATEGORY_FOOD = 1;// 美食
	public static final int CATEGORY_SHOPPING = 29;// 生活购物
	public static final int CATEGORY_RECREATION = 44;// 休闲娱乐
	public static final int CATEGORY_TRAVEL = 61;// 汽车出行

	public static final String SINA_WEIBO_APP_KEY = "3135167460";
	public static final String SINA_WEIBO_APP_SECRET = "671e00bc3b8ff89efaa74e57733b21a7";
	public static final String SINA_WEIBO_REDIRECT_URL = "http://www.baidu.com";
	public static final String SINA_WEIBO_SCOPE = "email,direct_messages_read,direct_messages_write,"
			+ "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
			+ "follow_app_official_microblog," + "invitation_write";

	public static final String TENCENT_QQ_APP_ID = "1101125359";
	// public static final String TENCENT_QQ_APP_KEY = "3ZchWagCse1ppGRe";
	public static final String TENCENT_QQ_SCOPE = "all";
	// public static final String TENCENT_QQ_SCOPE =
	// "get_user_info;upload_pic;get_info;add_t;add_pic_t;add_one_blog";

	public static final String TENCENT_WEIBO_APP_KEY = "801455412";
	public static final String TENCENT_WEIBO_APP_SECRET = "0e84a41d4f77ba9d14d33e461f9bee17";
	public static final String TENCENT_WEIBO_REDIRECT_URL = "http://www.baidu.com";

	public static final String TAOBAO_APP_KEY = "21701340";
	public static final String TAOBAO_APP_SECRET = "eea04b5e585b877f770213a80508fc47";
	public static final String TAOBAO_REDIRECT_URL = "com.fourkkm.citylife://authresult";

	public static class FloatingType {
		public static final int TYPE_SUBJECT_DISTANCE = 0;
		public static final int TYPE_SUBJECT_CATEGORY = 1;
		public static final int TYPE_SUBJECT_SORT = 2;
		public static final int TYPE_AREA = 3;
		public static final int TYPE_ASK_CATEGORY = 4;
		public static final int TYPE_ASK_STATE = 5;
		public static final int TYPE_PARTY_CATEGORY = 6;
		public static final int TYPE_PARTY_STATE = 7;
		public static final int TYPE_PARTY_MOST = 8;
		public static final int TYPE_CHINA_LANE_CATEGORY = 9;
		public static final int TYPE_CHINA_LANE_AREA = 10;
		public static final int TYPE_SUBJECT_SEARCH_CITY = 11;
	}

	public static class FloatingStr {
		public static final String STR_ALL_AREA = "全部地区";
		public static final String STR_ALL_ASK = "全部问题";
		public static final String STR_ALL_CATEGOTY = "全部类别";
		public static final String STR_DEFAULT_SORT = "默认排序";

	}

	public static class Operator {
		public static final int OPERATION_FINDALL_BCASTR = 0;
		public static final int OPERATION_FINDALL_AREA = 1;
		public static final int OPERATION_FINDALL_ASK_CATEGORY = 2;
		public static final int OPERATION_FINDALL_ASK = 3;
		public static final int OPERATION_FINDALL_SUJECT_CATEGORY = 4;
		public static final int OPERATION_FINDALL_SUBJECT = 5;
		public static final int OPERATION_FINDALL_PARTY_CATEGORY = 6;
		public static final int OPERATION_FINDALL_PARTY = 7;
		public static final int OPERATION_FINDALL_CHINA_LANE_CATEGORY = 8;
		public static final int OPERATION_FINDALL_CHINA_LANE = 9;
		public static final int OPERATION_FINDALL_CHINA_AREA = 10;
		public static final int OPERATION_FINDALL_SUBJECT_AREA = 11;
		public static final int OPERATION_SAVE_ASK = 12;
		public static final int OPERATION_SUBJECT_CONNLECTION = 13;
	}

	public static class SharePre {
		public static final String KEY_USERNAME = "UserName";
		public static final String KEY_PSWD = "Pswd";
		public static final String KEY_IS_REMBER_PSWD = "isRemberPswd";
		public static final String KEY_IS_AUTO_LOGIN = "isAutoLogin";
	}
}
