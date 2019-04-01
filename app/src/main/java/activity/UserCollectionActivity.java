package activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.SearchView;
import android.util.Log;

import com.example.lenovo.mypoetry.R;

import org.apache.commons.lang3.StringUtils;

import fragment.UserCollectionFragment;
import zuo.biao.library.base.BaseActivity;

public class UserCollectionActivity
        extends BaseActivity
        implements SearchView.OnQueryTextListener {

    private SearchView searchView;

    private String searchText;
    private UserCollectionFragment userCollectionFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);
        initView();
        initData();
        initEvent();
    }

    @Override
    public void initView() {

        searchView = findViewById(R.id.collection_search_view);
        searchView.clearFocus();

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        userCollectionFragment = new UserCollectionFragment();
        transaction.replace(R.id.collection_search_res_fragment, userCollectionFragment);//
        transaction.commit();
    }

    @Override
    public void initData() {
    }

    public void initEvent() {
        SearchView searchView = findViewById(R.id.collection_search_view);
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

        userCollectionFragment.refresh(searchText);
        return false;
    }

    // 当搜索内容改变时触发该方法
    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

}
