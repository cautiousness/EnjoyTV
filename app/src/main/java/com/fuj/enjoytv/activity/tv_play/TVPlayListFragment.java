package com.fuj.enjoytv.activity.tv_play;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fuj.enjoytv.R;
import com.fuj.enjoytv.model.tv.Playlist;
import com.fuj.enjoytv.utils.AppManager;
import com.fuj.enjoytv.utils.JsonUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gang
 */
public class TVPlayListFragment extends Fragment {
    private static final float MIN_SCALE = 0.75f;
    private static final float MIN_ALPHA = 0.75f;

    private TabAdapter mTabAdapter;
    private TVPlaySubscribeFragmentAdapter mFragmentAdapter;
    private VerticalViewPager mViewPager;
    private VerticalTabLayout mTablayout;
    private List<TVPlaySubscribeFragment> fragments = new ArrayList<>();

    public static Fragment newInstance() {
        return new TVPlayListFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTabAdapter = new TabAdapter();
        mFragmentAdapter = new TVPlaySubscribeFragmentAdapter(getFragmentManager(), fragments, new ArrayList<Playlist>());
        getData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab, container, false);
        initViewPager(view);
        initTabLayout(view);
        return view;
    }

    private void initViewPager(View view) {
        mViewPager = view.findViewById(R.id.verticalviewpager);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                mTablayout.setCurrentTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });
        mViewPager.setAdapter(mFragmentAdapter);
        mViewPager.setPageMargin(1);
        mViewPager.setPageMarginDrawable(new ColorDrawable(getResources().getColor(R.color.gray_light)));
        mViewPager.setPageTransformer(true, new ViewPager.PageTransformer() {
            @Override
            public void transformPage(View view, float position) {
                int pageWidth = view.getWidth();
                int pageHeight = view.getHeight();
                if (position <= 1) {
                    float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
                    float vertMargin = pageHeight * (1 - scaleFactor) / 2;
                    float horzMargin = pageWidth * (1 - scaleFactor) / 2;
                    view.setTranslationY(position < 0 ? vertMargin - horzMargin / 2 : -vertMargin + horzMargin / 2);
                    view.setScaleX(scaleFactor);
                    view.setScaleY(scaleFactor);
                    view.setAlpha(MIN_ALPHA + (scaleFactor - MIN_SCALE) / (1 - MIN_SCALE) * (1 - MIN_ALPHA));
                } else {
                    view.setAlpha(0);
                }
            }
        });
    }

    private void initTabLayout(View view) {
        mTablayout = view.findViewById(R.id.tablayout);
        mTablayout.addOnTabSelectedListener(new VerticalTabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabView tab, int position) {
                mViewPager.setCurrentItem(position);
            }
        });
        mTablayout.setTabAdapter(mTabAdapter);
    }

    public class TabAdapter {
        private List<Playlist> dataLists;

        public TabAdapter() {
            dataLists = new ArrayList<>();
        }

        public int getCount() {
            return dataLists.size();
        }

        public TabView.TabTitle getTitle(int position) {
            return new TabView.TabTitle(getContext())
                .setContent(dataLists.get(position).day);
        }

        public void updateData(List<Playlist> list) {
            dataLists = list;
            if(fragments.size() < 1) {
                for (Playlist playlist : dataLists) {
                    TVPlaySubscribeFragment fragment = new TVPlaySubscribeFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(AppManager.BUNDLE_PLAYLIST, playlist);
                    fragment.setArguments(bundle);
                    fragments.add(fragment);
                }
            }
            mFragmentAdapter.updateData(dataLists);
        }
    }

    private void getData() {
        String content = JsonUtils.readJsonFile(getContext(), "playlist");
        mTabAdapter.updateData(JsonUtils.getObjectList(content, Playlist.class));
    }
}
