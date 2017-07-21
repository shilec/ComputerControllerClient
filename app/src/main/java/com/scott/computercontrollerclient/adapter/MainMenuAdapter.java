package com.scott.computercontrollerclient.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.scott.computercontrollerclient.R;
import com.scott.computercontrollerclient.moudle.MenuItem;

import java.util.List;

/**
 * <p>Author:    shijiale</p>
 * <p>Date:      2017/7/21.</p>
 * <p>Email:     shijl5@lenovo.com</p>
 * <p>Describe:</p>
 */

public class MainMenuAdapter extends BaseAdapter{
    private List<MenuItem> mDatas;
    private LayoutInflater mInflater;

    public MainMenuAdapter(List<MenuItem> datas) {
        mDatas = datas;
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if(convertView == null) {
            if(mInflater == null) {
                mInflater = LayoutInflater.from(parent.getContext());
            }
            convertView = mInflater.inflate(R.layout.item_main_menu,parent,false);
            holder = new ViewHolder();
            holder.icon = (ImageView) convertView.findViewById(R.id.iv_menu);
            holder.title = (TextView) convertView.findViewById(R.id.tv_title);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        MenuItem item = mDatas.get(position);
        holder.title.setText(item.title);
        holder.icon.setImageResource(item.iconId);
        return convertView;
    }

    private final class ViewHolder {
        ImageView icon;
        TextView title;
    }
}
