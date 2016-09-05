package com.example.coolwaether.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.coolwaether.model.City;
import com.example.coolwaether.model.County;
import com.example.coolwaether.model.Province;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yangyongqiang on 16/9/4.
 */
public class CoolWeatherDB {
    public static final String DB_NAME="COOL_WEATHER";
    public static final int VERSION=1;
    private static CoolWeatherDB coolWeatherDB;
    private static SQLiteDatabase db;
    public synchronized static CoolWeatherDB getInstance(Context context){
        if (coolWeatherDB==null){
            CoolWeatherOpenHelper dbHelper=new CoolWeatherOpenHelper(context,DB_NAME,null,VERSION);
            db=dbHelper.getWritableDatabase();
            coolWeatherDB=new CoolWeatherDB();
        }
        return coolWeatherDB;
    }
    public void save(Object model){
        if (model==null)return;
        String className=model.getClass().getName();
        if (model instanceof Province){
            ContentValues values=new ContentValues();
            values.put("province_name",((Province)model).name);
            values.put("province_code",((Province)model).code);
            db.insert(CoolWeatherOpenHelper.Table_Province,null,values);
        }else if (model instanceof City){
            ContentValues values=new ContentValues();
            values.put("city_name",((City)model).name);
            values.put("city_code",((City)model).code);
            db.insert(CoolWeatherOpenHelper.Table_City,null,values);
        }else if (model instanceof County){
            ContentValues values=new ContentValues();
            values.put("county_name",((County)model).name);
            values.put("county_code",((County)model).code);
            db.insert(CoolWeatherOpenHelper.Table_County,null,values);
        }
    }
    public List<Province> loadProvince(){
        List<Province> list=new ArrayList<Province>();
        Cursor cursor =db.query(CoolWeatherOpenHelper.Table_Province,null,null,null,null,null,null);
        if (cursor.moveToFirst()){
            do {
                Province model=new Province();
                model.id=cursor.getInt(cursor.getColumnIndex("id"));
                model.name=cursor.getString(cursor.getColumnIndex("province_name"));
                model.code=cursor.getString(cursor.getColumnIndex("province_code"));
                list.add(model);
            }while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }
    public List<City> loadCity(){
        List<City> list=new ArrayList<City>();
        Cursor cursor =db.query(CoolWeatherOpenHelper.Table_City,null,null,null,null,null,null);
        if (cursor.moveToFirst()){
            do {
                City model=new City();
                model.id=cursor.getInt(cursor.getColumnIndex("id"));
                model.name=cursor.getString(cursor.getColumnIndex("city_name"));
                model.code=cursor.getString(cursor.getColumnIndex("city_code"));
                list.add(model);
            }while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }
    public List<County> loadCounty(){
        List<County> list=new ArrayList<County>();
        Cursor cursor =db.query(CoolWeatherOpenHelper.Table_County,null,null,null,null,null,null);
        if (cursor.moveToFirst()){
            do {
                County model=new County();
                model.id=cursor.getInt(cursor.getColumnIndex("id"));
                model.name=cursor.getString(cursor.getColumnIndex("county_name"));
                model.code=cursor.getString(cursor.getColumnIndex("county_code"));
                list.add(model);
            }while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }
}
