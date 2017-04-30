package com.carryj.root.contactbook.adapter;

import android.content.Context;
import android.provider.CallLog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.carryj.root.contactbook.R;
import com.carryj.root.contactbook.data.CollectListViewItemData;
import com.carryj.root.contactbook.data.RecordListViewItemData;

import java.util.ArrayList;

import static android.R.drawable.sym_call_incoming;
import static android.R.drawable.sym_call_missed;
import static android.R.drawable.sym_call_outgoing;

/**
 * Created by root on 17/4/20.
 */

public class CollectAdapter extends BaseAdapter{

    private Context mContext;
    private ArrayList<CollectListViewItemData> collectListViewItemDatas;

    public CollectAdapter(Context mContext, ArrayList<CollectListViewItemData> collectListViewItemDatas) {
        this.mContext =mContext;
        this.collectListViewItemDatas = collectListViewItemDatas;

    }

    @Override
    public int getCount() {
        return collectListViewItemDatas.size();
    }

    @Override
    public CollectListViewItemData getItem(int position) {
        return collectListViewItemDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = new ViewHolder();
        if (convertView == null) {
            LayoutInflater mInflater = LayoutInflater.from(mContext);
            convertView = mInflater.inflate(R.layout.collect_list_item, null);

            viewHolder.im_collect_icon = (ImageView) convertView.findViewById(R.id.im_collect_icon);
            viewHolder.tv_collect_display_name = (TextView) convertView.findViewById(R.id.tv_collect_display_name);
            viewHolder.tv_collect_number_type = (TextView) convertView.findViewById(R.id.tv_collect_number_type);


            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        CollectListViewItemData itemData = getItem(position);
        if (itemData != null) {



            if (itemData.getName() != null) { //没存名字就显示电话号码
                viewHolder.tv_collect_display_name.setText(itemData.getName());
            } else {
                viewHolder.tv_collect_display_name.setText(itemData.getStrPhoneNumber());
            }


            viewHolder.tv_collect_number_type.setText(itemData.getPhoneType());

        }


        return convertView;
    }

    private static class ViewHolder {

        ImageView im_collect_icon;
        TextView tv_collect_display_name;
        TextView tv_collect_number_type;

        public ViewHolder() {

        }

    }
}
