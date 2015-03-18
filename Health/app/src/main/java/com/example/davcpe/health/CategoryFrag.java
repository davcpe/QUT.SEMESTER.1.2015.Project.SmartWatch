package com.example.davcpe.health;


import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class CategoryFrag extends Fragment {


    public CategoryFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_category, container,false);
        // Inflate the layout for this fragment
        return rootView;
    }


}
