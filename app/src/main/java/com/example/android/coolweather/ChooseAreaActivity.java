package com.example.android.coolweather;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.coolweather.DB.CoolWeatherDB;
import com.example.android.coolweather.Model.City;
import com.example.android.coolweather.Model.County;
import com.example.android.coolweather.Model.Province;
import com.example.android.coolweather.Util.HttpCallbackListener;
import com.example.android.coolweather.Util.HttpUtil;
import com.example.android.coolweather.Util.Utility;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.LogRecord;

/**
 * Created by Danny.W on 7/31/2016.
 */
public class ChooseAreaActivity extends AppCompatActivity{

    private static final int PROVINCE_LEVEL = 1;
    private static final int CITY_LEVEL = 2;
    private static final int COUNTY_LEVEL = 3;
    private Handler handler;

    private ListView listView;
    private ArrayAdapter<String> adapter;
    private TextView title_text;
    private CoolWeatherDB coolWeatherDB;
    private ProgressDialog progressDialog;
    private List<String> dataList;

    private List<Province> provinceList;
    private List<City> cityList;
    private List<County> countyList;

    private Province selectedProvince;
    private City selectedCity;
    private County selectedCounty;

    private int currentLevel;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.choose_area);

        listView = (ListView) findViewById(R.id.list_view);
        title_text = (TextView) findViewById(R.id.title_text);

        dataList = new ArrayList<>();
        coolWeatherDB = CoolWeatherDB.getInstance(this);
        adapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                dataList
        );
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (currentLevel == PROVINCE_LEVEL) {
                    selectedProvince = provinceList.get(position);
                    queryCities();
                } else if (currentLevel == CITY_LEVEL) {
                    selectedCity = cityList.get(position);
                    queryCounties();
                }
            }
        });

        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                closeProgressDialog();
                String type = (String) msg.obj;
                if(msg.what == 1){
                    switch (type){

                        case "Province":
                            queryProvinces();
                            break;
                        case "City":
                            queryCities();
                            break;
                        case "County":
                            queryCounties();
                            break;
                    }
                    return true;
                }
                else if(msg.what == 2){
                    closeProgressDialog();
                    Toast.makeText(ChooseAreaActivity.this, "Loading Error", Toast.LENGTH_SHORT).show();
                }

                return false;
            }
        });

        queryProvinces();
    }




    public void queryProvinces(){

        provinceList = coolWeatherDB.loadProvinces();
        if(provinceList.size() > 0){

            dataList.clear();
            for(Province p: provinceList){
                dataList.add(p.getProvince_name());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            title_text.setText("China");
            currentLevel = PROVINCE_LEVEL;
        }else{
            queryFromSever(null, "Province");
        }

    }

    public void queryCities(){

        cityList = coolWeatherDB.loadCities(selectedProvince.get_id());
        if(cityList.size() > 0){

            dataList.clear();
            for(City c: cityList){
                dataList.add(c.getCity_name());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            title_text.setText(selectedProvince.getProvince_name());
            currentLevel = CITY_LEVEL;
        }else{
            queryFromSever(selectedProvince.getProvince_code(), "City");
        }

    }

    public void queryCounties(){

        countyList = coolWeatherDB.loadCounties(selectedCity.get_id());
        if(countyList.size() > 0){

            dataList.clear();
            for(County c : countyList){
                dataList.add(c.getCounty_name());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            title_text.setText(selectedCity.getCity_name());
            currentLevel = COUNTY_LEVEL;
        }else{
            queryFromSever(selectedCity.getCity_code(), "County");
        }
    }

    public void queryFromSever(final String code, final String type){

        String address;
        if(TextUtils.isEmpty(code)){
            address = "http://www.weather.com.cn/data/list3/city.xml";
        }else{
            address = "http://www.weather.com.cn/data/list3/city" +
                     code + ".xml";
        }

        showProgressDialog();
        HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
            @Override
            public void onFinish(String responce) {

                boolean result = false;
                switch (type){
                    case "Province":
                        result = Utility.handleProvinceResponce(coolWeatherDB, responce);
                        break;
                    case "City":
                        result = Utility.handleCityResponce(coolWeatherDB, responce, selectedProvince.get_id());
                        break;
                    case "County":
                        result = Utility.handleCountyResponce(coolWeatherDB, responce, selectedCity.get_id());
                        break;
                }
                if(result){
                    Message message = new Message();
                    message.obj = type;
                    message.what = 1;
                    handler.sendMessage(message);
                }
            }

            @Override
            public void onError(Exception e) {

                Message message = new Message();
                message.what = 2;
                handler.sendMessage(message);
            }
        });
    }

    public void showProgressDialog(){

        if(progressDialog == null){
            progressDialog = new ProgressDialog(this, ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage("Loading ...");
            progressDialog.setCanceledOnTouchOutside(false);

        }
        progressDialog.show();
    }

    public void closeProgressDialog(){

        if(progressDialog != null){
            progressDialog.dismiss();
        }
    }

    @Override
    public void onBackPressed(){

        if(currentLevel == PROVINCE_LEVEL){
            finish();
        }
        else if(currentLevel == CITY_LEVEL){
            queryProvinces();
        }
        else if(currentLevel == COUNTY_LEVEL){
            queryCities();
        }
    }
}
