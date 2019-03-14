package application;

import android.graphics.Typeface;
import android.util.Log;

import com.alibaba.fastjson.JSON;

import java.lang.reflect.Field;

import manager.DataManager;
import manager.OnHttpResponseListener;
import manager.OnHttpResponseListenerImpl;
import model.Poetry;
import model.UserAccount;
import model.UserSession;
import utils.RequestDataUtil;
import zuo.biao.library.base.BaseApplication;
import zuo.biao.library.util.StringUtil;


public class MyApplication extends BaseApplication implements OnHttpResponseListener {
    private static final String TAG = "MyApplication";


    private static MyApplication context;
    public static MyApplication getInstance() {
        return context;
    }



    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        initTypeface();
        RequestDataUtil.loginStatusCheck(new OnHttpResponseListenerImpl(this));
    }

    private void initTypeface() {
        Typeface typeface = Typeface.createFromAsset(getAssets(), "font/mini.ttf");

        try {
            Field field = Typeface.class.getDeclaredField("MONOSPACE");
            field.setAccessible(true);
            field.set(null, typeface);
        } catch (IllegalAccessException e) {
            Log.e(TAG, e.toString());
        } catch (NoSuchFieldException e) {
            Log.e(TAG, e.toString());
        }
    }


    public void setUser(UserSession argument) {

        UserAccount userAccount = argument.getUserAccount();
        userAccount.setToken(argument.getToken());
        saveCurrentUser(userAccount);

    }


    /**获取当前用户id
     * @return
     */
    public long getCurrentUserId() {
        currentUser = getCurrentUser();
        Log.d(TAG, "getCurrentUserId  currentUserId = " + (currentUser == null ? "null" : currentUser.getUid()));
        return currentUser == null ? 0 : currentUser.getUid();
    }

    public String getCurrentUserName() {
        currentUser = getCurrentUser();
        Log.d(TAG, "getCurrentUserId  currentUserId = " + (currentUser == null ? "null" : currentUser.getUid()));
        return currentUser == null ? "未登录" : currentUser.getNickName();
    }


    private static UserAccount currentUser = null;
    public UserAccount getCurrentUser() {
        if (currentUser == null) {
            currentUser = DataManager.getInstance().getCurrentUser();
        }
        return currentUser;
    }

    public void saveCurrentUser(UserAccount user) {
        if (user == null) {
            Log.e(TAG, "saveCurrentUser  currentUser == null >> return;");
            return;
        }
        if (user.getUid() <= 0 && StringUtil.isNotEmpty(user.getNickName(), true) == false) {
            Log.e(TAG, "saveCurrentUser  userAccount.getUid() <= 0" +
                    " && StringUtil.isNotEmpty(userAccount.getName(), true) == false >> return;");
            return;
        }

        currentUser = user;
        DataManager.getInstance().saveCurrentUser(currentUser);
    }

    public void logout() {
        currentUser = null;
        DataManager.getInstance().saveCurrentUser(currentUser);
    }

    /**判断是否为当前用户
     * @param userId
     * @return
     */
    public boolean isCurrentUser(long userId) {
        return DataManager.getInstance().isCurrentUser(userId);
    }

    public boolean isLoggedIn() {
        return getCurrentUserId() > 0;
    }

    @Override
    public void onHttpSuccess(int requestCode, int resultCode, String resultMsg, String resultData) {
        if (resultCode == 0) {
            Boolean loginStatus = JSON.parseObject(resultData, Boolean.class);
            if (loginStatus == null || loginStatus == false) {
                Log.w(TAG, resultMsg + resultData);
                saveCurrentUser(null);
            }
        } else {
            saveCurrentUser(null);
            Log.w(TAG, resultMsg + resultData);
        }
    }

    @Override
    public void onHttpError(int requestCode, Exception e) {
        saveCurrentUser(null);
        Log.e(TAG, e.toString());
    }
}
