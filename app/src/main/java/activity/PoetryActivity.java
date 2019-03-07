package activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.example.lenovo.mypoetry.R;
import com.google.common.collect.Lists;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

import fragment.PoetryFragmentInterface;
import fragment.PoetryRemarkFragment;
import fragment.PoetryRecordListFragment;
import fragment.PoetryContentFragment;
import manager.OnHttpResponseListener;
import manager.OnHttpResponseListenerImpl;
import model.Poetry;
import utils.Constant;
import utils.ServerUrlUtil;
import zuo.biao.library.base.BaseBottomTabActivity;
import zuo.biao.library.interfaces.OnBottomDragListener;

public class PoetryActivity extends BaseBottomTabActivity
        implements OnHttpResponseListener , OnBottomDragListener {

    private static final String TAG = "PoetryActivity";

    private List<Fragment> fragmentList;
    private Fragment poetryContentFragment;
    private Fragment poetryRermakFragment;
    private Fragment poetryRecordListFragment;



    private String poetryId;
    private Poetry poetry;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_poetry, this);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        poetryId = bundle.getString(Constant.POETRY_ID);

        initView();
        initData();
        initEvent();
    }

    @Override
    public void initView() {
        super.initView();

        tvBaseTitle = findViewById(R.id.poetryTabTitle);

        poetryContentFragment = new PoetryContentFragment();
        poetryRermakFragment = new PoetryRemarkFragment();
        poetryRecordListFragment = new PoetryRecordListFragment();

        fragmentList = Lists.newArrayList(poetryContentFragment, poetryRermakFragment, poetryRecordListFragment);
    }


    @Override
    public void initData() {
        super.initData();
        if (StringUtils.isNotBlank(poetryId)) {
            ServerUrlUtil.getPoetry(poetryId, new OnHttpResponseListenerImpl(this));
        }
    }

    @Override
    public void initEvent() {
        super.initEvent();

    }

    @Override
    public void onHttpSuccess(int requestCode, int resultCode, String resultMsg, String resultData) {
        Log.d("PoetryActivity", "onResponse: " + resultData);
        poetry = JSON.parseObject(resultData, Poetry.class);
        for (Fragment in :fragmentList) {
            ((PoetryFragmentInterface)in).bindData(poetry);
        }
    }

    @Override
    public void onHttpError(int requestCode, Exception e) {

    }





    private static final String[] TAB_NAMES = {"原文", "赏析", "发现"};
    @Override
    protected void selectTab(int position) {
        tvBaseTitle.setText(TAB_NAMES[position]);
    }

    @Override
    protected int[] getTabClickIds() {
        return new int[]{R.id.poetryTabPoint0, R.id.poetryTabPoint1, R.id.poetryTabPoint2};
    }

    @Override
    protected int[][] getTabSelectIds() {
        return new int[][]{
                new int[]{R.id.poetryTabPoint0, R.id.poetryTabPoint1, R.id.poetryTabPoint2}
        };
    }

    @Override
    public int getFragmentContainerResId() {
        return R.id.activity_poetry_fragment;
    }

    @Override
    protected Fragment getFragment(int position) {
        Fragment fragment = fragmentList.get(position);
        if (fragment != null) {
            return fragment;
        }
        return null;
    }

    @Override
    public void onDragBottom(boolean rightToLeft) {
        // 将Activity的onDragBottom事件传递到Fragment
        if (rightToLeft) {
            if (currentPosition == (getCount() - 1)) {
                return;
            }
            selectFragment(currentPosition + 1);
        } else {
            if (currentPosition == 0) {
                return;
            }
            selectFragment(currentPosition - 1);
        }
    }

}
