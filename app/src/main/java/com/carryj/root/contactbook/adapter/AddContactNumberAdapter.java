package com.carryj.root.contactbook.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import com.carryj.root.contactbook.R;
import com.carryj.root.contactbook.data.PhoneNumberData;

import java.util.ArrayList;


/**
 * Created by root on 17/5/4.
 */

public class AddContactNumberAdapter extends RecyclerView.Adapter<AddContactNumberAdapter.NumberViewHolder> {


    private Context context;
    private ArrayList<PhoneNumberData> numberData;
    private OnItemListener listener;
    private OnItemSpinnerListener spinnerListener;
    private TextChangeListener textChangeListener;
    private boolean updataFlag = false;


    public AddContactNumberAdapter(Context context, ArrayList<PhoneNumberData> numberData, boolean updataFlag) {
        this.context = context;
        this.numberData = numberData;
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
                context).inflate(R.layout.add_contact_number_item, parent,
                false));
        return holder;
    }

    @Override
    public void onBindViewHolder(NumberViewHolder holder, int position) {
        holder.number.setText(numberData.get(position).getNumber());
        if(updataFlag) {
            int spinnerIndex  = 0;

            if(numberData.get(position).getNumberType()!=null){
                switch (numberData.get(position).getNumberType()) {

                    case "住宅"://1
                        spinnerIndex = 0;
                        break;

                    case "手机"://2
                        spinnerIndex = 1;
                        break;

                    case "单位"://3
                        spinnerIndex = 2;
                        break;

                    case "单位传真":
                        spinnerIndex = 3;
                        break;

                    case "住宅传真":
                        spinnerIndex = 4;
                        break;

                    case "寻呼机":
                        spinnerIndex = 5;
                        break;

                    case "其他":
                        spinnerIndex = 6;
                        break;

                    case "回拨号码":
                        spinnerIndex = 7;
                        break;

                    case "车载电话":
                        spinnerIndex = 8;
                        break;

                    case "公司总机":
                        spinnerIndex = 9;
                        break;

                    case "ISDN":
                        spinnerIndex = 10;
                        break;

                    case "总机":
                        spinnerIndex = 11;
                        break;

                    case "其他传真":
                        spinnerIndex = 12;
                        break;

                    case "无线装置":
                        spinnerIndex = 13;
                        break;

                    case "电报":
                        spinnerIndex = 14;
                        break;

                    case "TTY TDD":
                        spinnerIndex = 15;
                        break;

                    case "单位手机":
                        spinnerIndex = 16;
                        break;

                    case "单位寻呼机":
                        spinnerIndex = 17;
                        break;

                    case "助理":
                        spinnerIndex = 18;
                        break;

                    case "彩信":
                        spinnerIndex = 19;
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
        return numberData.size();
    }

    public void addNumberData(int position) {
        numberData.add(position,new PhoneNumberData());
        notifyItemInserted(position);
    }

    public void  deleteNumberData(int position) {
        numberData.remove(position);
        notifyItemRemoved(position);
    }

    class NumberViewHolder extends RecyclerView.ViewHolder {
        ImageView delete;
        Spinner spinner;
        TextView number;

        public NumberViewHolder(final View view) {
            super(view);
            delete = (ImageView) view.findViewById(R.id.im_add_contact_number_item_delete);
            spinner = (Spinner) view.findViewById(R.id.sp_add_contact_number_item_type);
            number = (TextView) view.findViewById(R.id.et_add_contact_number_item_number);

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

            number.addTextChangedListener(new TextWatcher() {
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
