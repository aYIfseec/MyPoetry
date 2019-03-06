package fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.lenovo.mypoetry.R;

import java.util.ArrayList;
import java.util.List;


public class PoetryFragment extends Fragment{
    private static String TAG = "PoetryFragment";

    private List<String> tabIndicators;//tab标题
    private List<Fragment> tabFragments;//碎片
    private PoetryFragment.ContentPagerAdapter contentAdapter;

    private ViewPager viewPager;
    private View viewFragment;
    private TabLayout tabLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        viewFragment = inflater.inflate(R.layout.fragment_poetry,container);
        viewPager = viewFragment.findViewById(R.id.vp_content);
        tabLayout = viewFragment.findViewById(R.id.tl_tab);
//        context = (MainActivity)getActivity();
//        mainActivity = (MainActivity)getActivity();
        String poetryId = getArguments().getString("poetryId");

//        getData(poetryId);
        initTab();

        return viewFragment;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
//            String requestUrl = getContext().getPoetryId();
//            if (requestUrl != null) {
//                getData(requestUrl);
//                mainActivity.resetPoetryId();
//            }
        }
    }

    private void initTab() {
        tabIndicators = new ArrayList<>();
        tabIndicators.add("原文");
        tabIndicators.add("注释");
        tabIndicators.add("音频");

        tabFragments = new ArrayList<>();
        tabFragments.add(new TabContentFragment());
        tabFragments.add(new NotesFragment());
        tabFragments.add(new RecordListFragment());

        contentAdapter = new ContentPagerAdapter(getFragmentManager());
        viewPager.setAdapter(contentAdapter);
        viewPager.setOffscreenPageLimit(3);

        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        ViewCompat.setElevation(tabLayout,0);
        tabLayout.setupWithViewPager(viewPager);
        for (int i = 0; i < tabIndicators.size(); i++) {
            TabLayout.Tab tabItem = tabLayout.getTabAt(i);
            if (tabItem != null) {
                tabItem.setCustomView(R.layout.tab_item_layout);
                TextView tv = tabItem.getCustomView().findViewById(R.id.tv_menu_item);
                tv.setText(tabIndicators.get(i));
            }
        }
        tabLayout.getTabAt(0).getCustomView().setSelected(true);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    // Tab 适配器
    class ContentPagerAdapter extends FragmentPagerAdapter {

        public ContentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return tabFragments.get(position);
        }

        @Override
        public int getCount() {
            return tabIndicators.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabIndicators.get(position);
        }
    }
}
