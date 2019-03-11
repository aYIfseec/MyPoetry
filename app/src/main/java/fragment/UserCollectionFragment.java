package fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lenovo.mypoetry.R;
import com.google.common.collect.Lists;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import adapter.PoetrySearchResAdapter;
import adapter.UserCollectionAdapter;
import callback.ListViewItemClickCallBack;
import common.UserCollectionListOrder;
import manager.OnHttpResponseListener;
import manager.OnHttpResponseListenerImpl;
import model.Poetry;
import model.UserCollection;
import application.MyApplication;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import utils.ServerUrlUtil;
import utils.ParseJSONUtil;
import view.UserCollentionItemView;
import zuo.biao.library.base.BaseHttpListFragment;
import zuo.biao.library.base.BaseHttpRecyclerFragment;
import zuo.biao.library.interfaces.AdapterCallBack;
import zuo.biao.library.util.JSON;

/**
 * Created by Administrator on 2018/1/12.
 */

public class UserCollectionFragment
        extends BaseHttpRecyclerFragment<UserCollection, UserCollentionItemView, UserCollectionAdapter>
        implements OnHttpResponseListener {

    private String keyword;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

//        setContentView(R.layout.collection_list);

        //功能归类分区方法，必须调用<<<<<<<<<<
        initView();
        initData();
        initEvent();
        //功能归类分区方法，必须调用>>>>>>>>>>
        return view;
    }

    @Override
    public void initView() {
        super.initView();
    }
    @Override
    public void initData() {
        super.initData();
        keyword = "";
        srlBaseHttpRecycler.autoRefresh();
    }

    @Override
    public void initEvent() {
        super.initEvent();
    }

    public void refresh(String keyword) {
        if (StringUtils.isNotBlank(keyword)) {
            this.keyword = keyword;
            srlBaseHttpRecycler.autoRefresh();
        }
    }


    @Override
    public void setList(final List<UserCollection> list) {
        setList(new AdapterCallBack<UserCollectionAdapter>() {
            @Override
            public UserCollectionAdapter createAdapter() {
                return new UserCollectionAdapter(context);
            }

            @Override
            public void refreshAdapter() {
                adapter.refresh(list);
            }
        });
    }

    @Override
    public void getListAsync(int page) {
        ServerUrlUtil.getMyCollection(keyword, UserCollectionListOrder.ORDER_BY_TIME_DESC,
                page, 25, this);
    }

    @Override
    public List<UserCollection> parseArray(String json) {
        List<UserCollection> res = Lists.newArrayList();
        JSONObject resObj = null;
        try {
            resObj = new JSONObject(json);
            res = JSON.parseArray(resObj.getString("resData"), UserCollection.class);
        } catch (JSONException e) {
            Log.e("UserCollection", e.getMessage());
            e.printStackTrace();
        }
        return res;
    }


    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        final UserCollection collection = adapter.getItem(position);

        AlertDialog.Builder deleteDialog = new AlertDialog.Builder(context);
        deleteDialog.setTitle("您要删除此条收藏吗？");
        deleteDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ServerUrlUtil.delCollect(collection.getId(), new OnHttpResponseListenerImpl(UserCollectionFragment.this));
                List<UserCollection>  list = adapter.getList();
                list.remove(position);
                adapter.refresh(list);
            }
        });
        deleteDialog.show();
        return true;
    }

    @Override
    public void onHttpSuccess(int requestCode, int resultCode, String resultMsg, String resultData) {
        if (requestCode == ServerUrlUtil.COLLECTION_DEL_REQUEST_CODE) {
            if (resultCode != 0) {
                showShortToast(resultMsg);
            } else {
                // remove item
            }
        }

    }

    @Override
    public void onHttpError(int requestCode, Exception e) {
        showShortToast(e.getMessage());
    }
}
