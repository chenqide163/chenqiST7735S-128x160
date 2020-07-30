package com.chenqi.weather.sojson;

public class MainGetSojsonWeather {

    public static void main(String[] args) {
        String str = SojsonWeatherService.getWeatherByCityId("101010100").toString();
        System.out.println(str);
    }
}
