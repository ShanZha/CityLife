package com.fourkkm.citylife.control.activity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.andrnes.modoer.ModoerMemberPassport;
import com.andrnes.modoer.ModoerMembers;
import com.andrnes.modoer.ModoerUsergroups;
import com.fourkkm.citylife.CoreApp;
import com.fourkkm.citylife.R;
import com.fourkkm.citylife.constant.GlobalConfig;
import com.fourkkm.citylife.third.BaseQQRequestListener;
import com.fourkkm.citylife.third.IThirdAuthListener;
import com.fourkkm.citylife.third.IThirdUserInfoListener;
import com.fourkkm.citylife.third.QQAuthListener;
import com.fourkkm.citylife.third.SinaWeiboAuthListener;
import com.fourkkm.citylife.third.SinaWeiboUserInfoTask;
import com.fourkkm.citylife.third.TaobaoAuthListener;
import com.fourkkm.citylife.util.CommonUtil;
import com.fourkkm.citylife.util.MD5;
import com.fourkkm.citylife.widget.ProgressDialogProxy;
import com.taobao.top.android.TopAndroidClient;
import com.taobao.top.android.auth.AuthActivity;
import com.taobao.top.android.auth.AuthorizeListener;
import com.tencent.tauth.Constants;
import com.tencent.tauth.Tencent;
import com.weibo.sdk.android.Oauth2AccessToken;
import com.weibo.sdk.android.Weibo;
import com.weibo.sdk.android.util.AccessTokenKeeper;
import com.zj.app.db.dao.SharedPreferenceUtil;
import com.zj.app.http.StoreOperation;
import com.zj.app.utils.AppUtils;
import com.zj.support.observer.Observer;
import com.zj.support.observer.inter.ICallback;
import com.zj.support.observer.model.Param;

/**
 * 登录界面
 * 
 * @author ShanZha
 * 
 */
public class LoginActivity extends AuthActivity implements ICallback,
		IThirdAuthListener, IThirdUserInfoListener {

	private static final String TAG = "LoginActivity";
	private static final int TYPE_SINA_WEIBO = 1;
	private static final int TYPE_QQ = 2;
	private static final int TYPE_TENCENT_WEIBO = 3;
	private static final int TYPE_TAOBAO = 4;
	private static final int REGISTER_REQ_CODE = 1;
	private static final int TENCENT_WEIBO_REQ_CODE = 2;
	private TextView mTvTitle;
	private EditText mEtUsername, mEtPswd;
	private CheckBox mCbPswd, mCbAutoLogin;

	private ModoerMembers mMember;
	private ModoerUsergroups mUserGroup;
	private ModoerMemberPassport mMemberPassport;
	private ProgressDialogProxy mDialogProxy;
	/** 第三方登录时，用户名为昵称，密码为uid，邮箱未知？？ **/
	private String mUserName, mUserPswd, mEmail = "";

	private Weibo mSinaWeibo;
	private Tencent mTencent;
	private TopAndroidClient mTaobao = TopAndroidClient
			.getAndroidClientByAppKey(GlobalConfig.Third.TAOBAO_APP_KEY);
	private int mRetryCount = 0;
	private int mCurrThirdType = -1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Observer.getObserver().addCallback(this.hashCode(), this);
		AppUtils.getBaseApp(this).addActivity(this);
		this.prepareViews();
		this.prepareDatas();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Observer.getObserver().deleteCallback(this.hashCode(), this);
		AppUtils.getBaseApp(this).popActivity(this);
	}

	protected void prepareViews() {
		this.setContentView(R.layout.login);

		mTvTitle = (TextView) this.findViewById(R.id.titlebar_back_tv_title);
		mEtUsername = (EditText) this.findViewById(R.id.login_et_username);
		mEtPswd = (EditText) this.findViewById(R.id.login_et_pswd);
		mCbPswd = (CheckBox) this.findViewById(R.id.login_check_box_pswd);
		mCbAutoLogin = (CheckBox) this
				.findViewById(R.id.login_check_box_auto_login);
	}

	protected void prepareDatas() {
		mTvTitle.setText(this.getString(R.string.login_user));
		mDialogProxy = new ProgressDialogProxy(this);
		mDialogProxy.setMessage(this.getText(R.string.login_ing));

		boolean isRemeberPswd = SharedPreferenceUtil.getSharedPrefercence()
				.getBoolean(this.getApplicationContext(),
						GlobalConfig.SharePre.KEY_IS_REMBER_PSWD);
		boolean isAutoLogin = SharedPreferenceUtil.getSharedPrefercence()
				.getBoolean(this.getApplicationContext(),
						GlobalConfig.SharePre.KEY_IS_AUTO_LOGIN);
		mCbPswd.setChecked(isRemeberPswd);
		mCbAutoLogin.setChecked(isAutoLogin);
		String username = SharedPreferenceUtil.getSharedPrefercence()
				.getString(this.getApplicationContext(),
						GlobalConfig.SharePre.KEY_USERNAME);
		mEtUsername.setText(username);
		if (isRemeberPswd) {
			String pswd = SharedPreferenceUtil.getSharedPrefercence()
					.getString(this.getApplicationContext(),
							GlobalConfig.SharePre.KEY_PSWD);
			mEtPswd.setText(pswd);
		}

		mSinaWeibo = Weibo.getInstance(GlobalConfig.Third.SINA_WEIBO_APP_KEY,
				GlobalConfig.Third.SINA_WEIBO_REDIRECT_URL,
				GlobalConfig.Third.SINA_WEIBO_SCOPE);
		mTencent = Tencent.createInstance(GlobalConfig.Third.TENCENT_QQ_APP_ID,
				this.getApplicationContext());
	}

	private void showToast(String msg) {
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
	}

	private StoreOperation getStoreOperation() {
		return StoreOperation.getInstance(this);
	}

	/**
	 * 密码是否正确
	 * 
	 * @param inputPswd
	 * @param dbPswd
	 * @return
	 */
	private boolean isPswdCorrect(String inputPswd, String dbPswd) {
		String encrytPswd = MD5.encryptMd5(inputPswd);
		return encrytPswd.equals(dbPswd);
	}

	private void fecthMember(ModoerMembers member) {
		String selectCode = "from com.andrnes.modoer.ModoerMembers mm where mm.username = :username";
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put("username", member.getUsername());
		Param param = new Param(this.hashCode(), GlobalConfig.URL_CONN);
		param.setOperator(GlobalConfig.Operator.OPERATION_FIND_MEMBER);
		this.getStoreOperation().find(selectCode, paramsMap, true, member,
				param);
	}

	private void fetchUserGroup() {
		String selectCode = "from com.andrnes.modoer.ModoerUsergroups mug where mug.grouptype=:groupType order by point";
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("groupType", "member");

		Param param = new Param(this.hashCode(), GlobalConfig.URL_CONN);
		this.getStoreOperation().findAll(selectCode, paramMap, false,
				new ModoerUsergroups(), param);
	}

	private void fetchMemberByThirdInfo(ModoerMemberPassport passport) {
		String selectCode = "from com.andrnes.modoer.ModoerMemberPassport mmp where mmp.psuid = :psuid and mmp.psname = :psname ";
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put("psuid", passport.getPsuid());
		paramsMap.put("psname", passport.getPsname());
		// paramsMap.put("accessToken", passport.getAccessToken());
		Param param = new Param(this.hashCode(), GlobalConfig.URL_CONN);
		param.setOperator(GlobalConfig.Operator.OPERATION_FIND_MEMBERPASSPORT);
		this.getStoreOperation().find(selectCode, paramsMap, true,
				mMemberPassport, param);
	}

	private void onSaveMemeber() {
		if (null == mMember) {
			mMember = new ModoerMembers();
		}
		mMember.setUsername(mUserName);
		mMember.setEmail(mEmail);
		mMember.setPassword(MD5.encryptMd5(mUserPswd));
		mMember.setRegdate((int) CommonUtil.getCurrTimeByPHP());
		mMember.setRmb(new BigDecimal(0));
		mMember.setNewmsg("0");
		mMember.setPoint(0);
		mMember.setPoint1(0);
		mMember.setPoint2(0);
		mMember.setPoint3(0);
		mMember.setPoint4(0);
		mMember.setPoint5(0);
		mMember.setPoint6(0);
		mMember.setGroupid(mUserGroup);
		mMemberPassport.setUid(mMember);

		Param param = new Param(this.hashCode(), GlobalConfig.URL_CONN);
		param.setOperator(GlobalConfig.Operator.OPERATION_SAVE_MEMBER);
		List<Object> objs = new ArrayList<Object>();
		objs.add(mMember);
		objs.add(mMemberPassport);
		this.getStoreOperation().saveOrUpdateArray(objs, param);
	}

	/**
	 * 第三方鉴权完毕时，构建ModoerMemberPassport
	 * 
	 * @param psname
	 * @param accessToken
	 * @param uid
	 * @param expireTime
	 */
	private void buildMemberPassport(String psname, String accessToken,
			String uid, long expireTime) {
		mMemberPassport = new ModoerMemberPassport();
		mMemberPassport.setPsname(psname);
		mMemberPassport.setAccessToken(accessToken);
		mMemberPassport.setExpired(expireTime);
		mMemberPassport.setPsuid(uid);
		mMemberPassport.setIsbind(1);
	}

	private void fecthQQUserInfo() {
		if (null != mTencent && mTencent.ready(this.getApplicationContext())) {
			mTencent.requestAsync(Constants.GRAPH_SIMPLE_USER_INFO, null,
					Constants.HTTP_GET,
					new BaseQQRequestListener(TYPE_QQ, this), null);
		}
	}

	public void onClickBack(View view) {
		this.setResult(RESULT_CANCELED);
		this.finish();
	}

	public void onClickRegister(View view) {// 注册
		this.startActivityForResult(new Intent(this, RegisterActivity.class),
				REGISTER_REQ_CODE);
	}

	public void onClickLogin(View view) {// 登录
		String username = mEtUsername.getText().toString().trim();
		String pswd = mEtPswd.getText().toString().trim();
		if (TextUtils.isEmpty(username)) {
			this.showToast(this.getString(R.string.register_username_not_null));
			return;
		}
		if (TextUtils.isEmpty(pswd)) {
			this.showToast(this.getString(R.string.login_pswd_null));
			return;
		}
		mDialogProxy.showDialog();
		mMember = new ModoerMembers();
		mMember.setUsername(username);
		mUserName = username;
		mUserPswd = pswd;
		this.fecthMember(mMember);
	}

	public void onClickQQ(View view) {// qq登录
		mCurrThirdType = TYPE_QQ;
		mTencent.login(this, GlobalConfig.Third.TENCENT_QQ_SCOPE,
				new QQAuthListener(TYPE_QQ, this));
	}

	public void onClickTencentWeibo(View view) {// 腾讯微博登录
		mCurrThirdType = TYPE_TENCENT_WEIBO;
		this.startActivityForResult(
				new Intent(this, TencentAuthActivity.class),
				TENCENT_WEIBO_REQ_CODE);
	}

	public void onClickSinaWeibo(View view) {// 新浪微博登录
		mCurrThirdType = TYPE_SINA_WEIBO;
		mSinaWeibo.anthorize(this, new SinaWeiboAuthListener(TYPE_SINA_WEIBO,
				this));
	}

	public void onClickTaoBao(View view) {// 淘宝登录
		mCurrThirdType = TYPE_TAOBAO;
		mTaobao.authorize(this);
		this.finish();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (RESULT_OK != resultCode) {
			return;
		}

		if (REGISTER_REQ_CODE == requestCode) {// 注册成功
			this.setResult(RESULT_OK);
			this.finish();
		} else if (TENCENT_WEIBO_REQ_CODE == requestCode) {// 腾讯微博登录成功
			if (null != data) {
				this.onThirdAuthSuccess(TYPE_TENCENT_WEIBO,
						data.getBundleExtra("values"));
			}
		}
	}

	@Override
	protected TopAndroidClient getTopAndroidClient() {
		// TODO Auto-generated method stub
		return mTaobao;
	}

	@Override
	protected AuthorizeListener getAuthorizeListener() {
		// TODO Auto-generated method stub
		return new TaobaoAuthListener(TYPE_TAOBAO, this);
	}

	@Override
	public void onSuccessFindAll(Param out) {
		// TODO Auto-generated method stub
		List<Object> results = (List<Object>) out.getResult();
		if (null == results) {
			Log.e(TAG, "shan--->results is null");
			return;
		}
		if (results.size() > 0) {
			mUserGroup = (ModoerUsergroups) results.get(0);
		}
		if (null == mUserGroup) {
			mDialogProxy.hideDialog();
			this.showToast(this.getString(R.string.login_fail));
		} else {// Step 4:保存ModoerMembers
			this.onSaveMemeber();
		}

	}

	@Override
	public void onSuccessSaveOrUpdate(Param out) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onSuccessSaveOrUpdateArray(Param out) {
		// TODO Auto-generated method stub
		int operator = out.getOperator();
		if (GlobalConfig.Operator.OPERATION_SAVE_MEMBER == operator) {
			((CoreApp) AppUtils.getBaseApp(this)).setCurrMember(mMember);
			SharedPreferenceUtil.getSharedPrefercence().put(
					this.getApplicationContext(),
					GlobalConfig.SharePre.KEY_USERNAME, mMember.getUsername());
			SharedPreferenceUtil.getSharedPrefercence().put(
					this.getApplicationContext(),
					GlobalConfig.SharePre.KEY_PSWD, mUserPswd);
			SharedPreferenceUtil.getSharedPrefercence().put(
					this.getApplicationContext(),
					GlobalConfig.SharePre.KEY_IS_REMBER_PSWD, true);
			SharedPreferenceUtil.getSharedPrefercence().put(
					this.getApplicationContext(),
					GlobalConfig.SharePre.KEY_MEMBER_ID, mMember.getId());
			mDialogProxy.hideDialog();
			this.showToast(this.getString(R.string.login_success));
			this.setResult(RESULT_OK);
			this.finish();
		}
	}

	@Override
	public void onSuccessDelete(Param out) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSuccessShowDomain(Param out) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSuccessUploadFile(Param out) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSuccessExecuteQuery(Param out) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSuccessFind(Param out) {
		// TODO Auto-generated method stub
		int operator = out.getOperator();
		if (GlobalConfig.Operator.OPERATION_FIND_MEMBER == operator) {
			Object result = out.getResult();
			if (null == result) {
				mDialogProxy.hideDialog();
				this.showToast(this.getString(R.string.login_info_incorrect));
				return;
			}
			mMember = (ModoerMembers) result;
			if (this.isPswdCorrect(mUserPswd, mMember.getPassword())) {
				((CoreApp) AppUtils.getBaseApp(this)).setCurrMember(mMember);
				SharedPreferenceUtil.getSharedPrefercence().put(
						this.getApplicationContext(),
						GlobalConfig.SharePre.KEY_USERNAME,
						mMember.getUsername());
				SharedPreferenceUtil.getSharedPrefercence().put(
						this.getApplicationContext(),
						GlobalConfig.SharePre.KEY_PSWD, mUserPswd);
				SharedPreferenceUtil.getSharedPrefercence().put(
						this.getApplicationContext(),
						GlobalConfig.SharePre.KEY_MEMBER_ID, mMember.getId());
				SharedPreferenceUtil.getSharedPrefercence().put(
						this.getApplicationContext(),
						GlobalConfig.SharePre.KEY_IS_REMBER_PSWD,
						mCbPswd.isChecked());
				SharedPreferenceUtil.getSharedPrefercence().put(
						this.getApplicationContext(),
						GlobalConfig.SharePre.KEY_IS_AUTO_LOGIN,
						mCbAutoLogin.isChecked());
				this.setResult(RESULT_OK);
				this.finish();
			} else {
				this.showToast(this.getString(R.string.login_info_incorrect));
			}
			mDialogProxy.hideDialog();
		} else if (GlobalConfig.Operator.OPERATION_FIND_MEMBERPASSPORT == operator) {
			Object result = out.getResult();
			if (null == result) {// 第三方账号还没注册过，则自动注册
				switch (mCurrThirdType) {
				case TYPE_QQ:
					// Step 2:获取用户第三方的昵称
					this.fecthQQUserInfo();
					break;
				case TYPE_SINA_WEIBO:
					// Step 2:获取用户第三方的昵称
					AppUtils.getExecutors(getApplicationContext()).submit(
							new SinaWeiboUserInfoTask(mMemberPassport
									.getAccessToken(), mMemberPassport
									.getPsuid(), this, TYPE_SINA_WEIBO));
					break;
				case TYPE_TENCENT_WEIBO:// 已有昵称，直接进入第三步
					// Step 3:获取ModoerUserGroup
					LoginActivity.this.fetchUserGroup();
					break;
				case TYPE_TAOBAO:// 已有昵称，直接进入第二步
					// Step 3:获取ModoerUserGroup
					LoginActivity.this.fetchUserGroup();
					break;
				}
			} else {// 第三方账号之前已经登录过，则直接登录
				mMemberPassport = (ModoerMemberPassport) result;
				mMember = mMemberPassport.getUid();
				this.fecthMember(mMember);
			}

		}

	}

	@Override
	public void onFails(Param out) {
		int operator = out.getOperator();
		if (GlobalConfig.Operator.OPERATION_SAVE_MEMBER == operator) {
			mDialogProxy.hideDialog();
		} else if (GlobalConfig.Operator.OPERATION_FIND_MEMBERPASSPORT == operator) {
			if (mRetryCount < 3) {// 查询第三方信息失败，则重试三次
				this.fetchMemberByThirdInfo(mMemberPassport);
				mRetryCount++;
				return;
			}
			this.showToast(this.getString(R.string.login_fail));
			mDialogProxy.hideDialog();
		}
	}

	@Override
	public void onThirdUserInfoSuccess(final int type, final String nickname) {
		// TODO Auto-generated method stub
		AppUtils.getHandler(getApplicationContext()).post(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				switch (type) {
				case TYPE_SINA_WEIBO:
				case TYPE_QQ:
					mUserName = nickname;
					// Step 2:获取ModoerUserGroup
					LoginActivity.this.fetchUserGroup();
					break;
				}

			}
		});

	}

	@Override
	public void onThirdUserInfoFail(int type, String error) {
		// TODO Auto-generated method stub
		AppUtils.getHandler(getApplicationContext()).post(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				mDialogProxy.hideDialog();
				LoginActivity.this.showToast(LoginActivity.this
						.getString(R.string.login_fail));
			}
		});
	}

	@Override
	public void onThirdAuthSuccess(int type, Bundle bundle) {
		// TODO Auto-generated method stub
		String accessToken = bundle
				.getString(GlobalConfig.Third.KEY_ACCESS_TOKEN);
		String uid = bundle.getString(GlobalConfig.Third.KEY_UID);
		long expireTime = bundle.getLong(GlobalConfig.Third.KEY_EXPIRE_TIME);
		Log.e(TAG, "shan-->onThirdAuthSuccess: " + accessToken + " ,uid: "
				+ uid + " ,expireTime: " + expireTime);
		mUserPswd = uid;
		if (null != mDialogProxy) {
			mDialogProxy.showDialog();
		}
		switch (type) {
		case TYPE_SINA_WEIBO:
//			Oauth2AccessToken token = new Oauth2AccessToken(accessToken,
//					String.valueOf(expireTime));
//			AccessTokenKeeper.keepAccessToken(LoginActivity.this, token);
			this.buildMemberPassport(GlobalConfig.Third.PSNAME_SINA_WEIBO,
					accessToken, uid, expireTime);
			break;
		case TYPE_QQ:
			expireTime = (System.currentTimeMillis() + expireTime * 1000) / 1000;
			this.buildMemberPassport(GlobalConfig.Third.PSNAME_TENCENT_QQ,
					accessToken, uid, expireTime);
			break;

		case TYPE_TENCENT_WEIBO:
			expireTime = (System.currentTimeMillis() / 1000 + expireTime);
			mUserName = bundle.getString(GlobalConfig.Third.KEY_NICK_NAME);
			this.buildMemberPassport(GlobalConfig.Third.PSNAME_TENCENT_WEIBO,
					accessToken, uid, expireTime);

			break;
		case TYPE_TAOBAO:
			mCurrThirdType = TYPE_TAOBAO;
			mUserName = bundle.getString(GlobalConfig.Third.KEY_NICK_NAME);
			this.buildMemberPassport(GlobalConfig.Third.PSNAME_TAOBAO,
					accessToken, uid, expireTime);
			break;
		}
		// Step 1:根据条件获取ModoerMemberPassport，有则用之，无则注册
		this.fetchMemberByThirdInfo(mMemberPassport);
	}

	@Override
	public void onThirdAuthFail(int type, String e) {
		// TODO Auto-generated method stub
		Log.e(TAG, "shan-->onThirdAuthFail: " + e);
		this.showToast(this.getString(R.string.login_fail) + "：" + e);
	}

	@Override
	public void onThirdAuthCancel(int type) {
		// TODO Auto-generated method stub
		Log.e(TAG, "shan-->onThirdAuthCancel()");
	}

}
