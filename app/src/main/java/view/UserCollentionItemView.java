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

public class UserCollentionItemView extends BaseView<UserCollection> implements View.OnClickListener {
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
		tv_title = findView(R.id.tv_item_title, this);
		tv_time = findView(R.id.tv_collect_time, this);
		return super.createView();
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
		Intent intent = new Intent(context, PoetryActivity.class);
		intent.putExtra(Constant.POETRY_ID, data.getPoetryId().toString());
		toActivity(intent);
	}
}