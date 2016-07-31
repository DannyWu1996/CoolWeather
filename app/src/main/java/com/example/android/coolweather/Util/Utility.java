package com.example.android.coolweather.Util;

import com.example.android.coolweather.DB.CoolWeatherDB;
import com.example.android.coolweather.DB.CoolWeatherOpenHelper;
import com.example.android.coolweather.Model.City;
import com.example.android.coolweather.Model.County;
import com.example.android.coolweather.Model.Province;

/**
 * Created by Danny.W on 7/30/2016.
 */
public class Utility {

    /*
    parsing and dealing with the province data from http
     */

    public static boolean handleProvinceResponce(CoolWeatherDB db,
                                                 String responce){

        if(responce.length() > 0) {
            String allProvince[] = responce.split(",");
            if(allProvince != null && allProvince.length > 0){
                for(String p: allProvince){

                    String[] array = p.split("\\|");
                    Province province = new Province();
                    province.setProvince_code(array[0]);
                    province.setProvince_name(array[1]);
                    db.saveProvince(province);
                }
                return true;
            }


        }

        return false;
    }

    /*
    parsing and dealing with the city data from http
     */

    public static boolean handleCityResponce(CoolWeatherDB db,
                                             String responce, int province_id){

        if(responce.length() > 0) {
            String allCity[] = responce.split(",");
            if(allCity != null && allCity.length > 0){
                for(String c: allCity){

                    String[] array = c.split("\\|");
                    City city = new City();
                    city.setCity_code(array[0]);
                    city.setCity_name(array[1]);
                    city.setProvince_id(province_id);
                    db.saveCity(city);
                }
                return true;
            }


        }
        return false;
    }

    /*
    parsing and dealing with the county data from http
     */

    public static boolean handleCountyResponce(CoolWeatherDB db,
                                             String responce, int city_id){

        if(responce.length() > 0) {
            String allCounty[] = responce.split(",");
            if(allCounty != null && allCounty.length > 0){
                for(String c: allCounty){

                    String[] array = c.split("\\|");
                    County county = new County();
                    county.setCounty_code(array[0]);
                    county.setCounty_name(array[1]);
                    county.setCity_id(city_id);
                    db.saveCounty(county);
                }
                return true;
            }


        }
        return false;
    }
}
