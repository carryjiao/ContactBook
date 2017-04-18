package com.carryj.root.contactbook.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.carryj.root.contactbook.R;

/**
 * Created by root on 17/4/10.
 */

public class RecordFragement extends Fragment implements OnClickListener {

    //private GridView gridView;
    private TextView tv_record_all;
    private TextView tv_record_missed_call;
    private ImageView iv_record_box;

    public RecordFragement() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_record,null);
        initView(view);
        return view;
    }

    private void initView(View view) {
        tv_record_all = (TextView) view.findViewById(R.id.tv_record_all);
        tv_record_missed_call = (TextView) view.findViewById(R.id.tv_record_missed_call);
        iv_record_box = (ImageView) view.findViewById(R.id.iv_record_box);
        //gridView = (GridView) view.findViewById(R.id.grid_view_dial_number);

        tv_record_all.setOnClickListener(this);
        tv_record_missed_call.setOnClickListener(this);
        iv_record_box.setOnClickListener(this);

        /*data_list = new ArrayList<Map<String, String>>();
        getData();
        String[] from = {"number","str"};
        int[] to = {R.id.tv_dial_number,R.id.tv_dial_char};
        simpleAdapter = new SimpleAdapter(getContext(),data_list,R.layout.dial_number_item,from,to);
        gridView.setAdapter(simpleAdapter);*/

    }

    /*public List<Map<String,String>> getData() {
        for (int i = 0; i < number.length; i++) {
            Map<String,String> map = new HashMap<String, String>();
            map.put("number",number[i]);
            map.put("str",str[i]);
            data_list.add(map);
        }

        return data_list;
    }*/

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
