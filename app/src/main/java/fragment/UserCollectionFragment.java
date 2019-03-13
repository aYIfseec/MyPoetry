package fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.google.common.collect.Lists;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import adapter.UserCollectionAdapter;
import common.UserCollectionListOrder;
import manager.OnHttpResponseListener;
import manager.OnHttpResponseListenerImpl;
import model.UserCollection;
import utils.RequestDataUtil;
import view.UserCollentionItemView;
import zuo.biao.library.base.BaseHttpRecyclerFragment;
import zuo.biao.library.interfaces.AdapterCallBack;
import zuo.biao.library.ui.AlertDialog;
import zuo.biao.library.util.JSON;



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
        RequestDataUtil.getMyCollection(keyword, UserCollectionListOrder.ORDER_BY_TIME_DESC,
                page, RequestDataUtil.middlePageSize, this);
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


    private AlertDialog deleteDialog;

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        final UserCollection collection = adapter.getItem(position);
        deleteDialog = new AlertDialog(context, "提示", "您要删除此条收藏吗？", true, 0,
                new AlertDialog.OnDialogButtonClickListener() {
            @Override
            public void onDialogButtonClick(int requestCode, boolean isPositive) {
                if (isPositive) {
                    RequestDataUtil.delCollect(collection.getId(), new OnHttpResponseListenerImpl(UserCollectionFragment.this));
                    List<UserCollection> list = adapter.getList();
                    list.remove(position);
                    adapter.refresh(list);
                } else {
                    deleteDialog.cancel();
                }
            }
        });
        deleteDialog.show();
        return true;
    }

    @Override
    public void onHttpSuccess(int requestCode, int resultCode, String resultMsg, String resultData) {
        if (requestCode == RequestDataUtil.COLLECTION_DEL_REQUEST_CODE) {
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
