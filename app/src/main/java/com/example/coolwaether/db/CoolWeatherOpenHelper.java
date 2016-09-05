package com.example.coolwaether.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by yangyongqiang on 16/9/4.
 */
public class CoolWeatherOpenHelper extends SQLiteOpenHelper {

    public static String Table_Province="Province";
    public static String Table_City="City";
    public static String Table_County="County";

    public static String CREATE_PROVINCE="create table "+Table_Province+" ("
            +" id integer primary key autoincrement, "
            +"province_name text, "
            +"province_code text)";
    public static String CREATE_CITY= "create table "+ Table_City+" ("
            +"id integer primary key autoincrement,"
            +"city_name text, "
            +"city_code text, "
            +"province_id integer)";
    public  static String CREATE_COUNTY= "create table "+Table_County+" ("
            +"id integer primary key autoincrement,"
            +"county_name text"
            +"county_code text"
            +"city_id integer)";
    public CoolWeatherOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_PROVINCE);
        sqLiteDatabase.execSQL(CREATE_CITY);
        sqLiteDatabase.execSQL(CREATE_COUNTY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
