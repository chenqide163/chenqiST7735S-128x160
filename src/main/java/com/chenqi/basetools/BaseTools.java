package com.chenqi.basetools;

import com.chenqi.filetools.savetools.SaveFiles;
import org.apache.log4j.Logger;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class BaseTools {

    private static Logger LOG = Logger.getLogger(SaveFiles.class);

    public static String getJarPath() {
        String path = BaseTools.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        String jarPath = path.substring(0, path.lastIndexOf(File.separator) + 1);
        System.out.println("getJarPath = " + jarPath);
        LOG.debug("getJarPath = " + jarPath);
        return jarPath;
    }

    /**
     * 按具体格式获取当前时间，例如 yyyy-MM-dd HH:mm:ss
     * String pattern
     *
     * @return
     */
    public static String getTimeStr(String pattern) {
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(d);
    }

    /**
     * 读取n天后的日期，n为正值为n天后，n为负值则为n天前
     * @param pattern 例如 yyyyMMdd
     * @param nDaysAfter
     * @return
     */
    public static String getNDaysLater(String pattern, int nDaysAfter) {
        Calendar calendar1 = Calendar.getInstance();
        SimpleDateFormat sdf1 = new SimpleDateFormat(pattern);
        calendar1.add(Calendar.DATE, nDaysAfter);
        String three_days_ago = sdf1.format(calendar1.getTime());
        return three_days_ago;
    }
}
