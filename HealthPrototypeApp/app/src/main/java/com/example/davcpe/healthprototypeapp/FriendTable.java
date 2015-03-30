package com.example.davcpe.healthprototypeapp;

/**
 * Created by davcpe on 3/30/2015.
 */
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class FriendTable {

    private MyOpenHelper objMyOpenHelper;
    private SQLiteDatabase writeSQLite, readSQLite;

    public static final String TABLE_FRIEND = "friendTABLE";
    public static final String COLUMN_NO  ="_no";
    public static final String COLUMN_USER_ID = "user_id";
    public static final String COLUMN_FRIEND_NAME = "friend_name";
    public static  final String COLUMN_FRIEND_ID = "friend_id";


    public FriendTable(Context context) {
        objMyOpenHelper = new MyOpenHelper(context);
        writeSQLite = objMyOpenHelper.getWritableDatabase();
        readSQLite = objMyOpenHelper.getReadableDatabase();
    }// Constructor

    public String[]searchUser(String strUSerName){
        try {

            String strData[]= null;
            Cursor objCursor = readSQLite.query(TABLE_FRIEND,new String[] {COLUMN_NO, COLUMN_USER_ID,COLUMN_FRIEND_NAME,COLUMN_FRIEND_ID},COLUMN_USER_ID+"=?",new String[] {String.valueOf(strUSerName)}, null,null,null,null);
            if (objCursor != null) {
                if(objCursor.moveToFirst()){
                    strData = new String[objCursor.getColumnCount()];
                    strData[0] = objCursor.getString(0);
                    strData[1] = objCursor.getString(1);
                    strData[2] = objCursor.getString(2);
                    strData[3] = objCursor.getString(3);
                }// if2
            }//if1

            objCursor.close();
            return strData;
        }catch (Exception e){
            return null;
        }


    }//search User

    public long addValueUser ( String strUserID, String strFriendName, String strFriendID){
        ContentValues objContentValues = new ContentValues();


        objContentValues.put(COLUMN_USER_ID, strUserID);
        objContentValues.put(COLUMN_FRIEND_NAME, strFriendName);
        objContentValues.put(COLUMN_FRIEND_ID, strFriendID);
        return writeSQLite.insert(TABLE_FRIEND, null,objContentValues);
    } //addValueToUser

    public String[]listName(String strUser_id ){

        String strlistName[] = null;
       // Cursor objCursor = readSQLite.query(TABLE_FRIEND, new String[]{COLUMN_NO,COLUMN_FRIEND_NAME},null,null,null,null,null,null);

        Cursor objCursor = readSQLite.query(TABLE_FRIEND,new String[] {COLUMN_NO, COLUMN_USER_ID,COLUMN_FRIEND_NAME,COLUMN_FRIEND_ID},COLUMN_USER_ID+"=?",new String[] {String.valueOf(strUser_id)}, null,null,null,null);

        objCursor.moveToFirst();
        strlistName = new String[objCursor.getCount()];

        for(int i=0;i<objCursor.getCount();i++){
            strlistName[i] = objCursor.getString(objCursor.getColumnIndex(COLUMN_FRIEND_NAME));
            objCursor.moveToNext();
        }//for
        objCursor.close();
        return  strlistName;

    }//ListPrice


}
