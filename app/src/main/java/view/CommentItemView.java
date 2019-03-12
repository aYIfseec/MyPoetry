package view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lenovo.mypoetry.R;

import org.apache.commons.lang3.StringUtils;

import activity.PoetryActivity;
import callback.MediaPlayCallBack;
import callback.MediaStopPlayCallBack;
import manager.OnHttpResponseListener;
import manager.OnHttpResponseListenerImpl;
import model.Comment;
import service.AudioService;
import utils.ChineseDateUtil;
import utils.RequestDataUtil;
import zuo.biao.library.base.BaseView;

public class CommentItemView
		extends BaseView<Comment>
		implements View.OnClickListener, OnHttpResponseListener,
		MediaStopPlayCallBack, MediaPlayCallBack {
	private static final String TAG = "CommentItemView";

	public TextView tv_content;
	public TextView tv_upload_user_name;
	public TextView tv_upload_time;
	public ImageView play_voice;
	public TextView voice_record_path;
	public TextView tv_play_count;
	public ImageView do_praise;
	public TextView tv_praise_count;


	private AudioService audioService;
	private boolean isPlaying;

	public CommentItemView(Activity context, ViewGroup parent) {
		super(context, R.layout.record_list_item, parent);
	}



	@SuppressLint("InflateParams")
	@Override
	public View createView() {

		tv_content = findViewById(R.id.tv_content);
		tv_upload_user_name = findViewById(R.id.tv_upload_user_name);
		tv_upload_time = findViewById(R.id.tv_upload_time);
		play_voice = findViewById(R.id.play_upload_voice);
		voice_record_path = findViewById(R.id.voice_record_path);
		tv_play_count = findViewById(R.id.tv_play_count);
		do_praise = findViewById(R.id.do_praise);
		tv_praise_count = findViewById(R.id.tv_praise_count);

		initEven();
		return super.createView();
	}

	private void initEven() {
		do_praise.setOnClickListener(this);
		play_voice.setOnClickListener(this);
	}

	@Override
	public void bindView(Comment comment){
		super.bindView(comment != null ? comment : new Comment());

		audioService = ((PoetryActivity) context).getAudioService();

		tv_content.setText(comment.getContent());
		tv_upload_user_name.setText(StringUtils.isBlank(comment.getNickName()) ?
				comment.getPoetryTitle() : comment.getNickName());
		tv_upload_time.setText(ChineseDateUtil.dateToUpper(comment.getCreateTime()));
		voice_record_path.setText(comment.getResourceUrl());
		tv_play_count.setText(comment.getReadCount().toString());
		tv_praise_count.setText(comment.getLikeCount().toString());
	}

	@Override
	public void onClick(View view) {
		Comment comment = data;

		switch (view.getId())  {
			case R.id.do_praise:
				// TODO 再次点击取消
				if (comment.getLikeStatus() == false) {
					comment.setLikeStatus(true);
					comment.setLikeCount(comment.getLikeCount() + 1);
					do_praise.setImageResource(R.drawable.praise);
					RequestDataUtil.doLike(comment.getCommentId(), new OnHttpResponseListenerImpl(this));

				} else {
					comment.setLikeStatus(false);
					comment.setLikeCount(comment.getLikeCount() - 1);
					do_praise.setImageResource(R.drawable.praised);
					RequestDataUtil.cancelLike(comment.getCommentId(), new OnHttpResponseListenerImpl(this));
				}

				tv_praise_count.setText(comment.getLikeCount().toString());
				break;

			case R.id.play_upload_voice:
				if (isPlaying) {
					audioService.destoryMediaPlayer();
				} else {
					audioService.setPlay(this, this, comment.getResourceUrl());
					audioService.play();
				}
				break;
		}
	}


	@Override
	public void playCallBack() {
		if (data.getReadStatus() == false) {
			data.setReadCount(data.getReadCount() + 1);
			data.setReadStatus(true);
			RequestDataUtil.doPlay(data.getCommentId(), new OnHttpResponseListenerImpl(this));
		}
		isPlaying = true;

		TextView playCount = itemView.findViewById(R.id.tv_play_count);
		playCount.setText(data.getReadCount().toString());
		play_voice.setImageResource(R.drawable.stop);
	}

	@Override
	public void stopPlayCall() {
		isPlaying = false;
		play_voice.setImageResource(R.drawable.play);
	}

	@Override
	public void onHttpSuccess(int requestCode, int resultCode, String resultMsg, String resultData) {

	}

	@Override
	public void onHttpError(int requestCode, Exception e) {

	}

}