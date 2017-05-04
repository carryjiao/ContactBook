package com.carryj.root.contactbook.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import com.carryj.root.contactbook.R;
import com.carryj.root.contactbook.data.AddContactNumberData;

import java.util.ArrayList;


/**
 * Created by root on 17/5/4.
 */

public class AddContactNumberAdapter extends RecyclerView.Adapter<AddContactNumberAdapter.NumberViewHolder> {


    private Context context;
    private ArrayList<AddContactNumberData> numberData;
    private OnItemListener listener;


    public AddContactNumberAdapter(Context context, ArrayList<AddContactNumberData> numberData) {
        this.context = context;
        this.numberData = numberData;
    }

    //  点击事件
    public void setOnItemListener(OnItemListener onItemListener) {
        this.listener = onItemListener;
    }
    public interface OnItemListener {
        void onClick(View v, int position, AddContactNumberData numberItemData);
    }

    @Override
    public NumberViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        NumberViewHolder holder = new NumberViewHolder(LayoutInflater.from(
                context).inflate(R.layout.add_contact_number_item, parent,
                false));
        return holder;
    }

    @Override
    public void onBindViewHolder(NumberViewHolder holder, int position) {
        holder.number.setText(numberData.get(position).getPhoneNumber());
    }

    @Override
    public int getItemCount() {
        return numberData.size();
    }

    public void addNumberData(int position) {
        numberData.add(position,new AddContactNumberData());
        notifyItemInserted(position);
    }

    public void  deleteNumberData(int position) {
        numberData.remove(position);
        notifyItemRemoved(position);
    }

    class NumberViewHolder extends RecyclerView.ViewHolder {
        ImageView delete;
        Spinner type;
        TextView number;

        public NumberViewHolder(final View view) {
            super(view);
            delete = (ImageView) view.findViewById(R.id.im_add_contact_number_item_delete);
            type = (Spinner) view.findViewById(R.id.sp_add_contact_number_item_type);
            number = (TextView) view.findViewById(R.id.et_add_contact_number_item_number);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener!=null){
                        listener.onClick(view, getLayoutPosition(), numberData.get(getLayoutPosition()));
                    }
                }
            });
        }

    }
}
