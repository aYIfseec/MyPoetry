package fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.example.lenovo.mypoetry.R;
import com.google.common.collect.Lists;

import org.json.JSONObject;

import java.util.List;

import adapter.CommentListAdapter;
import manager.OnHttpResponseListener;
import model.Comment;
import model.Poetry;
import utils.RequestDataUtil;
import zuo.biao.library.base.BaseHttpListFragment;
import zuo.biao.library.interfaces.AdapterCallBack;

public class PoetryRecordListFragment
        extends BaseHttpListFragment<Comment, ListView, CommentListAdapter>
        implements MyBindDataInterface<Poetry>, OnHttpResponseListener {

    private static final String TAG = "RecordListFragment";

    private Poetry poetry;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_record_list, container);
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
    public void initEvent() {
        super.initEvent();

    }


    @Override
    public void bindData(Poetry baseModel) {
        poetry = baseModel;
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
        RequestDataUtil.getCommentForPoetry(poetry.getPoetryId(), page, RequestDataUtil.middlePageSize, this);
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

//    @Override
//    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        Comment comment = adapter.getItem(position);
//
//        switch (view.getId())  {
//            case R.id.do_praise:
//                // TODO 再次点击取消
//                comment.setLikeCount(comment.getLikeCount() + 1);
//                TextView tvLikeCount = view.findViewById(R.id.tv_praise_count);
//                tvLikeCount.setText(comment.getLikeCount().toString());
//                RequestDataUtil.doLike(comment.getCommentId(), new OnHttpResponseListenerImpl(this));
//                break;
//            case R.id.play_upload_voice:
//                if (currPosition != position && currPlayImageView != null) {//若有其它在放的音频，要先停止
//                    currPlayImageView.setImageResource(R.drawable.play);
//                    isPlaying = false;
//                }
//                if (isPlaying) {
//                    //Toast.makeText(context, "停止" + pos, Toast.LENGTH_SHORT).show();
//                    isPlaying = false;
//                    currPlayImageView.setImageResource(R.drawable.play);
//                    currPlayImageView = null;
//                    stopPlay();
//                } else {
//                    isPlaying = true;
//                    currPosition = position;
//                    comment.setReadCount(comment.getReadCount() + 1);
//                    TextView playCount = view.findViewById(R.id.tv_play_count);
//                    playCount.setText(comment.getReadCount().toString());
//                    currPlayImageView = view.findViewById(R.id.play_upload_voice);
//                    currPlayImageView.setImageResource(R.drawable.stop);
//                    doPlay(comment.getResourceUrl(), comment.getCommentId());
//                }
//                break;
//        }
//    }

    @Override
    public void onHttpSuccess(int requestCode, int resultCode, String resultMsg, String resultData) {

    }

    @Override
    public void onHttpError(int requestCode, Exception e) {

    }

}

