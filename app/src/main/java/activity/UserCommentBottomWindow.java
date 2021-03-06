package activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import common.ResourceType;
import manager.OnHttpResponseListener;
import manager.OnHttpResponseListenerImpl;
import model.Poetry;
import utils.RequestDataUtil;
import view.UserCommetView;
import zuo.biao.library.base.BaseViewBottomWindow;
import zuo.biao.library.model.Entry;
import zuo.biao.library.util.JSON;


public class UserCommentBottomWindow
		extends BaseViewBottomWindow<Entry<String, String>, UserCommetView>
		implements OnClickListener, OnHttpResponseListener {
	private static final String TAG = "UserCommentBottomWindow";

	private String filePath;
	private String comment;
	private Poetry poetry;


	//启动方法<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	public static Intent createIntent(Context context, Poetry poetry, String filePath) {
		return new Intent(context, UserCommentBottomWindow.class)
				.putExtra("poetryJsonStr", JSON.toJSONString(poetry))
				.putExtra("filePath", filePath);
	}

	//启动方法>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//功能归类分区方法，必须调用<<<<<<<<<<
		initView();
		initData();
		initEvent();
		//功能归类分区方法，必须调用>>>>>>>>>>

	}


	//UI显示区(操作UI，但不存在数据获取或处理代码，也不存在事件监听代码)<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	@Override
	public void initView() {//必须调用
		super.initView();

	}



	//UI显示区(操作UI，但不存在数据获取或处理代码，也不存在事件监听代码)>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>










	//Data数据区(存在数据获取或处理代码，但不存在事件监听代码)<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	@Override
	public void initData() {//必须调用
		super.initData();
		filePath = getIntent().getStringExtra("filePath");
		poetry = JSON.parseObject(getIntent().getStringExtra("poetryJsonStr"), Poetry.class);
		data = new Entry<String, String>("filePath", filePath);
		data.setId(1);
		containerView.bindView(data);
	}

	@Override
	public String getTitleName() {
		return "上传朗读音频";
	}
	@Override
	public String getReturnName() {
		return null;
	}
	@Override
	public String getForwardName() {
		return null;
	}

	@Override
	protected UserCommetView createView() {
		return new UserCommetView(context);
	}

	@Override
	protected void setResult() {
		//示例代码<<<<<<<<<<<<<<<<<<<
		setResult(RESULT_OK, new Intent().putExtra(RESULT_DATA, TAG + " saved"));
		//示例代码>>>>>>>>>>>>>>>>>>>
	}

	//Data数据区(存在数据获取或处理代码，但不存在事件监听代码)>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>








	//Event事件区(只要存在事件监听代码就是)<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	@Override
	public void initEvent() {//必须调用
		super.initEvent();

//		containerView.commentUpload.setOnClickListener(this);
	}

	@Override
	public void onForwardClick(View v) {
		showProgressDialog("上传中...");
		comment = containerView.editTextComment.getText().toString();
		RequestDataUtil.uploadRecord(filePath, new OnHttpResponseListenerImpl(this));

//		setResult();
//		finish();
	}


	@Override
	public void onClick(View v) {

	}

	@Override
	public void onHttpSuccess(int requestCode, int resultCode, String resultMsg, String resultData) {
		if (requestCode == RequestDataUtil.UPLOAD_AUDIO_CODE) {
			if (resultData != null) {
				RequestDataUtil.doComment(poetry, comment, resultData, ResourceType.AUDIO, new OnHttpResponseListenerImpl(this));
			} else {
				showShortToast(resultMsg);
			}
			dismissProgressDialog();
		} else if (requestCode == RequestDataUtil.DO_COMMENT_CODE) {
			showShortToast(resultMsg);
			setResult();
			finish();
		}
	}

	@Override
	public void onHttpError(int requestCode, Exception e) {

	}


	//生命周期、onActivityResult<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<



	//生命周期、onActivityResult>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


	//Event事件区(只要存在事件监听代码就是)>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>







	//内部类,尽量少用<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<



	//内部类,尽量少用>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

}