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
import com.carryj.root.contactbook.data.RecordListViewItemData;

import java.util.ArrayList;

import static android.R.drawable.sym_call_incoming;
import static android.R.drawable.sym_call_missed;
import static android.R.drawable.sym_call_outgoing;

/**
 * Created by root on 17/4/20.
 */

public class RecordAdapter extends BaseAdapter{

    private Context mContext;
    private ArrayList<RecordListViewItemData> recordListViewItemDatas;

    public RecordAdapter(Context mContext, ArrayList<RecordListViewItemData> recordListViewItemDatas) {
        this.mContext =mContext;
        this.recordListViewItemDatas = recordListViewItemDatas;

    }

    @Override
    public int getCount() {
        return recordListViewItemDatas.size();
    }

    @Override
    public RecordListViewItemData getItem(int position) {
        return recordListViewItemDatas.get(position);
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
            convertView = mInflater.inflate(R.layout.record_list_item, null);

            viewHolder.im_record_contact_type = (ImageView) convertView.findViewById(R.id.im_record_contact_type);
            viewHolder.tv_record_display_name = (TextView) convertView.findViewById(R.id.tv_record_display_name);
            viewHolder.tv_record_type = (TextView) convertView.findViewById(R.id.tv_record_type);
            viewHolder.tv_record_time = (TextView) convertView.findViewById(R.id.tv_record_time);
            viewHolder.im_record_detail = (ImageView) convertView.findViewById(R.id.im_record_detail);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        RecordListViewItemData itemData = getItem(position);
        if (itemData != null) {

            switch (itemData.getcontactType()) {
                case CallLog.Calls.INCOMING_TYPE:
                    viewHolder.im_record_contact_type.setImageResource(sym_call_incoming);
                    break;
                case CallLog.Calls.OUTGOING_TYPE:
                    viewHolder.im_record_contact_type.setImageResource(sym_call_outgoing);
                    break;
                case CallLog.Calls.MISSED_TYPE:
                    viewHolder.im_record_contact_type.setImageResource(sym_call_missed);
                    break;
                default:
                    break;
            }

            if (itemData.getStrCachedName() != null) { //没存名字就显示电话号码
                viewHolder.tv_record_display_name.setText(itemData.getStrCachedName());
            } else {
                viewHolder.tv_record_display_name.setText(itemData.getStrNumber());
            }


            //viewHolder.tv_record_type.setText(itemData.getPhoneType());

            viewHolder.tv_record_time.setText(itemData.getDate());
        }


        return convertView;
    }

    private static class ViewHolder {

        ImageView im_record_contact_type;
        TextView tv_record_display_name;
        TextView tv_record_type;
        TextView tv_record_time;
        ImageView im_record_detail;

        public ViewHolder() {

        }

    }
}
