package activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.util.Log;

import com.example.lenovo.mypoetry.R;
import com.google.common.collect.Lists;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

import fragment.SearchFragment;
import zuo.biao.library.base.BaseBottomTabActivity;
import zuo.biao.library.interfaces.OnBottomDragListener;

public class SearchActivity
        extends BaseBottomTabActivity
        implements SearchView.OnQueryTextListener, OnBottomDragListener {

    private List<SearchFragment> searchFragmentList = Lists.newArrayList(null, null, null);
    private String searchText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_search, this);

        initView();
        initData();
        initEvent();
    }

    @Override
    public void initView() {
        super.initView();
    }

    @Override
    public void initData() {
        super.initData();
    }

    @Override
    public void initEvent() {
        super.initEvent();
        SearchView searchView = findViewById(R.id.poetry_search_view);
        searchView.setOnQueryTextListener(this);
    }

    @Override
    public boolean onQueryTextSubmit(String queryKeyword) {
        queryKeyword = queryKeyword.trim();
        if (StringUtils.isBlank(queryKeyword) && queryKeyword.equals(searchText)) {
            return false;
        }

        Log.e("搜索内容为：", queryKeyword);
        searchText = queryKeyword;

        SearchFragment searchFragment = (SearchFragment) getFragment(currentPosition);
        searchFragment.refresh(searchText);
        return false;
    }

    // 当搜索内容改变时触发该方法
    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }




    /*************
     *
     * tab 与 fragment 管理
     *
     * ************/

    @Override
    protected int[] getTabClickIds() {
        // 监听tab切换的tab
        return new int[]{R.id.llBottomTabTab0, R.id.llBottomTabTab1, R.id.llBottomTabTab2};
    }

    @Override
    protected int[][] getTabSelectIds() {
        // 选中需要改状态的View id
        return new int[][]{
                new int[]{R.id.tvBottomTabTab0, R.id.tvBottomTabTab1, R.id.tvBottomTabTab2} //底部文字
        };
    }

    @Override
    protected void selectTab(int position) {

    }

//    /**设置选中状态
//     * @param position
//     */
//    @Override
//    protected void setTabSelection(int position) {
//        super.setTabSelection(position);
//        if (vTabSelectViews == null) {
//            return;
//        }
//        for (int i = 0; i < vTabSelectViews.length; i++) {
//            if (vTabSelectViews[i] == null) {
//                continue;
//            }
//            for (int j = 0; j < vTabSelectViews[i].length; j++) {
//                vTabSelectViews[i][j].();
//            }
//        }
//    }

    @Override
    public int getFragmentContainerResId() {
        return R.id.poetry_search_res_fragment;
    }

    @Override
    protected Fragment getFragment(int position) {
        Fragment fragment = searchFragmentList.get(position);
        //  && StringUtils.isNotBlank( ((SearchFragment)fragment).getKeyword() )
        if (fragment != null) {
            return fragment;
        }
        switch (position) {
            case 0:
                searchFragmentList.set(0, SearchFragment.getInstance(searchText, SearchFragment.SEARCH_BY_TITLE));
                break;
            case 1:
                searchFragmentList.set(1, SearchFragment.getInstance(searchText, SearchFragment.SEARCH_BY_AUTHOR));
                break;
            case 2:
                searchFragmentList.set(2, SearchFragment.getInstance(searchText, SearchFragment.SEARCH_BY_TYPE));
                break;

                default:break;
        }
        return searchFragmentList.get(position);
    }

    @Override
    public void onDragBottom(boolean rightToLeft) {
//        showShortToast("滑动" + rightToLeft);
        // 将Activity的onDragBottom事件传递到Fragment
        if (rightToLeft) {
            if (currentPosition == (getCount() - 1)) {
                return;
            }
            selectFragment(currentPosition + 1);
        } else {
            if (currentPosition == 0) {
                return;
            }
            selectFragment(currentPosition - 1);
        }
    }

}
