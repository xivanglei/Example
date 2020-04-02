package com.dalongtech.testapplication.base;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.dalongtech.testapplication.utils.ListUtil;

import java.util.Arrays;
import java.util.List;

/**
 * Author:xianglei
 * Date: 2019/1/10 9:23 AM
 * Description:ViewPager 的 Fragment适配器
 */
public class BaseFragmentAdapter extends FragmentPagerAdapter {

    private List<Fragment> mFragments;
    private List<String> mTitles;

    public BaseFragmentAdapter(FragmentManager fm, List<Fragment> fragments, String[] titles) {
        this(fm, fragments);
        mTitles = Arrays.asList(titles);
    }

    public BaseFragmentAdapter(FragmentManager fm, List<Fragment> fragments, List<String> titles) {
        this(fm, fragments);
        mTitles = titles;
    }

    public BaseFragmentAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        mFragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if(ListUtil.isEmpty(mTitles)) {
            return null;
        }
        return mTitles.get(position);
    }
}
