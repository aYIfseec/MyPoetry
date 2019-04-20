package view;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.lenovo.mypoetry.R;

import zuo.biao.library.base.BaseView;
import zuo.biao.library.model.Entry;


public class UserCommetView extends BaseView<Entry<String, String>> implements OnClickListener {
	private static final String TAG = "DemoComplexView";

	public UserCommetView(Activity context) {
		super(context, R.layout.user_comment_view);
	}

	public EditText editTextComment;


	@Override
	public View createView() {

		editTextComment = findView(R.id.tvEditTextCommentConent);
		return super.createView();
	}


	@Override
	public void bindView(Entry<String, String> data_){
		super.bindView(data_ != null ? data_ : new Entry<String, String>());

	}


	@Override
	public void onClick(View v) {
		if (data == null) {
			return;
		}
//		switch (v.getId()) {
//		case R.id.bottom_menu_btn_forward:
//			tvDemoComplexViewName.setText("New " + StringUtil.getString(tvDemoComplexViewName));
//			break;
//		default:
//			break;
//		}
	}




}
