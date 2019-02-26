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
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.lenovo.mypoetry.R;

import model.Poetry;
import zuo.biao.library.base.BaseModel;
import zuo.biao.library.base.BaseView;

public class PoetrySearchResItemView extends BaseView<Poetry> implements OnClickListener {
	private static final String TAG = "PoetrySearchResItemView";

	private TextView tv_title;
	private TextView tv_author;
	private TextView tv_id;

	public PoetrySearchResItemView(Activity context, ViewGroup parent) {
		super(context, R.layout.poetry_list_item, parent);
	}




	@SuppressLint("InflateParams")
	@Override
	public View createView() {
		tv_title = findView(R.id.poetry_list_title, this);
		tv_author = findView(R.id.poetry_list_author, this);

		tv_id = findView(R.id.poetry_list_id, this);
		return super.createView();
	}

	@Override
	public void bindView(Poetry data_){
		super.bindView(data_ != null ? data_ : new Poetry());

		tv_title.setText(data_.getTitle());
		tv_author.setText(data_.getAuthor());
		tv_id.setText(data_.getPoetryId().toString());
	}

	@Override
	public void onClick(View v) {
		if (BaseModel.checkCorrect(data) == false) {
			return;
		}

		// TODO 跳转到详情
//		switch (v.getId()) {
//			case R.id.ivUserViewHead:
//				toActivity(WebViewActivity.createIntent(context, data.getName(), data.getHead()));
//				break;
//			default:
//				switch (v.getId()) {
//					case R.id.ivUserViewStar:
//						data.setStarred(!data.getStarred());
//						break;
//					case R.id.tvUserViewSex:
//						data.setSex(data.getSex() == User.SEX_FEMALE ? User.SEX_MAIL : User.SEX_FEMALE);
//						break;
//				}
//				bindView(data);
//				break;
//		}
	}
}