package com.fourkkm.citylife.constant;

public interface GlobalConfig {

	// public final static String URL_CONN =
	// "http://115.28.167.199/modoer/androidInterFace/";
	public final static String URL_CONN = "http://www.40000km.com.cn:8080/modoer/androidInterFace/";
	// public final static String URL_UPLOAD = "http://115.28.167.199/modoer/";
	public final static String URL_UPLOAD = "http://www.40000km.com.cn:8080/modoer/";
	public static final String URL_PIC = "http://www.40000km.com.cn/";
	public static final String URL_ATTR_PIC = URL_PIC
			+ "static/images/att/att/";
	public static final String URL_FORGET_PSWD = URL_PIC
			+ "member-login-op-forget.html";

	public static final int MAX = 15;
	public static final int MAX_ALL = 99999999;
	public static final int UPLOAD_PIC_MAX = 8;
	public static final String SEPERATOR = "/";

	public static final int CATEGORY_FOOD = 1;// 美食
	public static final int CATEGORY_SHOPPING = 29;// 生活购物
	public static final int CATEGORY_RECREATION = 44;// 休闲娱乐
	public static final int CATEGORY_TRAVEL = 61;// 汽车出行

	public static final int STATUS_UPLOAD_NONE = -1;
	public static final int STATUS_UPLOAD_START = 0;
	public static final int STATUS_UPLOAD_SUCCESS = 1;
	public static final int STATUS_UPLOAD_FAIL = 2;

	public static final int PUBLIC_MEMBER_ID = 50;

	public static class FloatingType {
		public static final int TYPE_SUBJECT_DISTANCE = 0;
		public static final int TYPE_CATEGORY = 1;
		public static final int TYPE_SUBJECT_SORT = 2;
		public static final int TYPE_AREA = 3;
		public static final int TYPE_ASK_STATE = 4;
		public static final int TYPE_ASK_CATEGORY = 5;
		public static final int TYPE_PARTY_STATE = 6;
		public static final int TYPE_PARTY_MOST = 7;
		public static final int TYPE_PIC_SELECT = 8;
		public static final int TYPE_SHARE = 9;
		public static final int TYPE_ERROR = 10;
		public static final int TYPE_PARTY_SIGNUP_END_TIME = 11;
		public static final int TYPE_PARTY_BEGIN_TIME = 12;
		public static final int TYPE_PARTY_END_TIME = 13;

	}

	public static class FloatingStr {
		public static final String STR_ALL_AREA = "全部地区";
		public static final String STR_ALL_ASK = "全部问题";
		public static final String STR_ALL_CATEGOTY = "全部类别";
		public static final String STR_SORT_NEAR = "按距离排序";
		public static final String STR_DEFAULT_SORT = "默认排序";
		public static final String STR_ALL_CHILD = "全部";

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
		public static final int OPERATION_SAVE_REVIEW = 14;
		public static final int OPERATION_UPLOAD_FILE = 15;
		public static final int OPERATION_UPLOAD_PIC_WITH_THUMB = 16;
		public static final int OPERATION_UPLOAD_THUMB = 17;
		public static final int OPERATION_SAVE_PARTY = 18;
		public static final int OPERATION_SAVE_CHINA_LANE = 19;
		public static final int OPERATION_SAVE_SUBJECT = 20;
		public static final int OPERATION_SAVE_ALBUM = 21;
		public static final int OPERATION_SAVE_PICS = 22;
		public static final int OPERATION_FINDALL_FAVORITE = 23;
		public static final int OPERATION_FINDALL_ASK_ANSWER = 24;
		public static final int OPERATION_SAVE_MEMBER = 25;
		public static final int OPERATION_FINDALL_REVIEW_OPT = 26;
		public static final int OPERATION_FINDALL_REVIEW_TAGS = 27;
		public static final int OPERATION_FIND_REVIEW_BY_SUBJECT = 28;
		public static final int OPERATION_FIND_ALBUM_BY_SUBJECT = 29;
		public static final int OPERATION_CLOSE_ASK = 30;
		public static final int OPERATION_UPLOAD_MEMBER_THUMB = 31;
		public static final int OPERATION_FIND_MEMBER = 32;
		public static final int OPERATION_FIND_MEMBERPASSPORT = 33;
		public static final int OPERATION_SAVE_ERROR = 34;
		public static final int OPERATION_DELETE_FAVORITE = 35;
		public static final int OPERATION_FIND_ALBUM = 36;
	}

	public static class SharePre {
		public static final String KEY_USERNAME = "UserName";
		public static final String KEY_PSWD = "Pswd";
		public static final String KEY_IS_REMBER_PSWD = "isRemberPswd";
		public static final String KEY_IS_AUTO_LOGIN = "isAutoLogin";
		public static final String KEY_MEMBER_ID = "memberId";

		public static final String KEY_CURR_AREA_ID = "AreaId";
		public static final String KEY_IS_WELCOME = "isWelcome";
		public static final String KEY_PUSH_TIME = "pushTime";
	}

	public static class IntentKey {
		public static final int INDEX_TENCENT_QZONE = 0;
		public static final int INDEX_TENCENT_WEIBO = 1;
		public static final int INDEX_TENCENT_QQ = 2;
		public static final int INDEX_SINA_WEIBO = 3;
		public static final int INDEX_WEIXIN_FRIENDS = 4;
		public static final int INDEX_WEIXIN_TIMELINE = 5;

		public static final int SUBJECT_SEACH_CITY = 1;
		public static final int SUBJECT_ME = 2;
		public static final int SUBJECT_FAVORITE = 3;
		public static final int SUBJECT_NEAR = 4;

		public static final int REVIEW_ME = 1;
		public static final int PARTY_ME = 1;
		public static final int CHINA_LANE_ME = 1;

		public static final int ASK_ME = 1;
		public static final int ASK_ANSWER_ME = 2;

		public static final int MAP_POINT_ADD = 1;
		public static final int MAP_POINT_ERROR = 2;

		public static final int ALBUM_SUBJECT = 1;
		public static final int ALBUM_REVIEW = 2;
		public static final int ALBUM_CHINA_LANE = 3;

	}

	public static class Third {
		public static final String SINA_WEIBO_APP_KEY = "821457627";
		public static final String SINA_WEIBO_APP_SECRET = "95cf4f7d3a871ff91be5ba445b81185f";
		public static final String SINA_WEIBO_REDIRECT_URL = "http://www.40000km.com.cn/";
		public static final String SINA_WEIBO_SCOPE = "email,direct_messages_read,direct_messages_write,"
				+ "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
				+ "follow_app_official_microblog," + "invitation_write";
		public static final String SINA_WEIBO_GET_USERINFO_URL = "https://api.weibo.com/2/users/show.json";

		/** QQ互联平台 **/
		public static final String TENCENT_QQ_APP_ID = "100455976";
//		public static final String TENCENT_QQ_APP_ID = "101007340";
		// public static final String TENCENT_QQ_APP_ID = "101001015";
		// public static final String TENCENT_QQ_APP_KEY =
		// "3e36173d11d89aa7f7bb6d98466c4edc";
		//新：100455976
		//新：9ffa10a79ec68f314b07aa5c48e92cf2
		public static final String TENCENT_QQ_SCOPE = "all";
		// public static final String TENCENT_QQ_SCOPE =
		// "get_user_info;upload_pic;get_info;add_t;add_pic_t;add_one_blog";
		/** 腾讯开放平台 **/
		public static final String TENCENT_WEIBO_APP_KEY = "801470184";
		public static final String TENCENT_WEIBO_APP_SECRET = "1c6f4ff400b8f20e73ad5573200464b7";
		public static final String TENCENT_WEIBO_REDIRECT_URL = "http://www.40000km.com.cn/";

		public static final String WEIXIN_APP_ID = "wxf8be95d381cc3b3d";
		// public static final String WEIXIN_APP_ID = "wx0be56f9c9aae8e96";
		// public static final String WEIXIN_APP_KEY =
		// "4c53e361f42041e8f1dd82414f633f2b";

//		public static final String TAOBAO_APP_KEY = "21724339";
//		public static final String TAOBAO_APP_SECRET = "4dfa1b6312cddc83d2a811c4251bdc18";
//		public static final String TAOBAO_REDIRECT_URL = "com.fourkkm.citylife://authresult";
		public static final String TAOBAO_APP_KEY = "21725410";
		public static final String TAOBAO_APP_SECRET = "422e8878626e5b08616230f0139fa916";
		public static final String TAOBAO_REDIRECT_URL = "taobao21725410://";

		public static final String PSNAME_SINA_WEIBO = "weibo";
		public static final String PSNAME_TENCENT_QQ = "qq";
		public static final String PSNAME_TENCENT_WEIBO = "qqweibo";
		public static final String PSNAME_TAOBAO = "taobao";
		public static final String PSNAME_WEIXIN = "weixin";

		public static final String KEY_ACCESS_TOKEN = "AccessToken";
		public static final String KEY_UID = "Uid";
		public static final String KEY_EXPIRE_TIME = "ExpireTime";
		public static final String KEY_NICK_NAME = "NickName";

		/** TencentWeibo Api url **/
		public static final String TENCENT_URL_API = "https://open.t.qq.com/api";
		/** 发表一条微博请求url **/
		public static final String TENCENT_URL_API_ADD = TENCENT_URL_API
				+ "/t/add";//
	}
}
