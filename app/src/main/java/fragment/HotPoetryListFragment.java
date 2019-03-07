package fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import adapter.PoetrySearchResAdapter;
import model.Poetry;
import utils.ServerUrlUtil;
import zuo.biao.library.base.BaseHttpListFragment;
import zuo.biao.library.interfaces.AdapterCallBack;

public class HotPoetryListFragment extends BaseHttpListFragment<Poetry, ListView, PoetrySearchResAdapter> {

    private static final String TAG = "HotPoetryListFragment";


    public static HotPoetryListFragment getInstance() {
        return new HotPoetryListFragment();
    }


    @Override
    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        initView();
        initData();
        initEvent();

        srlBaseHttpList.autoRefresh();

        return view;
    }

    @Override
    public void initView() {
        super.initView();
    }

    @Override
    public void initData() {
        super.initData();
    }

    @Override
    public void initEvent() {
        super.initEvent();
    }


    @Override
    public void setList(final List<Poetry> list) {
        setList(new AdapterCallBack<PoetrySearchResAdapter>() {
            @Override
            public PoetrySearchResAdapter createAdapter() {
                return new PoetrySearchResAdapter(context);
            }

            @Override
            public void refreshAdapter() {
                adapter.refresh(list);
            }
        });
    }

    @Override
    public void getListAsync(int page) {
        ServerUrlUtil.getHotPoetryList(page, 15, this);
    }

    @Override
    public List parseArray(String json) {
        List<Poetry> res = Lists.newArrayList();
        JSONObject resObj = null;
        try {
            resObj = new JSONObject(json);
            res = JSON.parseArray(resObj.getString("resData"), Poetry.class);
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
            e.printStackTrace();
        }
        return res;
    }

}
