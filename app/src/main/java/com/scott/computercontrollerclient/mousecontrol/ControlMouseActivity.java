package com.scott.computercontrollerclient.mousecontrol;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.Window;

import com.scott.computercontrollerclient.R;
import com.scott.computercontrollerclient.adapter.VpAdapter;
import com.scott.computercontrollerclient.base.BaseActivity;
import com.scott.computercontrollerclient.event.IEventCustomer;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

@IEventCustomer
public class ControlMouseActivity extends BaseActivity implements ViewPager.OnPageChangeListener{

    @BindView(R.id.vp_main)
    ViewPager vPager;

    @BindView(R.id.tab_layout)
    TabLayout tabLayout;

    private List<Fragment> mFragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mouse_control);

        initViews();
    }

    private void initViews() {
        mFragments = new ArrayList<>();
        mFragments.add(new ControlMouseScreenModeFragment());
        mFragments.add(new ControlMouseTouchPadFragment());
        vPager.setAdapter(new VpAdapter(getSupportFragmentManager(), mFragments));
        tabLayout.setupWithViewPager(vPager);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        ControlMouseScreenModeFragment fragment = (ControlMouseScreenModeFragment) mFragments.get(0);
        if(position == 0) {
            fragment.isShow = true;
        } else {
            fragment.isShow = false;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
