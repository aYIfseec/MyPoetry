package activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.lenovo.mypoetry.R;

import application.MyApplication;
import fragment.TodayFragment;
import manager.DataManager;
import utils.RequestDataUtil;
import zuo.biao.library.base.BaseActivity;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static int LOGIN_REQUEST_CODE = 1;
    private TextView tvUserName;
    private FragmentManager fragmentManager;

    private Fragment todayFragment;

    private Fragment currFragment;
    private MenuItem loginItem, homeItem;
    private MenuItem logoutItem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
        initEvent();
    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_main);

        final NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        loginItem = navigationView.getMenu().findItem(R.id.nav_login);
        logoutItem = navigationView.getMenu().findItem(R.id.nav_logout);
//        homeItem = navigationView.getMenu().findItem(R.id.nav_one_poetry);
        View headerLayout = navigationView.inflateHeaderView(R.layout.nav_header_main);
        //userHead = (ImageView) headerLayout.findViewById(R.uid.userHead);
        tvUserName = headerLayout.findViewById(R.id.tv_user_name);

        fragmentManager = getSupportFragmentManager();

        setDefaultFragment();
    }

    private void setDefaultFragment() {
        if (todayFragment != null) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.remove(todayFragment).commit();
        }
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        todayFragment = TodayFragment.getInstance();
        transaction.replace(R.id.app_main_content, todayFragment);//
        transaction.commit();
        currFragment = todayFragment;
    }

    @Override
    public void initData() {
        context = this;
        if (MyApplication.getInstance().isLoggedIn()) {
            afterLogin();
        }
    }

    @Override
    public void initEvent() {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
//        if (id == R.id.nav_one_poetry) {
//            switchFragment(currFragment, poetryFragment);
        if (id == R.id.nav_logout) {
            loginItem.setVisible(true);
            logoutItem.setVisible(false);
            DataManager.getInstance().saveCurrentUser(null);
            RequestDataUtil.doLogout();
            afterLogin();
        }  else if (id == R.id.nav_login) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            toActivity(intent, LOGIN_REQUEST_CODE);
        } else if (id == R.id.nav_my_collection) {
            if (MyApplication.getInstance().isLoggedIn() == false) {
                showShortToast("登录后才能使用此功能");
                return false;
            } else {
                Intent intent = new Intent(context, UserCollectionActivity.class);
                toActivity(intent);
            }
        } else if (id == R.id.nav_upload_voice) {
            if (MyApplication.getInstance().isLoggedIn() == false) {
                showShortToast("登录后才能使用此功能");
                return false;
            } else {
                Intent intent = new Intent(context, UserUploadActivity.class);
                toActivity(intent);
            }
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 登录成功后会回调
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LOGIN_REQUEST_CODE && resultCode == RESULT_OK) {
            afterLogin();
        }
    }

    public void afterLogin() {
        tvUserName.setText(MyApplication.getInstance().getCurrentUserName());
//        homeItem.setChecked(true);
        if (loginItem != null) {
            loginItem.setVisible(false);
            logoutItem.setVisible(true);
        }
    }

    //双击手机返回键退出<<<<<<<<<<<<<<<<<<<<<
    private long firstTime = 0;//第一次返回按钮计时
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch(keyCode){
            case KeyEvent.KEYCODE_BACK:
                long secondTime = System.currentTimeMillis();
                if(secondTime - firstTime > 2000){
                    showShortToast("再按一次退出");
                    firstTime = secondTime;
                } else {//完全退出
                    moveTaskToBack(false);//应用退到后台
                    System.exit(0);
                }
                return true;
        }

        return super.onKeyUp(keyCode, event);
    }
}
