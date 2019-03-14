package manager;

import android.content.Context;
import android.content.SharedPreferences;

import application.MyApplication;
import model.UserAccount;
import zuo.biao.library.util.JSON;
import zuo.biao.library.util.Log;
import zuo.biao.library.util.StringUtil;

/**
 * 数据管理工具类
 */
public class DataManager {
	private final String TAG = "DataManager";

	private Context context;
	private DataManager(Context context) {
		this.context = context;
	}

	private static DataManager instance;
	public static DataManager getInstance() {
		if (instance == null) {
			synchronized (DataManager.class) {
				if (instance == null) {
					instance = new DataManager(MyApplication.getInstance());
				}
			}
		}
		return instance;
	}

	//用户 <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	private String PATH_USER = "PATH_USER";

	public final String KEY_USER = "KEY_USER";
	public final String KEY_USER_ID = "KEY_USER_ID";
	public final String KEY_USER_NAME = "KEY_USER_NAME";
	public final String KEY_USER_PHONE = "KEY_USER_PHONE";

	public final String KEY_CURRENT_USER_ID = "KEY_CURRENT_USER_ID";
	public final String KEY_LAST_USER_ID = "KEY_LAST_USER_ID";


	/**
	 * 判断是否为当前用户
	 * @param userId
	 * @return
	 */
	public boolean isCurrentUser(long userId) {
		return userId > 0 && userId == getCurrentUserId();
	}

	/**
	 * 获取当前用户id
	 * @return
	 */
	public long getCurrentUserId() {
		UserAccount user = getCurrentUser();
		return user == null ? 0 : user.getUid();
	}

	public String getCurrentUserToken() {
		UserAccount user = getCurrentUser();
		return user == null ? "" : user.getToken();
	}

	/**获取当前用户
	 * @return
	 */
	public UserAccount getCurrentUser() {
		SharedPreferences sdf = context.getSharedPreferences(PATH_USER, Context.MODE_PRIVATE);
		return sdf == null ? null : getUser(sdf.getLong(KEY_CURRENT_USER_ID, 0));
	}

	/**获取最后一次登录的用户
	 * @return
	 */
	public UserAccount getLastUser() {
		SharedPreferences sdf = context.getSharedPreferences(PATH_USER, Context.MODE_PRIVATE);
		return sdf == null ? null : getUser(sdf.getLong(KEY_LAST_USER_ID, 0));
	}

	/**获取用户
	 * @param userId
	 * @return
	 */
	public UserAccount getUser(long userId) {
		SharedPreferences sdf = context.getSharedPreferences(PATH_USER, Context.MODE_PRIVATE);
		if (sdf == null) {
			Log.e(TAG, "get sdf == null >>  return;");
			return null;
		}
		Log.i(TAG, "getUserAccount  userId = " + userId);
		return JSON.parseObject(sdf.getString(StringUtil.getTrimedString(userId), null), UserAccount.class);
	}


	/**保存当前用户,只在登录或注销时调用
	 * @param user  userAccount == null >> userAccount = new UserAccount();
	 */
	public void saveCurrentUser(UserAccount user) {
		SharedPreferences sdf = context.getSharedPreferences(PATH_USER, Context.MODE_PRIVATE);
		if (sdf == null) {
			Log.e(TAG, "saveUser sdf == null  >> return;");
			return;
		}
		if (user == null) {
			Log.w(TAG, "saveUser  userAccount == null >>  userAccount = new UserAccount();");
			user = UserAccount.builder().uid(0L).nickName("未登录").build();
		}
		SharedPreferences.Editor editor = sdf.edit();
		editor.remove(KEY_LAST_USER_ID).putLong(KEY_LAST_USER_ID, getCurrentUserId());
		editor.remove(KEY_CURRENT_USER_ID).putLong(KEY_CURRENT_USER_ID, user.getUid());
		editor.commit();

		saveUser(sdf, user);
	}

	/**保存用户
	 * @param user
	 */
	public void saveUser(UserAccount user) {
		saveUser(context.getSharedPreferences(PATH_USER, Context.MODE_PRIVATE), user);
	}
	/**保存用户
	 * @param sdf
	 * @param user
	 */
	public void saveUser(SharedPreferences sdf, UserAccount user) {
		if (sdf == null || user == null) {
			Log.e(TAG, "saveUser sdf == null || userAccount == null >> return;");
			return;
		}
		String key = StringUtil.getTrimedString(user.getUid());
		Log.i(TAG, "saveUser  key = userAccount.getUid() = " + user.getUid());
		sdf.edit().remove(key).putString(key, JSON.toJSONString(user)).commit();
	}

	/**删除用户
	 * @param sdf
	 */
	public void removeUser(SharedPreferences sdf, long userId) {
		if (sdf == null) {
			Log.e(TAG, "removeUser sdf == null  >> return;");
			return;
		}
		sdf.edit().remove(StringUtil.getTrimedString(userId)).commit();
	}


	/**设置当前用户姓名
	 * @param name
	 */
	public void setCurrentUserName(String name) {
		UserAccount user = getCurrentUser();
		if (user == null) {
			user = new UserAccount();
		}
		user.setNickName(name);
		saveUser(user);
	}

	//用户 >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>




}
