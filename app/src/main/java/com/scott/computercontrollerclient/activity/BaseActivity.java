package com.scott.computercontrollerclient.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;


import com.afollestad.materialdialogs.MaterialDialog;
import com.scott.computercontrollerclient.R;

import java.lang.reflect.Method;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * <p>Author:    shijiale</p>
 * <p>Date:      2017/7/5.</p>
 * <p>Email:     shijl5@lenovo.com</p>
 * <p>Describe:</p>
 */

public class BaseActivity extends AppCompatActivity implements Toolbar.OnMenuItemClickListener{

    @BindView(R.id.tool_bar)
    Toolbar toolBar;
    protected ViewGroup mContent;

    private static BaseActivity mInstacne;
    private MaterialDialog mLoadingDialog;

    public void showLoadingDialog(String title,String content) {
        if(mLoadingDialog == null) {
            mLoadingDialog = new MaterialDialog.Builder(this)
                    .title(title)
                    .content(content)
                    .progress(true, 0)
                    .negativeText("CANCEL")
                    .build();
        }
        mLoadingDialog.setCancelable(false);
        mLoadingDialog.setTitle(title);
        mLoadingDialog.setContent(content);
        mLoadingDialog.setProgress(0);
        mLoadingDialog.show();
    }


    public void showMsgDialog(String msg) {
        new MaterialDialog.Builder(this)
                .title("提示")
                .content(msg)
                .canceledOnTouchOutside(false)
                .positiveText("OK")
                .show();
    }

    public void showConfigmDialog(String msg, MaterialDialog.SingleButtonCallback callback) {
        new MaterialDialog.Builder(this)
                .title("提示")
                .content(msg)
                .canceledOnTouchOutside(false)
                .positiveText("OK")
                .onPositive(callback)
                .negativeText("CANCEL")
                .show();
    }

    public void dismissLoadingDialog() {
        if(mLoadingDialog != null && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
        }
    }

    public static BaseActivity getInstacne() {
        return mInstacne;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        mContent = (ViewGroup) LayoutInflater.from(this).inflate(R.layout.activity_base,null,false);
        super.setContentView(mContent);
        mInstacne = this;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mInstacne = null;
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        LayoutInflater.from(this).inflate(layoutResID,mContent,true);
        ButterKnife.bind(this);
        setSupportActionBar(toolBar);
        toolBar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolBar.setTitleTextColor(Color.WHITE);
        toolBar.setOverflowIcon(getResources().getDrawable(R.drawable.ic_menu_white_24dp));
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        toolBar.setOnMenuItemClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        menu.findItem(R.id.menu_add).setVisible(true);
//        menu.findItem(R.id.menu_del).setVisible(true);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (menu != null) {
            if (menu.getClass().getSimpleName().equals("MenuBuilder")) {
                try {
                    Method m = menu.getClass().getDeclaredMethod(
                            "setOptionalIconsVisible", Boolean.TYPE);
                    m.setAccessible(true);
                    m.invoke(menu, true);
                } catch (NoSuchMethodException e) {
                } catch (Exception e) {
                }
            }
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
//        if (item.getItemId() == R.id.menu_del) {
//            delTasks();
//        } else if(item.getItemId() == R.id.menu_add) {
//            addTasks();
//        }
        return false;
    }

}
