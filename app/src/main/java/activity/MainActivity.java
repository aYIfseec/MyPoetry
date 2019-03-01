package activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.example.lenovo.mypoetry.R;

import org.apache.commons.lang3.StringUtils;

import callback.ListViewItemClickCallBack;
import fragment.MyCollectionFragment;
import fragment.MyUploadRecordFragment;
import fragment.PoetryFragment;
import fragment.SearchFragment;
import application.MyApplication;
import manager.OnHttpResponseListener;
import manager.OnHttpResponseListenerImpl;
import model.Poetry;
import utils.ServerUrlUtil;
import zuo.biao.library.base.BaseActivity;

public class MainActivity extends BaseActivity
        implements OnHttpResponseListener,
        NavigationView.OnNavigationItemSelectedListener, ListViewItemClickCallBack {
    private static int LOGIN_REQUEST_CODE = 1;
    private boolean isLogin = false;
    private TextView tvUserName;
    private FragmentManager fragmentManager;

    private Fragment poetryFragment;
    private Fragment searchFragment;
    private Fragment myCollectionFragment;
    private Fragment myUploadRecordFragment;

    private Fragment currFragment;
    private SearchView searchView;
    private MenuItem loginItem, homeItem;

    private String poetryId;
    private MyApplication myApplication;
    private MainActivity context;

    private Poetry poetry;


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            myApplication.setCurrPoetry(poetry);
            Intent i = new Intent("MyPoetry");
            i.putExtra("Msg","PoetryUpdate");
            context.sendBroadcast(i);
        }
    };

    public String getPoetryId() {
        return poetryId;
    }

    public void resetPoetryId() {
        poetryId = null;
    }

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

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //自定义tool bar
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

//        DrawerLayout drawer = findViewById(R.id.drawer_layout);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.addDrawerListener(toggle);
//        toggle.syncState();

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
        if (poetryFragment != null) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.remove(poetryFragment).commit();
        }
        Bundle bundle = new Bundle();
        bundle.putString("poetryId", poetryId);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        poetryFragment = new PoetryFragment();
        poetryFragment.setArguments(bundle);
        transaction.replace(R.id.app_main_content, poetryFragment);//
        transaction.commit();
        currFragment = poetryFragment;
    }

    @Override
    public void initData() {
        myApplication = (MyApplication) getApplication();
        context = this;
        if (StringUtils.isNotBlank(ServerUrlUtil.getUserName())) {
            afterLogin();
        }
        getData(poetryId);
    }

    public void getData(String poetryId) {
        ServerUrlUtil.getPoetry(poetryId, new OnHttpResponseListenerImpl(context));
    }

    @Override
    public void initEvent() {

    }

    @Override
    public void onHttpSuccess(int requestCode, int resultCode, String resultMsg, String resultData) {
        Log.d("MainActivity", "onResponse: " + resultData);
        poetry = JSON.parseObject(resultData, Poetry.class);
        handler.sendEmptyMessage(0);
//        myApplication.setCurrPoetry(poetry);
//        Intent i = new Intent("MyPoetry");
//        i.putExtra("Msg","PoetryUpdate");
//        context.sendBroadcast(i);
    }

    @Override
    public void onHttpError(int requestCode, Exception e) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            //super.onBackPressed();
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addCategory(Intent.CATEGORY_HOME);
            startActivity(intent);
        }
    }

    //top menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setQueryHint("搜索诗词");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            // 当点击搜索按钮时触发该方法
            @Override
            public boolean onQueryTextSubmit(String query) {
                showShortToast("搜索内容为：" + query);
                Bundle bundle = new Bundle();
                bundle.putString("keyword", query.trim());
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                if (currFragment == searchFragment) {
                    transaction.remove(currFragment).commit();
                    searchFragment = new SearchFragment();
                    searchFragment.setArguments(bundle);
                    transaction = fragmentManager.beginTransaction();
                    transaction.add(R.id.app_main_content, searchFragment).commit();
                } else {
                    searchFragment = new SearchFragment();
                    searchFragment.setArguments(bundle);
                    transaction.hide(currFragment).add(R.id.app_main_content, searchFragment).commit();
                }
                currFragment = searchFragment;
                return false;
            }

            // 当搜索内容改变时触发该方法
            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText == null || "".equals(newText)) {
                    switchFragment(searchFragment, poetryFragment);
                }
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.search_poetry) {
            if (searchFragment == null) {
                searchFragment = SearchFragment.createInstance();
            }
            switchFragment(currFragment, searchFragment);
        } else if (id == R.id.nav_one_poetry) {
            switchFragment(currFragment, poetryFragment);
        } else if (id == R.id.nav_login) {
            if (isLogin) {
                showShortToast("您已登录");
                return false;
            } else {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                toActivity(intent, LOGIN_REQUEST_CODE);
            }
        } else if (id == R.id.nav_my_collection) {
            if (!isLogin) {
                showShortToast("登录后才能使用此功能");
                return false;
            } else {
                if (myCollectionFragment == null) {
                    myCollectionFragment = new MyCollectionFragment();
                }
                switchFragment(currFragment, myCollectionFragment);
            }
        } else if (id == R.id.nav_upload_voice) {
            if (!isLogin) {
                showShortToast("登录后才能使用此功能");
                return false;
            } else {
                if (myUploadRecordFragment == null) {
                    myUploadRecordFragment = new MyUploadRecordFragment();
                }
                switchFragment(currFragment, myUploadRecordFragment);
            }
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void sendPoetryId(String poetryId) {
        this.poetryId = poetryId;
        homeItem.setChecked(true);
        switchFragment(currFragment, poetryFragment);
        getData(poetryId);
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
        if (currFragment == poetryFragment) {
            searchView.setVisibility(View.VISIBLE);
        } else {
            searchView.setVisibility(View.GONE);
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
        tvUserName.setText(ServerUrlUtil.getUserName());
        Intent i = new Intent("MyPoetry");
        i.putExtra("Msg","UserLogin");
        homeItem.setChecked(true);
        sendBroadcast(i);
        if (loginItem != null) {
            loginItem.setVisible(false);
        }
    }
}
