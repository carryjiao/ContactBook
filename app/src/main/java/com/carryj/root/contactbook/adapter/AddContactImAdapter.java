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
import com.carryj.root.contactbook.data.ImData;

import java.util.ArrayList;


/**
 * Created by root on 17/5/4.
 */

public class AddContactImAdapter extends RecyclerView.Adapter<AddContactImAdapter.NumberViewHolder> {

    private Context context;
    private ArrayList<ImData> imData;
    private OnItemListener listener;
    private OnItemSpinnerListener spinnerListener;
    private TextChangeListener textChangeListener;
    private boolean updataFlag = false;


    public AddContactImAdapter(Context context, ArrayList<ImData> imData, boolean updataFlag) {
        this.context = context;
        this.imData = imData;
        this.updataFlag = updataFlag;
    }

    //  点击事件
    public void setOnItemListener(OnItemListener onItemListener) {
        this.listener = onItemListener;
    }

    public void setOnItemSpinnerListener(OnItemSpinnerListener onItemSpinnerListener) {
        this.spinnerListener = onItemSpinnerListener;
    }

    public void myAddTextChangeListener(TextChangeListener textChangeListener) {
        this.textChangeListener = textChangeListener;
    }


    public interface OnItemListener {
        void onClick(int position);
    }

    public interface OnItemSpinnerListener {
        void onItemSelected(AdapterView<?> parent, View view, int position, long id, int listposition);
        void onNothingSelected(AdapterView<?> parent, int listposition);
    }

    public interface TextChangeListener {
        public void beforeTextChanged(CharSequence s, int start, int count, int after, int listposition);
        public void onTextChanged(CharSequence s, int start, int before, int count, int listposition);
        public void afterTextChanged(Editable s, int listposition);
    }

    @Override
    public NumberViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        NumberViewHolder holder = new NumberViewHolder(LayoutInflater.from(
                context).inflate(R.layout.add_contact_im_item, parent,
                false));
        return holder;
    }

    @Override
    public void onBindViewHolder(NumberViewHolder holder, int position) {
        holder.im.setText(imData.get(position).getIm());
        if(updataFlag) {
            int spinnerIndex  = 0;
            if(imData.get(position).getImType()!=null) {
                switch (imData.get(position).getImType()) {
                    case "MSN":
                        spinnerIndex = 0;
                        break;
                    case "雅虎":
                        spinnerIndex = 1;
                        break;
                    case "Skype":
                        spinnerIndex = 2;
                        break;
                    case "QQ":
                        spinnerIndex = 3;
                        break;
                    case "环聊":
                        spinnerIndex = 4;
                        break;
                    case "ICQ":
                        spinnerIndex = 5;
                        break;
                    case "Jabber":
                        spinnerIndex = 6;
                        break;
                    case "Windows Live":
                        spinnerIndex = 7;
                        break;
                    default:
                        spinnerIndex = 0;
                        break;
                }
            }else {
                spinnerIndex = 0;
            }
            holder.spinner.setSelection(spinnerIndex);
        }
    }

    @Override
    public int getItemCount() {
        return imData.size();
    }

    public void addImData(int position) {
        imData.add(position,new ImData());
        notifyItemInserted(position);
    }

    public void  deleteImData(int position) {
        imData.remove(position);
        notifyItemRemoved(position);
    }

    class NumberViewHolder extends RecyclerView.ViewHolder {
        ImageView delete;
        Spinner spinner;
        TextView im;

        public NumberViewHolder(final View view) {
            super(view);
            delete = (ImageView) view.findViewById(R.id.im_add_contact_im_item_delete);
            spinner = (Spinner) view.findViewById(R.id.sp_add_contact_im_item_type);
            im = (TextView) view.findViewById(R.id.et_add_contact_im_item_number);

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener!=null){
                        listener.onClick(getLayoutPosition());
                    }
                }
            });


            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if(spinnerListener!=null) {
                        spinnerListener.onItemSelected(parent, view, position, id, getLayoutPosition());
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    if(spinnerListener!=null) {
                        spinnerListener.onNothingSelected(parent, getLayoutPosition());
                    }
                }
            });

            im.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    if(textChangeListener!=null){
                        textChangeListener.beforeTextChanged(s, start, count, after, getLayoutPosition());
                    }
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if(textChangeListener!=null){
                        textChangeListener.onTextChanged(s, start, before, count, getLayoutPosition());
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                    if(textChangeListener!=null){
                        textChangeListener.afterTextChanged(s, getLayoutPosition());
                    }
                }
            });
        }

    }
}
