package fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.common.collect.Lists;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import adapter.PoetrySearchResAdapter;
import model.Poetry;
import utils.RequestDataUtil;
import view.PoetrySearchResItemView;
import zuo.biao.library.base.BaseHttpRecyclerFragment;
import zuo.biao.library.interfaces.AdapterCallBack;
import zuo.biao.library.util.JSON;


public class SearchFragment
        extends BaseHttpRecyclerFragment<Poetry, PoetrySearchResItemView, PoetrySearchResAdapter> {

    private String keyword;
    private Integer searchType;

    public static final int SEARCH_BY_TITLE = 0;
    public static final int SEARCH_BY_AUTHOR = 1;
    public static final int SEARCH_BY_TYPE = 2;
    public static final int SEARCH_BY_ = 3;


    public static SearchFragment getInstance(String queryKeyword, int searchType) {
        SearchFragment fragment = new SearchFragment();
        if (StringUtils.isNoneBlank(queryKeyword)) {
            fragment.keyword = queryKeyword;
        }
        fragment.searchType = searchType;
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        //功能归类分区方法，必须调用<<<<<<<<<<
        initView();
        initData();
        initEvent();
        //功能归类分区方法，必须调用>>>>>>>>>>

        return view;
    }

    @Override
    public void initView() {
        super.initView();
    }

    @Override
    public void initData() {
        super.initData();

        if (StringUtils.isNotBlank(keyword)) {
            srlBaseHttpRecycler.autoRefresh();
        }
    }

    public void refresh(String keyword) {
        if (StringUtils.isNotBlank(keyword)) {
            this.keyword = keyword;
            srlBaseHttpRecycler.autoRefresh();
        }
    }

    @Override
    public void initEvent() {
        super.initEvent();
    }

    @Override
    public void setList(final List<Poetry> list) {
        setList(new AdapterCallBack<PoetrySearchResAdapter>() {
            @Override
            public PoetrySearchResAdapter createAdapter() {
                return new PoetrySearchResAdapter(context);
            }

            @Override
            public void refreshAdapter() {
                adapter.refresh(list);
            }
        });
    }


    @Override
    public void getListAsync(int page) {
        switch (searchType) {
            case SEARCH_BY_TITLE:
                RequestDataUtil.searchPoetryListByTitle(keyword, page, RequestDataUtil.middlePageSize, this);
                break;
            case SEARCH_BY_AUTHOR:
                RequestDataUtil.searchPoetryListByAuthor(keyword, page, RequestDataUtil.middlePageSize, this);
                break;
            case SEARCH_BY_TYPE:
                RequestDataUtil.searchPoetryListByType(keyword, page, RequestDataUtil.middlePageSize, this);
                break;
            default:break;
        }
    }

    @Override
    public List<Poetry> parseArray(String json) {
        List<Poetry> res = Lists.newArrayList();
        JSONObject resObj;
        try {
            resObj = new JSONObject(json);
            res = JSON.parseArray(resObj.getString("resData"), Poetry.class);
        } catch (JSONException e) {
            Log.e("SearchFragment", e.getMessage());
            e.printStackTrace();
        }
        return res;
    }
}
