package fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.example.lenovo.mypoetry.R;


import org.apache.commons.lang3.StringUtils;

import java.util.Calendar;
import java.util.Date;

import activity.PoetryActivity;
import activity.SearchActivity;
import manager.OnHttpResponseListener;
import manager.OnHttpResponseListenerImpl;
import model.Poetry;
import utils.ChineseDateUtil;
import utils.Constant;
import utils.ServerUrlUtil;
import zuo.biao.library.base.BaseFragment;

public class TodayFragment
        extends BaseFragment
        implements OnHttpResponseListener {

    private static final String TAG = "TodayFragment";

    private Poetry poetry;


    public static TodayFragment getInstance() {
        return new TodayFragment();
    }


    @Override
    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        initView();
        initData();
        initEvent();

        return view;
    }

    @Override
    public void initView() {
        view = inflater.inflate(R.layout.fragment_today, null);
        Fragment hotPoetryListFragment = HotPoetryListFragment.getInstance();
        getChildFragmentManager()
                .beginTransaction()
                .add(R.id.hot_poetry_fragment, hotPoetryListFragment)
                .commit();
    }

    @Override
    public void initData() {

        // 日期数据
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int day2 = day % 10;
        int day1 = day / 10;

        TextView textViewYear = findViewById(R.id.tv_calendar_year);
        textViewYear.setText(ChineseDateUtil.yearToUpper(year));

        TextView textViewMonth = findViewById(R.id.tv_calendar_month);
        textViewMonth.setText(ChineseDateUtil.monthToUppder(ChineseDateUtil.monthToUppder(month)));

        ImageView imageViewDay1 = findViewById(R.id.img_calendar_day_1);
        imageViewDay1.setImageResource(getMipmapResource("number_" + day1));
        ImageView imageViewDay2 = findViewById(R.id.img_calendar_day_2);
        imageViewDay2.setImageResource(getMipmapResource("number_" + day2));

        // today poetry
        ServerUrlUtil.getPoetry(null, new OnHttpResponseListenerImpl(this));
    }

    public int getMipmapResource(String resName) {
        Context ctx = context.getBaseContext();
        int resId = getResources().getIdentifier(resName, "mipmap", ctx.getPackageName());
        //如果没有在"mipmap"下找到imageName,将会返回0
        return resId;
    }

    @Override
    public void initEvent() {
        TextView searchTextView = findViewById(R.id.poetry_search_text_pos);
        searchTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(context, SearchActivity.class);
                toActivity(intent);
            }
        });

        LinearLayout linearLayout = findViewById(R.id.ll_calendar);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, PoetryActivity.class);
                intent.putExtra(Constant.POETRY_ID, poetry.getPoetryId().toString());
                toActivity(intent);
            }
        });
    }

    @Override
    public void onHttpSuccess(int requestCode, int resultCode, String resultMsg, String resultData) {
        Log.e(TAG, "onResponse: " + resultData);

        poetry = JSON.parseObject(resultData, Poetry.class);
        if (poetry == null || StringUtils.isBlank(poetry.getContent())) {
            return;
        }
        TextView textView = findViewById(R.id.tv_calendar_dailyPoem);
        String sentence = poetry.getContent();
        sentence = sentence.substring(0, sentence.indexOf("\n"));
        sentence = sentence.replace("，", "，\n");
        textView.setText(sentence);
    }

    @Override
    public void onHttpError(int requestCode, Exception e) {
    }



}
