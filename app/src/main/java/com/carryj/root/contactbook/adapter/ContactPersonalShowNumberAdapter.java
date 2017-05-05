package com.carryj.root.contactbook.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.carryj.root.contactbook.R;
import com.carryj.root.contactbook.data.AddContactNumberData;
import com.carryj.root.contactbook.data.PhoneNumberData;

import java.util.ArrayList;


/**
 * Created by root on 17/5/4.
 */

public class ContactPersonalShowNumberAdapter extends RecyclerView.Adapter<ContactPersonalShowNumberAdapter.NumberViewHolder> {


    private Context context;
    private ArrayList<PhoneNumberData> numberData;
    private OnItemClickListener onItemClickListener;
    private OnItemLongClickListener onItemLongClickListener;



    public ContactPersonalShowNumberAdapter(Context context, ArrayList<PhoneNumberData> numberData) {
        this.context = context;
        this.numberData = numberData;
    }

    //  点击事件
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    public interface OnItemClickListener {
        void onClick(int position);
    }

    public interface OnItemLongClickListener {
        boolean onLongClick(View v, int position);
    }


    @Override
    public NumberViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        NumberViewHolder holder = new NumberViewHolder(LayoutInflater.from(
                context).inflate(R.layout.phone_number_show_list_item, parent,
                false));
        return holder;
    }

    @Override
    public void onBindViewHolder(NumberViewHolder holder, int position) {
        holder.type.setText(numberData.get(position).getNumberType());
        holder.number.setText(numberData.get(position).getNumber());
    }

    @Override
    public int getItemCount() {
        return numberData.size();
    }


    class NumberViewHolder extends RecyclerView.ViewHolder {
        ImageView call;
        TextView type;
        TextView number;

        public NumberViewHolder(final View view) {
            super(view);
            call = (ImageView) view.findViewById(R.id.im_contact_personal_show_call_icon);
            type = (TextView) view.findViewById(R.id.tv_contact_personal_show_number_type);
            number = (TextView) view.findViewById(R.id.tv_contact_personal_show_number);

            call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onItemClickListener!=null){
                        onItemClickListener.onClick(getLayoutPosition());
                    }
                }
            });
            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if(onItemLongClickListener!=null) {
                        onItemLongClickListener.onLongClick(v, getLayoutPosition());
                    }
                    return true;
                }
            });

        }

    }
}
