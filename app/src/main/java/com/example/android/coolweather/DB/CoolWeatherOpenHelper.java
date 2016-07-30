package com.example.android.coolweather.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Danny.W on 7/29/2016.
 */
public class CoolWeatherOpenHelper extends SQLiteOpenHelper {

    /*
    Create table Province
     */
    private static final String CREATE_PROVINCE = "create table Province (" +
            "_id integer primary key autoincrement, " +
            "province_name text, " +
            "province_code text)";

    /*
    Create table City
     */
    private static final String CREATE_CITY = "create table City (" +
            "_id integer primary key autoincrement, " +
            "city_name text, " +
            "city_code text," +
            "foreign key (province_id) references Province(_id))";

    /*
    Create table County
     */
    private static final String CREATE_COUNTY = "create table County (" +
            "_id integer primary key autoincrement, " +
            "county_name text, " +
            "county_code text," +
            "foreign key (city_id) references City(_id))";

    public CoolWeatherOpenHelper(Context context, String name,
                                 SQLiteDatabase.CursorFactory factory, int version){

        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db){

        db.execSQL(CREATE_PROVINCE);
        db.execSQL(CREATE_CITY);
        db.execSQL(CREATE_COUNTY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){

//        if(oldVersion != newVersion){
//            db.execSQL("drop table if exist" + );
//        }
    }

}
