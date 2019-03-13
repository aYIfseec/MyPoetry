package view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.lenovo.mypoetry.R;

import java.text.SimpleDateFormat;

import activity.PoetryActivity;
import model.Poetry;
import model.UserCollection;
import utils.ChineseDateUtil;
import utils.Constant;
import zuo.biao.library.base.BaseModel;
import zuo.biao.library.base.BaseView;

public class UserCollentionItemView
		extends BaseView<UserCollection>
		implements View.OnClickListener, View.OnLongClickListener{
	private static final String TAG = "PoetrySearchResItemView";
	private static final String COLLECT_DATE_PREFIX = "收藏于：";

	public TextView tv_title;
	public TextView tv_time;

	public UserCollentionItemView(Activity context, ViewGroup parent) {
		super(context, R.layout.collection_list_item, parent);
	}



	@SuppressLint("InflateParams")
	@Override
	public View createView() {
		super.createView();
		tv_title = findView(R.id.tv_item_title, this);
		tv_time = findView(R.id.tv_collect_time, this);

		// 将LongClick传递至fragment
		itemView.setOnLongClickListener(this);
		tv_title.setOnLongClickListener(this);
		tv_time.setOnLongClickListener(this);
		return itemView;
	}

	@Override
	public void bindView(UserCollection data_){
		super.bindView(data_ != null ? data_ : new UserCollection());

		tv_title.setText(data_.getTitle());
		tv_time.setText(COLLECT_DATE_PREFIX + ChineseDateUtil.dateToUpper(data_.getCreateTime()));
	}

	@Override
	public void onClick(View v) {
		showShortToast(TAG);
		toActivity(PoetryActivity.createIntent(context, data.getPoetryId()));
	}

	@Override
	public boolean onLongClick(View view) {
		// 将LongClick传递至fragment
		return false;
	}
}