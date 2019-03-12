package fragment;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.example.lenovo.mypoetry.R;
import com.google.common.collect.Lists;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import adapter.CommentListAdapter;
import manager.OnHttpResponseListener;
import manager.OnHttpResponseListenerImpl;
import model.Comment;
import model.Poetry;
import service.AudioService;
import utils.RequestDataUtil;
import zuo.biao.library.base.BaseHttpListFragment;
import zuo.biao.library.interfaces.AdapterCallBack;

public class PoetryRecordListFragment
        extends BaseHttpListFragment<Comment, ListView, CommentListAdapter>
        implements MyBindDataInterface<Poetry>, OnHttpResponseListener {

    private static final String TAG = "RecordListFragment";

    private Poetry poetry;

//    private View view;
//    private ListView recordListView;
//    private TextView no_data_hint;
//    private SwipeRefreshLayout swipeRefreshLayout;

    private int currPosition = -1;
    private ImageView currPlayImageView;

    private boolean isPlaying = false;
    private AudioService audioService;
    private Handler playHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            currPlayImageView.callOnClick();
        }
    };

    private Handler stopHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //TODO 刷新音频时 停止播放
            if (isPlaying) {
                currPlayImageView.callOnClick();
            }
        }
    };

    private Intent intentPlay;
    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder serviceBinder) {
            audioService = ((AudioService.MyBinder) serviceBinder).getService();
            Log.e("audioService", audioService.toString()+"");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            audioService = null;
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_record_list, container);
        super.onCreateView(inflater, container, savedInstanceState);


        initView();
        super.initData();
        super.initEvent();

//        audioService = ((MainActivity)getActivity()).getAudioService();
        intentPlay = new Intent(getActivity(), AudioService.class);
        getActivity().bindService(intentPlay, conn, Context.BIND_AUTO_CREATE);

        srlBaseHttpList.autoRefresh();
        return view;
    }

    @Override
    public void initView() {
        super.initView();
//
//        recordListView = view.findViewById(R.id.record_list_view);
//        no_data_hint = view.findViewById(R.id.no_data_hint);
//        no_data_hint.setText("还没有人朗读这首诗词，快去上传你的朗读音频吧！");
//        recordListView.setEmptyView(no_data_hint);
//        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
//        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                swipeRefreshLayout.setRefreshing(true);
////                String url = RequestDataUtil.getRecordListUrl(poetry.getId(), 1);
////                new PoetryRecordListFragment.GetRecordsTask(recordListView, recordListAdapter).execute(url);
//                swipeRefreshLayout.setRefreshing(false);
//            }
//        });
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Comment comment = adapter.getItem(position);

        switch (view.getId())  {
            case R.id.do_praise:
                // TODO 再次点击取消
                comment.setLikeCount(comment.getLikeCount() + 1);
                TextView tvLikeCount = view.findViewById(R.id.tv_praise_count);
                tvLikeCount.setText(comment.getLikeCount().toString());
                RequestDataUtil.doLike(comment.getCommentId(), new OnHttpResponseListenerImpl(this));
                break;
            case R.id.play_upload_voice:
                if (currPosition != position && currPlayImageView != null) {//若有其它在放的音频，要先停止
                    currPlayImageView.setImageResource(R.drawable.play);
                    isPlaying = false;
                }
                if (isPlaying) {
                    //Toast.makeText(context, "停止" + pos, Toast.LENGTH_SHORT).show();
                    isPlaying = false;
                    currPlayImageView.setImageResource(R.drawable.play);
                    currPlayImageView = null;
                    stopPlay();
                } else {
                    isPlaying = true;
                    currPosition = position;
                    comment.setReadCount(comment.getReadCount() + 1);
                    TextView playCount = view.findViewById(R.id.tv_play_count);
                    playCount.setText(comment.getReadCount().toString());
                    currPlayImageView = view.findViewById(R.id.play_upload_voice);
                    currPlayImageView.setImageResource(R.drawable.stop);
                    doPlay(comment.getResourceUrl(), comment.getCommentId());
                }
                break;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
//        context.unregisterReceiver(myBroadcastReceiver);
        getActivity().unbindService(conn);
        getActivity().stopService(intentPlay);
    }

    @Override
    public void onHttpSuccess(int requestCode, int resultCode, String resultMsg, String resultData) {

    }

    @Override
    public void onHttpError(int requestCode, Exception e) {

    }

    private void doPlay(String recordPath, String commentId) {
        RequestDataUtil.doPlay(commentId, new OnHttpResponseListenerImpl(this));
        String url = RequestDataUtil.getPlayNetPath(recordPath);
        audioService.setPlayUrl(url);
        audioService.setHandler(playHandler);
        audioService.play();
    }

    private void stopPlay() {
        audioService.destoryMediaPlayer();
    }

}

