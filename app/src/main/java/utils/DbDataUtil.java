package utils;

import android.util.Log;

import manager.DataManager;
import model.UserAccount;

public class DbDataUtil {
    private static final String TAG = "DbDataUtil";

    public static void saveCurrentUser(UserAccount user) {
        if (user == null) {
            Log.e(TAG, "saveCurrentUser  currentUser == null >> return;");
            return;
        }
        if (user.checkCorrect() == false) {
            Log.e(TAG, "saveCurrentUser  userAccount.getUid() <= 0" +
                    " && StringUtil.isNotEmpty(userAccount.getName(), true) == false >> return;");
            return;
        }

        DataManager.getInstance().saveCurrentUser(user);
    }
}
