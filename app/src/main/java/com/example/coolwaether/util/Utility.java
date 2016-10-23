package com.example.coolwaether.util;

import android.text.TextUtils;

import com.example.coolwaether.db.CoolWeatherDB;
import com.example.coolwaether.model.City;
import com.example.coolwaether.model.County;
import com.example.coolwaether.model.Province;

/**
 * Created by yangyongqiang on 16/9/5.
 */
public class Utility {
    public synchronized static boolean handleResponse(CoolWeatherDB coolWeatherDB,String response,int type,int id){
        if (TextUtils.isEmpty(response))return false;
        response=response.replace("{","");
        response=response.replace("}","");
        String[] allValues=response.split(",");
        for (String value:allValues){
            value = value.replace("\"","").replace("\"","").replace("\"","").replace("\"","");
            String[] array=value.split(":");
            switch (type){
                case 0:
                    Province p=new Province();
                    p.name=array[1];
                    p.code=array[0];
                    coolWeatherDB.save(p);
                    break;
                case 1:
                    City c=new City();
                    c.name=array[1];
                    c.code=array[0];
                    c.province_id=id;
                    coolWeatherDB.save(c);
                    break;
                case 2:
                    County cy=new County();
                    cy.name=array[1];
                    cy.code=array[0];
                    cy.city_id=id;
                    coolWeatherDB.save(cy);
                    break;
                default:
                    break;
            }
        }
        return true;
    }
}
