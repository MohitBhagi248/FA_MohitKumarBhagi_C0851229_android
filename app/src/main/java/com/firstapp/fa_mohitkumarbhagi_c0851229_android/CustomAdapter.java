package com.firstapp.fa_mohitkumarbhagi_c0851229_android;

import android.app.Activity;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class CustomAdapter extends ArrayAdapter
{
    Activity activity;
    List<AddressBean> addressBeanList;

    public CustomAdapter(Activity activity, List<AddressBean> addressBeanList)
    {
        super(activity, R.layout.custom, addressBeanList);

        this.activity=activity;
        this.addressBeanList=addressBeanList;
    }


    public void remove(int position) {
        addressBeanList.remove(position);
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = activity.getLayoutInflater().inflate(R.layout.custom,null, true);

        TextView tv_adname=v.findViewById(R.id.tv_adname);
        TextView tv_address=v.findViewById(R.id.tv_address);
        TextView tv_createdOn=v.findViewById(R.id.tv_createdOn);
        TextView tv_visited=v.findViewById(R.id.tv_visited);


        AddressBean ab = addressBeanList.get(position);

        if(ab.getVisited().equalsIgnoreCase("visited"))
        {
            v.setBackgroundColor(Color.GREEN);
        }

        tv_adname.setText(ab.getPlacename());
        tv_address.setText(ab.getAddress());
        tv_createdOn.setText(ab.getCreatedon());
        tv_visited.setText(ab.getVisited());

        return v;
    }
}
