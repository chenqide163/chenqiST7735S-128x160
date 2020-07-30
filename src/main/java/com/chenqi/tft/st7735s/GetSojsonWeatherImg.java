package com.chenqi.tft.st7735s;

import com.chenqi.basetools.BaseTools;
import com.chenqi.weather.sojson.SojsonWeatherService;
import com.chenqi.weather.sojson.pojo.SojsonForecast;
import com.chenqi.weather.sojson.pojo.SojsonWeather;
import org.apache.log4j.Logger;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class GetSojsonWeatherImg {
    private static Logger LOG = Logger.getLogger(GetSojsonWeatherImg.class);

    private static final Map<String, String> WEATHERTYPE_MAP = initWeatherType();

    public static Map<String, String> initWeatherType() {
        Map<String, String> weatherType = new HashMap<>();
        //晴、多云、阴、阵雨、雷阵雨、雷阵雨伴有冰雹、雨夹雪、小雨、中雨
        weatherType.put("晴", "0");
        weatherType.put("多云", "1");
        weatherType.put("阴", "2");
        weatherType.put("阵雨", "3");
        weatherType.put("雷阵雨", "4");
        weatherType.put("雷阵雨伴有冰雹", "5");
        weatherType.put("雨夹雪", "6");
        weatherType.put("小雨", "7");
        weatherType.put("中雨", "8");

        //大雨、暴雨、大暴雨、特大暴雨、阵雪、小雪、中雪、大雪、暴雪、雾、冻雨
        weatherType.put("大雨", "9");
        weatherType.put("暴雨", "10");
        weatherType.put("大暴雨", "11");
        weatherType.put("特大暴雨", "12");
        weatherType.put("阵雪", "13");
        weatherType.put("小雪", "14");
        weatherType.put("中雪", "15");
        weatherType.put("大雪", "16");
        weatherType.put("暴雪", "17");
        weatherType.put("雾", "18");
        weatherType.put("冻雨", "19");

        //沙尘暴、小到中雨、中到大雨、大到暴雨、暴雨到大暴雨、大暴雨到特大暴雨
        weatherType.put("沙尘暴", "20");
        weatherType.put("小到中雨", "21");
        weatherType.put("中到大雨", "22");
        weatherType.put("大到暴雨", "23");
        weatherType.put("暴雨到大暴雨", "24");
        weatherType.put("大暴雨到特大暴雨", "25");

        //小到中雪、 中到大雪、大到暴雪、浮尘、扬沙、强沙尘暴、霾
        weatherType.put("小到中雪", "26");
        weatherType.put("中到大雪", "27");
        weatherType.put("大到暴雪", "28");
        weatherType.put("浮尘", "29");
        weatherType.put("扬沙", "30");
        weatherType.put("强沙尘暴", "31");

        return weatherType;
    }

    /**
     * 获取今日气象
     *
     * @return
     */
    public static BufferedImage getSojsonWeatherImg() throws IOException {
        LOG.debug("start to GetSojsonWeatherImg ");
        int width = ST7735sDriver.WIDTH;
        int height = ST7735sDriver.HEIGHT;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        Graphics2D g = image.createGraphics();
        //g.setFont(new java.awt.Font("叶根友毛笔行书2.0版", Font.PLAIN, 25));

        //101190101 南京
        SojsonWeather result = SojsonWeatherService.getWeatherByCityId("101190101");

        String city = result.getCityInfo().getCity().substring(0, 2);
        String quality = "空气质量：" + result.getData().getQuality();
        String humility = result.getData().getShidu();
        String type = result.getData().getSojsonForecast().get(0).getType();
        String fb = WEATHERTYPE_MAP.get(type);
        String week = result.getData().getSojsonForecast().get(0).getWeek();
        String hiTemp = result.getData().getSojsonForecast().get(0).getHigh().split(" ")[1];
        String lowTemp = result.getData().getSojsonForecast().get(0).getLow().split(" ")[1];
        String temperature = "温度:" + lowTemp + "~" + hiTemp;
        String time = BaseTools.getTimeStr("HH:mm:ss");

        String wind = result.getData().getSojsonForecast().get(0).getFx() +
                result.getData().getSojsonForecast().get(0).getFl();
        g.setFont(new Font("微软雅黑", Font.BOLD, 30));
        g.setColor(new Color(0x25FF22));
        g.drawString(city, 0, 26);

        g.setColor(new Color(0xFFF300));
        g.drawLine(0, 33, 128, 33);

        String date_y = BaseTools.getTimeStr("MM月dd HH:mm:ss");
        Color color = null;
        String tip = null;
        if (Integer.parseInt(fb) > 2 || Integer.parseInt(fb) > 2) {
            color = new Color(0xFF5D7D);
            g.setColor(color);
            date_y += "  请带伞";
            tip = "请带伞";
        } else {
            color = new Color(0x25FF22);
            g.setColor(color);
            date_y += "  无需带伞";
            tip = "无需带伞";
        }

        g.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        g.drawString(week, 66, 30);
        //g.drawString(time, 66, 31);
        g.drawString(date_y, 0, 49);
        g.drawString(wind, 0, 66);
        g.drawString(temperature, 0, 84);
        g.drawString(quality, 0, 102);

        g.setFont(new Font("微软雅黑", Font.BOLD, 20));
        g.drawString(type, 0, 128);
        g.drawString(tip, 0, 150);

        GetSojsonWeatherImg getLcdImg = new GetSojsonWeatherImg();
        InputStream is = getLcdImg.getClass().getResourceAsStream("/weatherIcon/b_" + fb + ".gif");
        Image fbImg = javax.imageio.ImageIO.read(is);

        g.drawImage(fbImg, 73, 110, 50, 46, null);

        return image;
    }

    /**
     * 优化黑色背景
     *
     * @param imgBf
     * @return
     */
    private static BufferedImage getBlackbackGround(BufferedImage imgBf) {
        for (int y = 0; y < ST7735sDriver.HEIGHT; y++) {
            for (int x = 0; x < ST7735sDriver.WIDTH; x++) {
                int rgb = imgBf.getRGB(x, y);
                int red = (rgb >> 16) & 0xff; //获取红色的色值
                int green = (rgb >> 8) & 0xff;
                int blue = rgb & 0xff;

                int color = 240;
                if (red > color && green > color && blue > color) {
                    imgBf.setRGB(x, y, 0);
                }
            }
        }
        return imgBf;
    }

    /**
     * 获取未来3天的天气，并做图
     *
     * @return
     */
    public static BufferedImage getSojsonFutureWeatherImg() throws IOException {
        LOG.debug("start to getFutureWeatherImg ");
        int width = ST7735sDriver.WIDTH;
        int height = ST7735sDriver.HEIGHT;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        Graphics2D g = image.createGraphics();
        g.setColor(new Color(0xFFF300));
        //每天给45像素的高度进行展示
        g.drawLine(0, 25, width, 25);
        g.drawLine(0, 70, width, 70);
        g.drawLine(0, 115, width, 115);
        //g.setFont(new java.awt.Font("叶根友毛笔行书2.0版", Font.PLAIN, 25));

        //101190101 南京
        SojsonWeather result = SojsonWeatherService.getWeatherByCityId("101190101");

        SojsonForecast todayForecast = result.getData().getSojsonForecast().get(0);
        SojsonForecast tomorrowForecast = result.getData().getSojsonForecast().get(1);
        SojsonForecast twoDaysLaterForecast = result.getData().getSojsonForecast().get(2);

        String city = result.getCityInfo().getCity().substring(0, 2);
        g.setFont(new Font("微软雅黑", Font.BOLD, 23));
        g.setColor(new Color(0x25FF22));
        g.drawString(city, 2, 21);

        g.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        g.setColor(new Color(0x25FF22));
        g.drawString(BaseTools.getTimeStr("HH:mm:ss"), 60, 23);

        drawEveryFutureWeather(todayForecast, g, 25);
        drawEveryFutureWeather(tomorrowForecast, g, 70);
        drawEveryFutureWeather(twoDaysLaterForecast, g, 115);
        return image;
    }


    /**
     * 做每天的天气预报图
     *
     * @param futureWeather
     * @param graphics2D
     * @param y
     * @throws IOException
     */
    private static void drawEveryFutureWeather(SojsonForecast futureWeather, Graphics2D graphics2D, int y) throws IOException {
        String weather = futureWeather.getType();
        String hiTemp = futureWeather.getHigh().split(" ")[1];
        String lowTemp = futureWeather.getLow().split(" ")[1];
        String temperature = "温度:" + lowTemp + "~" + hiTemp;

        String week = futureWeather.getWeek();

        String fb = WEATHERTYPE_MAP.get(weather);

        graphics2D.setFont(new Font("微软雅黑", Font.PLAIN, 13));

        //如果是下雨天，展示红色字体，如果是不下雨的天，展示绿色字体
        Color color = null;
        if (Integer.parseInt(fb) > 2) {
            color = new Color(0xFF5D7D);
            graphics2D.setColor(color);
            graphics2D.drawString(week + "    请带伞！", 0, y + 14);
        } else {
            color = new Color(0x25FF22);
            graphics2D.setColor(color);
            graphics2D.drawString(week + "    无需带伞", 0, y + 14);
        }

        graphics2D.drawString(temperature, 0, y + 28);
        graphics2D.drawString(weather, 0, y + 43);

        GetSojsonWeatherImg getLcdImg = new GetSojsonWeatherImg();
        InputStream is = getLcdImg.getClass().getResourceAsStream("/weatherIcon/b_" + fb + ".gif");
        Image fbImg = javax.imageio.ImageIO.read(is);
        graphics2D.drawImage(fbImg, 105, y + 20, 20, 20, null);
    }

    public static void main(String[] args) throws IOException {
        //生成今天的天气预报图片
        BufferedImage bufferedImage = getSojsonFutureWeatherImg();
        ImageIO.write(bufferedImage, "jpg", new File("D:\\weatherFuture.jpg"));
        //生成今天、明天、后天的天气预报图片
        bufferedImage = getSojsonWeatherImg();
        ImageIO.write(bufferedImage, "jpg", new File("D:\\weatherToday.jpg"));
    }

    /**
     * 打开树莓派AP，并且提示公告版
     *
     * @return
     */
    public static BufferedImage getNoticeImg() throws IOException {
        int width = ST7735sDriver.WIDTH;
        int height = ST7735sDriver.HEIGHT;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        Graphics2D g = image.createGraphics();
        String raspIp = "";

        g.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        g.setColor(new Color(0x25FF22));
        g.drawString("树莓派未连接网络", 0, 17);
        g.drawString("请连接至wifi:pi",0,34);

        g.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        g.drawString("piIp:"+raspIp,0,51);

        g.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        g.drawString("修改以下配置：",0,68);

        g.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        g.drawString("/etc/wpa_supplicant",0,85);
        g.drawString("wpa_supplicant.conf",0,102);
        return image;
    }
}
