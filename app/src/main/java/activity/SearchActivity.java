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
import zuo.biao.library.util.StringUtil;

public class SearchActivity
        extends BaseBottomTabActivity
        implements SearchView.OnQueryTextListener, OnBottomDragListener {

    private List<SearchFragment> searchFragmentList = Lists.newArrayList(null, null, null);
    private String searchText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_search);

        initView();
        initData();
        initEvent();
    }

    @Override
    public void initView() {
        super.initView();
//        setDefaultFragment();
    }


    private void setDefaultFragment() {
//        if (poetryFragment != null) {
//            FragmentTransaction transaction = fragmentManager.beginTransaction();
//            transaction.remove(poetryFragment).commit();
//        }
//        Bundle bundle = new Bundle();
//        bundle.putString("poetryId", poetryId);
//        FragmentTransaction transaction = fragmentManager.beginTransaction();
//        poetryFragment = new PoetryFragment();
//        poetryFragment.setArguments(bundle);
//        transaction.replace(R.id.app_main_content, poetryFragment);//
//        transaction.commit();
//        currFragment = poetryFragment;
//        if (todayFragment != null) {
//            FragmentTransaction transaction = fragmentManager.beginTransaction();
//            transaction.remove(todayFragment).commit();
//        }
//        FragmentTransaction transaction = fragmentManager.beginTransaction();
//        todayFragment = TodayFragment.getInstance();
//        transaction.replace(R.id.app_main_content, todayFragment);//
//        transaction.commit();
//        currFragment = todayFragment;
    }

    @Override
    public void initData() {
        super.initData();
    }


    public void getData(String poetryId) {
//        ServerUrlUtil.getPoetry(poetryId, new OnHttpResponseListenerImpl(context));
    }

    @Override
    public void initEvent() {
        super.initEvent();
        SearchView searchView = findViewById(R.id.poetry_search_view);
        searchView.setOnQueryTextListener(this);
    }

//    @Override
//    public void onBackPressed() {
//        DrawerLayout drawer = findViewById(R.id.drawer_layout);
//        if (drawer.isDrawerOpen(GravityCompat.START)) {
//            drawer.closeDrawer(GravityCompat.START);
//        } else {
//            //super.onBackPressed();
//            Intent intent = new Intent(Intent.ACTION_MAIN);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            intent.addCategory(Intent.CATEGORY_HOME);
//            startActivity(intent);
//        }
//    }

    public void switchFragment(Fragment from, Fragment to) {
//        if (currFragment != to) {
//            FragmentTransaction transaction = fragmentManager.beginTransaction();
//            if (! to.isAdded()) {
//                transaction.hide(from).add(R.id.app_main_content, to).commit();
//            } else {
//                transaction.hide(from).show(to).commit();
//            }
//            currFragment = to;
//        }
//        if (currFragment == poetryFragment) {
//            searchView.setVisibility(View.VISIBLE);
//        } else {
//            searchView.setVisibility(View.GONE);
//        }
    }

    @Override
    public boolean onQueryTextSubmit(String queryKeyword) {
        queryKeyword = queryKeyword.trim();
        if (StringUtils.isBlank(queryKeyword) && queryKeyword.equals(searchText)) {
            return false;
        }

        Log.e("搜索内容为：", queryKeyword);
        searchText = queryKeyword;

//        srlBaseHttpRecycler.autoRefresh();
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
//        if (vTabSelectViews == null) {
//            zuo.biao.library.util.Log.e("SearchActivity", "setTabSelection  vTabSelectViews == null >> return;");
//            return;
//        }
//        for (int i = 0; i < vTabSelectViews.length; i++) {
//            if (vTabSelectViews[i] == null) {
//                zuo.biao.library.util.Log.w("SearchActivity",  "setTabSelection  vTabSelectViews[" + i + "] == null >> continue;");
//                continue;
//            }
//            for (int j = 0; j < vTabSelectViews[i].length; j++) {
//                vTabSelectViews[i][j].setSelected(j == position);
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
//        //将Activity的onDragBottom事件传递到Fragment，非必要<<<<<<<<<<<<<<<<<<<<<<<<<<<
//        switch (currentPosition) {
//            case 2:
//                if (searchFragment != null) {
//                    if (rightToLeft) {
//                        searchFragment.selectMan();
//                    } else {
//                        searchFragment.selectPlace();
//                    }
//                }
//                break;
//            default:
//                break;
//        }
//        //将Activity的onDragBottom事件传递到Fragment，非必要>>>>>>>>>>>>>>>>>>>>>>>>>>>
    }

}
