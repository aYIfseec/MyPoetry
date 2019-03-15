package activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.example.lenovo.mypoetry.R;
import com.google.common.collect.Lists;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

import application.MyApplication;
import fragment.PoetryContentFragment;
import fragment.MyBindDataInterface;
import fragment.PoetryRecordListFragment;
import fragment.PoetryRemarkFragment;
import manager.OnHttpResponseListener;
import manager.OnHttpResponseListenerImpl;
import model.Poetry;
import service.AudioService;
import utils.Constant;
import utils.RequestDataUtil;
import zuo.biao.library.base.BaseBottomTabActivity;
import zuo.biao.library.interfaces.OnBottomDragListener;

public class PoetryActivity extends BaseBottomTabActivity
        implements OnHttpResponseListener , OnBottomDragListener {

    private static final String TAG = "PoetryActivity";

    private List<String> tabNames;
    private List<Fragment> fragmentList;
    private Fragment poetryContentFragment;
    private Fragment poetryRermakFragment;
    private Fragment poetryRecordListFragment;


    private String poetryId;
    private Poetry poetry;


    public static Intent createIntent(Context context, Long poetryId) {
        Intent intent = new Intent(context, PoetryActivity.class);
        intent.putExtra(Constant.POETRY_ID, poetryId.toString());
        return intent;
    }


    private AudioService audioService;
    private Intent intentPlay;
    public AudioService getAudioService() {
        return audioService;
    }

    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder serviceBinder) {
            audioService = ((AudioService.MyBinder) serviceBinder).getService();
            Log.i(TAG, "audioService" + audioService.toString());
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            audioService = null;
            Log.w(TAG, "audioService Disconnected");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_poetry, this);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        poetryId = bundle.getString(Constant.POETRY_ID);

        initAudioService();

        initView();
        initData();
        initEvent();
    }

    private void initAudioService() {
        intentPlay = new Intent(getActivity(), AudioService.class);
        getActivity().bindService(intentPlay, conn, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void initView() {
        super.initView();

        tvBaseTitle = findViewById(R.id.poetryTabTitle);

        poetryContentFragment = new PoetryContentFragment();
        poetryRermakFragment = new PoetryRemarkFragment();
        poetryRecordListFragment = new PoetryRecordListFragment();

        fragmentList = Lists.newArrayList(poetryContentFragment, poetryRermakFragment);
        tabNames = Lists.newArrayList("原文", "赏析");
        if (MyApplication.getInstance().isLoggedIn()) {
            fragmentList.add(poetryRecordListFragment);
            tabNames.add("发现");
            findView(R.id.poetryTabPoint2).setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void initData() {
        super.initData();
        if (StringUtils.isNotBlank(poetryId)) {
            RequestDataUtil.getPoetry(poetryId, new OnHttpResponseListenerImpl(this));
        }
    }

    @Override
    public void initEvent() {
        super.initEvent();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unbindService(conn);
        getActivity().stopService(intentPlay);
    }

    @Override
    public void onHttpSuccess(int requestCode, int resultCode, String resultMsg, String resultData) {
        Log.d("PoetryActivity", "onResponse: " + resultData);
        poetry = JSON.parseObject(resultData, Poetry.class);
        for (Fragment in :fragmentList) {
            ((MyBindDataInterface)in).bindData(poetry);
        }
    }

    @Override
    public void onHttpError(int requestCode, Exception e) {

    }


    @Override
    protected void selectTab(int position) {
        tvBaseTitle.setText(tabNames.get(position));
    }

    @Override
    protected int[] getTabClickIds() {
        if (MyApplication.getInstance().isLoggedIn()) {
            return new int[]{R.id.poetryTabPoint0, R.id.poetryTabPoint1, R.id.poetryTabPoint2};
        }
        return new int[]{R.id.poetryTabPoint0, R.id.poetryTabPoint1};
    }

    @Override
    protected int[][] getTabSelectIds() {
        if (MyApplication.getInstance().isLoggedIn()) {
            return new int[][]{
                    new int[]{R.id.poetryTabPoint0, R.id.poetryTabPoint1, R.id.poetryTabPoint2}
            };
        }
        return new int[][]{
                new int[]{R.id.poetryTabPoint0, R.id.poetryTabPoint1}
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
