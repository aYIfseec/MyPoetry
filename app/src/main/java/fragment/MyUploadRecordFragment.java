package fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.example.lenovo.mypoetry.R;
import com.google.common.collect.Lists;

import org.json.JSONObject;

import java.util.List;

import adapter.CommentListAdapter;
import manager.OnHttpResponseListener;
import manager.OnHttpResponseListenerImpl;
import model.Comment;
import utils.RequestDataUtil;
import zuo.biao.library.base.BaseHttpListFragment;
import zuo.biao.library.interfaces.AdapterCallBack;
import zuo.biao.library.ui.AlertDialog;

public class MyUploadRecordFragment
        extends BaseHttpListFragment<Comment, ListView, CommentListAdapter>
        implements OnHttpResponseListener {

    private static final String TAG = "MyUploadRecordFragment";
    TextView tv;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_record_list, null);
        super.onCreateView(inflater, container, savedInstanceState);


        initView();
        super.initData();
        super.initEvent();

        srlBaseHttpList.autoRefresh();
        return view;
    }

    @Override
    public void initView() {
        super.initView();
    }

    @Override
    public void setList(final List<Comment> list) {
        setList(new AdapterCallBack<CommentListAdapter>() {
            @Override
            public CommentListAdapter createAdapter() {
                return new CommentListAdapter(context);
            }

            @Override
            public void refreshAdapter() {
                adapter.refresh(list);
            }
        });
    }

    @Override
    public void getListAsync(int page) {
        RequestDataUtil.getMyComment(page, RequestDataUtil.middlePageSize, this);
    }

    @Override
    public List<Comment> parseArray(String json) {
        List<Comment> res = Lists.newArrayList();
        JSONObject resObj;
        try {
            resObj = new JSONObject(json);
            res = JSON.parseArray(resObj.getString("resData"), Comment.class);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return res;
    }

    @Override
    public void onHttpSuccess(int requestCode, int resultCode, String resultMsg, String resultData) {

    }

    @Override
    public void onHttpError(int requestCode, Exception e) {

    }

    private AlertDialog deleteDialog;

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        final Comment comment = adapter.getItem(position);
        deleteDialog = new AlertDialog(context, "提示", "您要删除此条音频吗？", true, 0,
                new AlertDialog.OnDialogButtonClickListener() {
                    @Override
                    public void onDialogButtonClick(int requestCode, boolean isPositive) {
                        if (isPositive) {
                            RequestDataUtil.delComment(comment.getCommentId(),
                                    new OnHttpResponseListenerImpl(MyUploadRecordFragment.this));
                            srlBaseHttpList.removeViewAt(position);
                        } else {
                            deleteDialog.cancel();
                        }
                    }
                });
        deleteDialog.show();
        return true;
    }

}