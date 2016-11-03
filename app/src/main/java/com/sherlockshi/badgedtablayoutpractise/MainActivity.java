package com.sherlockshi.badgedtablayoutpractise;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<String> mPageTitleList = new ArrayList<String>();
    private List<Fragment> mFragmentList = new ArrayList<Fragment>();
    private List<Integer> mBadgeCountList = new ArrayList<Integer>();

    private SimpleFragmentPagerAdapter mPagerAdapter;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    private List<BadgeView> mBadgeViews;

    private int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initFragments();
        initView();
    }

    private void initFragments() {
        mPageTitleList.add("Tab1");
        mPageTitleList.add("Tab2");
        mPageTitleList.add("Tab3");

        mBadgeCountList.add(6);
        mBadgeCountList.add(count++);
        mBadgeCountList.add(166);

        for (int i = 0; i < mPageTitleList.size(); i++) {
            mFragmentList.add(PageFragment.getInstance(mPageTitleList.get(i)));
        }
    }

    private void initView() {
        findViewById(R.id.btn_add_badge).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBadgeCountList.set(1, count++);
                setUpTabBadge();
            }
        });

        mTabLayout = (TabLayout) findViewById(R.id.tab_layout);
        mViewPager = (ViewPager) findViewById(R.id.view_pager);

        mPagerAdapter = new SimpleFragmentPagerAdapter(this, getSupportFragmentManager(),
                        mFragmentList, mPageTitleList, mBadgeCountList);
        mViewPager.setAdapter(mPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);

        initBadgeViews();
        setUpTabBadge();
    }

    private void initBadgeViews() {
        if (mBadgeViews == null) {
            mBadgeViews = new ArrayList<BadgeView>();
            for (int i = 0; i < mFragmentList.size(); i++) {
                BadgeView tmp = new BadgeView(this);
                tmp.setBadgeMargin(0, 6, 10, 0);
                tmp.setTextSize(10);
                mBadgeViews.add(tmp);
            }
        }
    }

    /**
     * 设置Tablayout上的标题的角标
     */
    private void setUpTabBadge() {
        // 1. 最简单
//        for (int i = 0; i < mFragmentList.size(); i++) {
//            mBadgeViews.get(i).setTargetView(((ViewGroup) mTabLayout.getChildAt(0)).getChildAt(i));
//            mBadgeViews.get(i).setText(formatBadgeNumber(mBadgeCountList.get(i)));
//        }

        // 2. 最实用
        for (int i = 0; i < mFragmentList.size(); i++) {
            TabLayout.Tab tab = mTabLayout.getTabAt(i);

            // 更新Badge前,先remove原来的customView,否则Badge无法更新
            View customView = tab.getCustomView();
            if (customView != null) {
                ViewParent parent = customView.getParent();
                if (parent != null) {
                    ((ViewGroup) parent).removeView(customView);
                }
            }

            // 更新CustomView
            tab.setCustomView(mPagerAdapter.getTabItemView(i));
        }

        // 需加上以下代码,不然会出现更新Tab角标后,选中的Tab字体颜色不是选中状态的颜色
        mTabLayout.getTabAt(mTabLayout.getSelectedTabPosition()).getCustomView().setSelected(true);
    }
}
