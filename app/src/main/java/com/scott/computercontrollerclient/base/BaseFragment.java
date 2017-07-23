package com.scott.computercontrollerclient.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/7/23 0023.
 */

public abstract class BaseFragment extends Fragment {

    private View mContentView;
    private LayoutInflater mInfalter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if(mInfalter == null) {
            mInfalter = LayoutInflater.from(getActivity());
        }
        int layoutId = initLayout();
        mContentView = mInfalter.inflate(layoutId,container,false);
        ButterKnife.bind(this,mContentView);
        initViews(mContentView);
        return mContentView;
    }

    protected abstract void initViews(View contentView);

    protected abstract int initLayout();
}
