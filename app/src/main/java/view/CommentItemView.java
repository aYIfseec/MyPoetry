/*Copyright ©2015 TommyLemon(https://github.com/TommyLemon)

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.*/

package view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lenovo.mypoetry.R;

import org.apache.commons.lang3.StringUtils;

import activity.PoetryActivity;
import model.Comment;
import model.UserCollection;
import utils.ChineseDateUtil;
import utils.Constant;
import zuo.biao.library.base.BaseView;

public class CommentItemView extends BaseView<Comment> implements View.OnClickListener {
	private static final String TAG = "CommentItemView";

	public TextView tv_record_id;
	public TextView tv_upload_user_name;
	public TextView tv_upload_time;
	public ImageView play_upload_voice;
	public TextView voice_record_path;
	public TextView tv_play_count;
	public ImageView do_praise;
	public TextView tv_praise_count;

	public CommentItemView(Activity context, ViewGroup parent) {
		super(context, R.layout.record_list_item, parent);
	}



	@SuppressLint("InflateParams")
	@Override
	public View createView() {

		tv_record_id = findViewById(R.id.tv_record_id);
		tv_upload_user_name = findViewById(R.id.tv_upload_user_name);
		tv_upload_time = findViewById(R.id.tv_upload_time);
		play_upload_voice = findViewById(R.id.play_upload_voice);
		voice_record_path = findViewById(R.id.voice_record_path);
		tv_play_count = findViewById(R.id.tv_play_count);
		do_praise = findViewById(R.id.do_praise);
		tv_praise_count = findViewById(R.id.tv_praise_count);

		return super.createView();
	}

	@Override
	public void bindView(Comment comment){
		super.bindView(comment != null ? comment : new Comment());

		tv_record_id.setText(comment.getCommentId());
		tv_upload_user_name.setText(StringUtils.isBlank(comment.getNickName()) ?
				comment.getPoetryTitle() : comment.getNickName());
		tv_upload_time.setText(ChineseDateUtil.dateToUpper(comment.getCreateTime()));
		voice_record_path.setText(comment.getResourceUrl());
		tv_play_count.setText(comment.getReadCount().toString());
		tv_praise_count.setText(comment.getLikeCount().toString());
	}

	@Override
	public void onClick(View v) {
		showShortToast(TAG);
//		Intent intent = new Intent(context, PoetryActivity.class);
//		intent.putExtra(Constant.POETRY_ID, data.getCommentId());
//		toActivity(intent);
	}
}