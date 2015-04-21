package com.appdever.healthapp;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class MyDbHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "HealthApp";
    private static final int DB_VERSION = 1;
    
    public static final String TB_SLEEP = "sleep";
    
    public static final String COL_ID = "id";
    public static final String COL_START_H = "start_h";
    public static final String COL_START_M = "start_m";
    public static final String COL_START_S = "start_s";
    public static final String COL_START_AMPM = "start_ampm";
    public static final String COL_START_DAY = "start_day";
    public static final String COL_STOP_H = "stop_h";
    public static final String COL_STOP_M = "stop_m";
    public static final String COL_STOP_S = "stop_s";
    public static final String COL_STOP_AMPM = "stop_ampm";
    public static final String COL_STOP_DAY = "stop_day";

    
    public MyDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        
        
        db.execSQL("CREATE TABLE "+TB_SLEEP
                + "("
                + COL_ID + " TEXT , "
                + COL_START_H + " LONG , "
                + COL_START_M + " LONG , "
                + COL_START_S + " LONG , "
                + COL_START_AMPM + " LONG , "
                + COL_START_DAY + " LONG , "
                + COL_STOP_H + " LONG , "
                + COL_STOP_M + " LONG , "
                + COL_STOP_S + " LONG , "
                + COL_STOP_AMPM + " LONG , "
                + COL_STOP_DAY + " LONG);");
        

    	    	
        db.execSQL("INSERT INTO "+TB_SLEEP+"("
        		+ COL_ID + ","+ COL_START_H + ","+ COL_START_M + ","+ COL_START_S+","+ COL_START_AMPM+","+ COL_START_DAY+
                ","+ COL_STOP_H + ","+ COL_STOP_M + ","+ COL_STOP_S + ","+ COL_STOP_AMPM +","+ COL_STOP_DAY +") VALUES ('0','0','0','0','0','0','0','0','0','0','0');");
        
        
    }

    public void onStartTimer(SQLiteDatabase db,long start_h,long start_m,long start_s,long start_ampm,long start_day) {

        db.execSQL("UPDATE "+TB_SLEEP+" SET "
                + COL_START_H + " = "+ start_h
                +", "+ COL_START_M + " = " + start_m
                +", "+ COL_START_S + " = " + start_s
                +", "+ COL_START_AMPM + " = " + start_ampm
                +", "+ COL_START_DAY + " = " + start_day
                +"  WHERE " + COL_ID + " = '0' ");

    }



    public void onStopTimer(SQLiteDatabase db,long stop_h,long stop_m,long stop_s,long stop_ampm,long stop_day) {

        db.execSQL("UPDATE "+TB_SLEEP+" SET "
                + COL_STOP_H + " = " + stop_h
                +", "+ COL_STOP_M + " = " + stop_m
                +", "+ COL_STOP_S + " = " + stop_s
                +", "+ COL_STOP_AMPM + " = " + stop_ampm
                +", "+ COL_STOP_DAY + " = " + stop_day
                +"  WHERE " + COL_ID + " = '0' ");

    }
    

    
    
    
    public void onUpgrade(SQLiteDatabase db, int oldVersion
            , int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TB_SLEEP);
        onCreate(db);
    }
    
   
}
