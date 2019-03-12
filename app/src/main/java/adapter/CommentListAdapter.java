package adapter;

import android.app.Activity;
import android.view.ViewGroup;

import model.Comment;
import view.CommentItemView;
import zuo.biao.library.base.BaseAdapter;

public class CommentListAdapter extends BaseAdapter<Comment, CommentItemView> {
	private static final String TAG = "CommentListAdapter";

	public CommentListAdapter(Activity context) {
		super(context);
	}

	@Override
	public CommentItemView createView(int position, ViewGroup parent) {
		return new CommentItemView(context, parent);
	}

	@Override
	public long getItemId(int position) {
		return getItem(position).getId();
	}



}