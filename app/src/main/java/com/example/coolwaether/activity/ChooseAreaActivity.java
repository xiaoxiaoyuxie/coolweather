package com.example.coolwaether.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coolwaether.R;
import com.example.coolwaether.db.CoolWeatherDB;
import com.example.coolwaether.model.City;
import com.example.coolwaether.model.County;
import com.example.coolwaether.model.Province;
import com.example.coolwaether.util.HttpCallBackListener;
import com.example.coolwaether.util.HttpUtil;
import com.example.coolwaether.util.Utility;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yangyongqiang on 16/9/5.
 */
public class ChooseAreaActivity extends AppCompatActivity {
    public static final int LEVEL_PROVINCE=0;
    public static final int LEVEL_CITY=1;
    public static final int LEVEL_COUNTY=2;
    private ProgressDialog progressDialog;
    private TextView titleView;
    private ListView listView;
    private ArrayAdapter adapter;
    private CoolWeatherDB coolWeatherDB;
    private List dataList=new ArrayList();

    private List modelList;
    private Province selectProvince;
    private City selectCity;
    private int currentLevel=0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_area);
        listView=(ListView) findViewById(R.id.list_view);
        titleView=(TextView) findViewById(R.id.title_text);
        adapter=new ArrayAdapter(this,android.R.layout.simple_expandable_list_item_1,dataList);
        listView.setAdapter(adapter);
        coolWeatherDB=CoolWeatherDB.getInstance(this);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (currentLevel==LEVEL_PROVINCE){
                    selectProvince=(Province)modelList.get(i);
                    queryModels(currentLevel+1);
                }else if (currentLevel==LEVEL_CITY){
                    selectCity=(City) modelList.get(i);
                    queryModels(currentLevel+1);
                }
            }
        });
        queryModels(currentLevel);
    }
    private void queryModels(int levelType){
        switch (levelType){
            case LEVEL_PROVINCE:
                modelList=coolWeatherDB.loadProvince();
                break;
            case LEVEL_CITY:
                modelList=coolWeatherDB.loadCity();
                break;
            case LEVEL_COUNTY:
                modelList=coolWeatherDB.loadCounty();
                break;
            default:
                break;
        }
        if (modelList.size()>0){
            dataList.clear();
            for (Object object:modelList){
                if (object instanceof Province){
                    dataList.add(((Province)object).name);
                }else if (object instanceof City){
                    dataList.add(((City)object).name);
                }else {
                    dataList.add(((County)object).name);
                }
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel=levelType;
            switch (levelType){
                case LEVEL_PROVINCE:
                    titleView.setText("中国");
                    break;
                case LEVEL_CITY:
                    titleView.setText(selectProvince.name);
                    break;
                case LEVEL_COUNTY:
                    titleView.setText(selectCity.name);
                    break;
                default:
                    break;
            }

        }else {
            switch (levelType){
                case LEVEL_PROVINCE:
                    queryFromServer(null,levelType);
                    break;
                case LEVEL_CITY:
                    queryFromServer(selectProvince.code,levelType);
                    break;
                case LEVEL_COUNTY:
                    queryFromServer(selectCity.code,levelType);
                    break;
                default:
                    break;
            }
        }
    }
    private void queryFromServer(final String code, final int levelType){
        String address="";
        switch (levelType){
            case LEVEL_PROVINCE:
                address="http://www.weather.com.cn/data/city3jdata/china.html";
                break;
            case LEVEL_CITY:
                address="http://www.weather.com.cn/data/city3jdata/provshi/"+code+".html";
                break;
            case LEVEL_COUNTY:
                address="http://www.weather.com.cn/data/city3jdata/station/"+code+".html";
                break;
            default:
                break;
        }
        showProgress();
        HttpUtil.sendHttpRequest(address, new HttpCallBackListener() {
            @Override
            public void success(String response) {
                boolean isSuccess= Utility.handleResponse(coolWeatherDB,response,levelType);;
                if (isSuccess){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            hideProgress();
                            queryModels(levelType);
                        }
                    });
                }
            }

            @Override
            public void failed(Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        hideProgress();
                        Toast.makeText(ChooseAreaActivity.this,"加载失败",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
    private void showProgress(){
        if (progressDialog==null){
            progressDialog=new ProgressDialog(this);
            progressDialog.setMessage("正在加载");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }
    private void hideProgress(){
        if (progressDialog!=null){
            progressDialog.dismiss();
        }
    }

    @Override
    public void onBackPressed() {
        if (currentLevel==LEVEL_PROVINCE){
            finish();
        }else {
            queryModels(currentLevel);
        }
    }
}
