package com.example.davcpe.healthapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by davcpe on 3/12/2015.
 */
public class MyOpenHelper extends SQLiteOpenHelper {

    private  static final String DATABASE_NAME = "Health.db";
    private  static  final  int DATABASE_VERSION =1;
    private static  final String USER_TABLE ="create table userTABLE (_no integer primary key,"+" user_id varchar, user_name text, user_password text, user_officer text);";

    public MyOpenHelper(Context context){

        super(context, DATABASE_NAME,null,DATABASE_VERSION);

    }//Constructor



    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(USER_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
