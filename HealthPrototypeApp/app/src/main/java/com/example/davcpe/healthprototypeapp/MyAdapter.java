package com.example.davcpe.healthprototypeapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Created by davcpe on 30/03/15.
 */
public class MyAdapter extends BaseAdapter {
    //Expicit
    private Context objContext;
    private  String[] str_Listname;


    public MyAdapter(Context context,String[] strListname) {

        this.objContext = context;
        this.str_Listname = strListname;
    }

    @Override
    public int getCount() {
        return str_Listname.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater objLayoutInflater = (LayoutInflater)objContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = objLayoutInflater.inflate(R.layout.friend_list_view_row, parent, false);

        //Setup Text Price
        TextView listViewName = (TextView)view.findViewById(R.id.txtShowName);
        listViewName.setText(str_Listname[position]);

//        //Setup Image
//        ImageView listImageCoffee = (ImageView)view.findViewById(R.id.imgCoffee);
//        listImageCoffee.setBackgroundResource(intMyTarget[position]);
//

        return view;
    }
}
