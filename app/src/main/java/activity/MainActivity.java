package activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.lenovo.mypoetry.R;

import org.apache.commons.lang3.StringUtils;

import fragment.TodayFragment;
import service.AudioService;
import utils.RequestDataUtil;
import zuo.biao.library.base.BaseActivity;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static int LOGIN_REQUEST_CODE = 1;
    private boolean isLogin = false;
    private TextView tvUserName;
    private FragmentManager fragmentManager;

    private Fragment todayFragment;
    private Fragment myCollectionFragment;
    private Fragment myUploadRecordFragment;

    private Fragment currFragment;
    private MenuItem loginItem, homeItem;


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
        homeItem = navigationView.getMenu().findItem(R.id.nav_one_poetry);
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
        if (StringUtils.isNotBlank(RequestDataUtil.getUserName())) {
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
        if (id == R.id.nav_one_poetry) {
//            switchFragment(currFragment, poetryFragment);
        } else if (id == R.id.nav_login) {
            if (RequestDataUtil.checkLoginStatus()) {
                showShortToast("您已登录");
                return false;
            } else {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                toActivity(intent, LOGIN_REQUEST_CODE);
            }
        } else if (id == R.id.nav_my_collection) {
            if (RequestDataUtil.checkLoginStatus() == false) {
                showShortToast("登录后才能使用此功能");
                return false;
            } else {
//                if (myCollectionFragment == null) {
//                    myCollectionFragment = new UserCollectionFragment();
//                }
//                switchFragment(currFragment, myCollectionFragment);
                Intent intent = new Intent(context, UserCollectionActivity.class);
                toActivity(intent);
            }
        } else if (id == R.id.nav_upload_voice) {
            if (!isLogin) {
                showShortToast("登录后才能使用此功能");
                return false;
            } else {
                if (myUploadRecordFragment == null) {
//                    myUploadRecordFragment = new MyUploadRecordFragment();
                }
                switchFragment(currFragment, myUploadRecordFragment);
            }
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void switchFragment(Fragment from, Fragment to) {
        if (currFragment != to) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            if (! to.isAdded()) {
                transaction.hide(from).add(R.id.app_main_content, to).commit();
            } else {
                transaction.hide(from).show(to).commit();
            }
            currFragment = to;
        }
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
        isLogin = true;
        tvUserName.setText(RequestDataUtil.getUserName());
        Intent i = new Intent("MyPoetry");
        i.putExtra("Msg","UserLogin");
        homeItem.setChecked(true);
        sendBroadcast(i);
        if (loginItem != null) {
            loginItem.setVisible(false);
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
