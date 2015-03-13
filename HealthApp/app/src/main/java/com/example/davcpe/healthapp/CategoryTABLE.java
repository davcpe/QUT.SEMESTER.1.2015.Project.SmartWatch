package com.example.davcpe.healthapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by davcpe on 3/13/2015.
 */
public class CategoryTABLE {
    private  MyOpenHelper objMyOpenHelper;
    private SQLiteDatabase writeSQLite, readSQLite;

    public static  final String Category_TABLE = "categoryTABLE";
    public static  final String COLUMN_NO_CATEGORY  ="_no";
    public static  final String COLUMN_ID_CATEGORY = "category_id";
    public static  final String COLUMN_NAME_CATEGORY = "category_name";

    public CategoryTABLE(Context context){

        objMyOpenHelper = new MyOpenHelper(context);
        writeSQLite = objMyOpenHelper.getWritableDatabase();
        readSQLite = objMyOpenHelper.getReadableDatabase();

    }//Constructor

    public String[]listCategoryName(){

        String strListCategoryName[] = null;
        Cursor objCursor = readSQLite.query(Category_TABLE,
                new String[]{COLUMN_NO_CATEGORY,COLUMN_NAME_CATEGORY},null,null,null,null,null);

        objCursor.moveToFirst();
        strListCategoryName = new String[objCursor.getCount()];

        for(int i =0; i<objCursor.getCount();i++){
            strListCategoryName[i] = objCursor.getString(objCursor.getColumnIndex(COLUMN_NAME_CATEGORY));
            objCursor.moveToNext();
        }//for
        objCursor.close();
        return strListCategoryName;
    }//ListCategoryName


    //Add Value to Coffee Table
    public long addValueCoffee(String strCategoryID, String strCategoryName){
        ContentValues objContentValues = new ContentValues();
        objContentValues.put(COLUMN_ID_CATEGORY,strCategoryID);
        objContentValues.put(COLUMN_NAME_CATEGORY, strCategoryName);

        return writeSQLite.insert(Category_TABLE,null,objContentValues);
    }


}
