package com.example.davcpe.healthapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by davcpe on 3/13/2015.
 */
public class MyAdapter extends BaseAdapter {

    //Explicit
    private Context objContext;
    private String[] strCategoryName;
    private int[] intMyTarget;

    public MyAdapter(Context context, String[] strName, int[] targetID){

        this.objContext = context;
        this.strCategoryName = strName;
        this.intMyTarget = targetID;



    }//Constructor

    @Override
    public int getCount() {
        return  strCategoryName.length;
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
        View view = objLayoutInflater.inflate(R.layout.activity_physical_row, parent, false);


        //Setup Text Coffee
        TextView listViewCoffee  =  (TextView)view.findViewById(R.id.txtShowActName);
        listViewCoffee.setText(strCategoryName[position]);

//        Setup Text Price
//        TextView listViewPrice = (TextView)view.findViewById(R.id.txtShowPrice);
//        listViewPrice.setText(strPriceCoffee[position]);

        //Setup Image
        ImageView listImageCoffee = (ImageView)view.findViewById(R.id.imgAct);
        listImageCoffee.setBackgroundResource(intMyTarget[position]);

        return view;
    }//getView




    }

