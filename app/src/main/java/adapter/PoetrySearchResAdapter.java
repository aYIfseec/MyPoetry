package adapter;

import android.app.Activity;
import android.view.ViewGroup;

import model.Poetry;
import view.PoetrySearchResItemView;
import zuo.biao.library.base.BaseAdapter;

public class PoetrySearchResAdapter extends BaseAdapter<Poetry, PoetrySearchResItemView> {
	private static final String TAG = "PoetrySearchResAdapter";

	public PoetrySearchResAdapter(Activity context) {
		super(context);
	}

	@Override
	public PoetrySearchResItemView createView(int position, ViewGroup parent) {
		return new PoetrySearchResItemView(context, parent);
	}

	@Override
	public long getItemId(int position) {
		return getItem(position).getPoetryId();
	}



}