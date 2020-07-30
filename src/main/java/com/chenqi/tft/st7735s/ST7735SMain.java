package com.chenqi.tft.st7735s;

import org.apache.log4j.Logger;

public class ST7735SMain {

    private static Logger LOG = Logger.getLogger(ST7735SMain.class);
    public static void main(String[] args) {
        for (; ; ) {
            try {
                //ST7735sDriver.getInstance().drawImg16BitColor(GetLcdImg.getColorImg(imgPath));
                ST7735sDriver.getInstance().drawImg16BitColorOptimization(GetSojsonWeatherImg.getSojsonWeatherImg());
                Thread.sleep(8000);
                ST7735sDriver.getInstance().drawImg16BitColorOptimization(GetSojsonWeatherImg.getSojsonFutureWeatherImg());
                Thread.sleep(8000);
            } catch (Throwable e) {
                disPlayNoticeImg();
                e.printStackTrace();
            }
        }
    }

    private static void disPlayNoticeImg(){
        try {
            ST7735sDriver.getInstance().drawImg16BitColor(GetSojsonWeatherImg.getNoticeImg());
            Thread.sleep(60000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
