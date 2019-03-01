package fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.lenovo.mypoetry.R;
import com.google.common.collect.Lists;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import adapter.PoetrySearchResAdapter;
import callback.ListViewItemClickCallBack;
import model.Poetry;
import utils.ServerUrlUtil;
import view.PoetrySearchResItemView;
import zuo.biao.library.base.BaseHttpRecyclerFragment;
import zuo.biao.library.interfaces.AdapterCallBack;
import zuo.biao.library.interfaces.CacheCallBack;
import zuo.biao.library.util.JSON;


public class SearchFragment extends BaseHttpRecyclerFragment<Poetry, PoetrySearchResItemView, PoetrySearchResAdapter> implements CacheCallBack<Poetry> {

    private String keyword;
    private ListViewItemClickCallBack clickCallBack;

    public static final int RANGE_ALL = ServerUrlUtil.USER_LIST_RANGE_ALL;
    public static final int RANGE_RECOMMEND = ServerUrlUtil.USER_LIST_RANGE_RECOMMEND;

    private int range = RANGE_ALL;

    public static SearchFragment createInstance() {
        SearchFragment fragment = new SearchFragment();

        Bundle bundle = new Bundle();

        fragment.setArguments(bundle);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        keyword = getArguments().getString("keyword");
        if (StringUtils.isBlank(keyword)) {
            keyword = "杜甫";
        }

        initCache(this);

        //功能归类分区方法，必须调用<<<<<<<<<<
        initView();
        initData();
        initEvent();
        //功能归类分区方法，必须调用>>>>>>>>>>

//        View contentView = inflater.inflate(R.layout.fragment_search, null);

//        onRefresh();
        srlBaseHttpRecycler.autoRefresh();
//        return contentView;
        return view;
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
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // TODO 是否可优化到 PoetrySearchResItemView 中跳转而不是回调？
        clickCallBack.sendPoetryId(((TextView)view.findViewById(R.id.poetry_list_id)).getText().toString());
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
        ServerUrlUtil.getSearchPoetryList(keyword, page, 15, this);
    }

    @Override
    public List<Poetry> parseArray(String json) {

        List<Poetry> res = Lists.newArrayList();
        JSONObject resObj = null;
        try {
            resObj = new JSONObject(json);
            res = JSON.parseArray(resObj.getString("resData"), Poetry.class);
        } catch (JSONException e) {
            Log.e("SearchFragment", e.getMessage());
            e.printStackTrace();
        }



/*        poetryList = Lists.newArrayList();
        JSONObject resObj = new JSONObject(sb.toString());

        JSONObject jsonObject = new JSONObject(resObj.getString("searchByAuthorRes"));
        List<Poetry> temp = JSON.parseArray(jsonObject.getString("resData"), Poetry.class);
        if (CollectionUtils.isNotEmpty(temp)) {
            poetryList.addAll(temp);
        }

        jsonObject = new JSONObject(resObj.getString("searchByTitleRes"));
        temp = JSON.parseArray(jsonObject.getString("resData"), Poetry.class);
        if (CollectionUtils.isNotEmpty(temp)) {
            poetryList.addAll(temp);
        }


        jsonObject = new JSONObject(resObj.getString("searchByType"));
        temp = JSON.parseArray(jsonObject.getString("resData"), Poetry.class);
        if (CollectionUtils.isNotEmpty(temp)) {
            poetryList.addAll(temp);
        }*/

        return res;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        clickCallBack = (ListViewItemClickCallBack) getActivity();
    }

    @Override
    public Class<Poetry> getCacheClass() {
        return Poetry.class;
    }

    @Override
    public String getCacheGroup() {
        return "range=" + range;
    }

    @Override
    public String getCacheId(Poetry data) {
        return data == null ? null : "" + data.getPoetryId();
    }

    @Override
    public int getCacheCount() {
        return 15;
    }
}
