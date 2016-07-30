package com.example.android.coolweather.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.android.coolweather.Model.City;
import com.example.android.coolweather.Model.County;
import com.example.android.coolweather.Model.Province;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Danny.W on 7/29/2016.
 */
public class CoolWeatherDB {

    private static final String DB_NAME = "CoolWeather";
    private static final int DB_VERSION = 1;
    private SQLiteDatabase db;

    private static CoolWeatherDB coolWeatherDB;

    private CoolWeatherDB(Context context){

        CoolWeatherOpenHelper coolWeatherOpenHelper = new CoolWeatherOpenHelper(
                context,
                DB_NAME,
                null,
                DB_VERSION
        );

        db = coolWeatherOpenHelper.getWritableDatabase();
    }

    private static CoolWeatherDB getInstance(Context context){

        if(coolWeatherDB == null){
            coolWeatherDB = new CoolWeatherDB(context);
        }
        return coolWeatherDB;
    }

    /*
    insert Province instance into Database
     */
    private void saveProvince(Province province){

        ContentValues value = new ContentValues();
        value.put("province_name", province.getProvince_name());
        value.put("province_code", province.getProvince_code());
        db.insert("Province", null, value);
    }

    /*
    query All Province from Database
     */
    public List<Province> loadProvinces(){

        List<Province> list = new ArrayList<>();
        Cursor cursor = db.query(
                 "Province", null, null, null, null, null, null);

        if(cursor.moveToFirst()){

            do{
                Province province = new Province();
                String name = cursor.getString(cursor.getColumnIndex("province_name"));
                String code = cursor.getString(cursor.getColumnIndex("province_code"));
                int id = cursor.getInt(cursor.getColumnIndex("_id"));
                province.set_id(id);
                province.setProvince_name(name);
                province.setProvince_code(code);
                list.add(province);
            }while(cursor.moveToNext());
        }

        return list;
    }

    /*
    insert City instance to Dababase
     */

    public void saveCity(City city){

        ContentValues values = new ContentValues();
        values.put("city_name", city.getCity_name());
        values.put("city_code", city.getCity_code());
        values.put("province_id", city.getProvince_id());
        db.insert("City", null, values);
    }

    /*
    query City in Province from Database
     */

    public List<City> loadCities(int province_id){

        List<City> list = new ArrayList<>();
        Cursor cursor = db.query(
                "City", null, "province_id = ?", new String[]{String.valueOf(province_id)}, null, null, null
        );

        if(cursor.moveToFirst()){

            do{
                City city = new City();
                city.set_id(cursor.getInt(cursor.getColumnIndex("_id")));
                city.setProvince_id(province_id);

                city.setCity_name(
                        cursor.getString(cursor.getColumnIndex("city_name")));

                city.setCity_code(
                        cursor.getString(cursor.getColumnIndex("city_code")));
               list.add(city);
            }while(cursor.moveToNext());
        }

        return list;
    }

    /*
    insert County instance to Database
     */

    public void saveCounty(County county){

        ContentValues values = new ContentValues();
        values.put("county_name", county.getCounty_name());
        values.put("county_code", county.getCounty_code());
        values.put("city_id", county.getCity_id());
        db.insert("County", null, values);
    }

    /*
    query county in the city from Database
     */
    public List<County> loadCounties(int city_id){

        List<County> list = new ArrayList<>();
        Cursor cursor = db.query(
                "City", null, "city_id = ?", new String[]{String.valueOf(city_id)}, null, null, null
        );

        if(cursor.moveToFirst()){

            do{
                County county = new County();
                county.set_id(cursor.getInt(cursor.getColumnIndex("_id")));
                county.setCity_id(city_id);

                county.setCounty_name(
                        cursor.getString(cursor.getColumnIndex("county_name")));

                county.setCounty_code(
                        cursor.getString(cursor.getColumnIndex("county_code")));
                list.add(county);
            }while(cursor.moveToNext());
        }

        return list;
    }
}
