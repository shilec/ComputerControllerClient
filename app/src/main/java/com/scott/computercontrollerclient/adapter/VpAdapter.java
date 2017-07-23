package com.scott.computercontrollerclient.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * <p>Author:    shijiale</p>
 * <p>Date:      2017/4/18.</p>
 * <p>Email:     shijl5@lenovo.com</p>
 * <p>Describe:</p>
 */

public class VpAdapter extends FragmentPagerAdapter {

    private List<Fragment> mDatas;
    private String[] mTitles = {"屏显模式","触摸板"};
    private VpAdapter(FragmentManager fm) {
        super(fm);
    }

    public VpAdapter(FragmentManager fm, List<Fragment> datas) {
        super(fm);
        mDatas = datas;
    }

    @Override
    public Fragment getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles[position];
    }
}
