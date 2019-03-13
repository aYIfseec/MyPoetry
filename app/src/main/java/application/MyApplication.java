package application;

import android.graphics.Typeface;
import android.util.Log;

import java.lang.reflect.Field;

import manager.DataManager;
import model.Poetry;
import model.UserAccount;
import zuo.biao.library.base.BaseApplication;
import zuo.biao.library.util.StringUtil;


public class MyApplication extends BaseApplication {
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


    /**获取当前用户id
     * @return
     */
    public long getCurrentUserId() {
        currentUser = getCurrentUser();
        Log.d(TAG, "getCurrentUserId  currentUserId = " + (currentUser == null ? "null" : currentUser.getUid()));
        return currentUser == null ? 0 : currentUser.getUid();
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




    private UserAccount user;
    private Poetry currPoetry;

    public String getPhoneNumber() {
        return "";
    }
    public UserAccount getUser(){
        return this.user;
    }

    public Poetry getCurrPoetry() {
        return currPoetry;
    }

    public void setCurrPoetry(Poetry currPoetry) {
        this.currPoetry = currPoetry;
    }
}
