package activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.example.lenovo.mypoetry.R;

import org.apache.commons.lang3.StringUtils;

import application.MyApplication;
import callback.ListViewItemClickCallBack;
import fragment.MyCollectionFragment;
import fragment.MyUploadRecordFragment;
import fragment.PoetryFragment;
import fragment.SearchFragment;
import fragment.TodayFragment;
import manager.OnHttpResponseListener;
import manager.OnHttpResponseListenerImpl;
import model.Poetry;
import utils.ServerUrlUtil;
import zuo.biao.library.base.BaseActivity;

public class PoetryActivity extends BaseActivity
        implements OnHttpResponseListener {

    private FragmentManager fragmentManager;

    private Fragment poetryFragment;

    private String poetryId;

    private Poetry poetry;


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
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

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        poetryId = bundle.getString("poetryId");

        initView();
        initData();
        initEvent();
    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_poetry);

        setDefaultFragment();
    }

    private void setDefaultFragment() {
        fragmentManager = getSupportFragmentManager();

        if (poetryFragment != null) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.remove(poetryFragment).commit();
        }
        Bundle bundle = new Bundle();
        bundle.putString("poetryId", poetryId);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        poetryFragment = new PoetryFragment();
        poetryFragment.setArguments(bundle);
        transaction.replace(R.id.activity_poetry_fragment, poetryFragment);//
        transaction.commit();
//        currFragment = poetryFragment;
    }

    @Override
    public void initData() {

    }

    public void getData(String poetryId) {
//        ServerUrlUtil.getPoetry(poetryId, new OnHttpResponseListenerImpl(context));
    }

    @Override
    public void initEvent() {

    }

    @Override
    public void onHttpSuccess(int requestCode, int resultCode, String resultMsg, String resultData) {
        Log.d("MainActivity", "onResponse: " + resultData);
        poetry = JSON.parseObject(resultData, Poetry.class);
        handler.sendEmptyMessage(0);
    }

    @Override
    public void onHttpError(int requestCode, Exception e) {

    }

}
