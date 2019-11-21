package com.example.open_third_app.utils;

/**
 * 公司：天天生活科技有限公司
 * 作者：TigerLau
 * 背景：
 *      由于不同的地图服务，拥有自己的一套坐标系，如高德地图使用的坐标系是：火星坐标系 (GCJ-02)，百度地图使用的坐标系是：百度坐标系 (BD-09)
 *      在给出一对经纬度时，同时需要明确这是基于哪种坐标系，同一经纬度，在不同的坐标系上的定位不同
 *
 *
 * 描述：各地图API坐标系统比较与转换
 *
 *       WGS84坐标系：即地球坐标系，国际上通用的坐标系。设备一般包含GPS芯片或者北斗芯片获取的经纬度为WGS84地理坐标系,
 *       谷歌地图采用的是WGS84地理坐标系（中国范围除外）;
 *       GCJ02坐标系：即火星坐标系，是由中国国家测绘局制订的地理信息系统的坐标系统。由WGS84坐标系经加密后的坐标系。
 *       谷歌中国地图和搜搜中国地图采用的是GCJ02地理坐标系; BD09坐标系：即百度坐标系，GCJ02坐标系经加密后的坐标系;
 *       搜狗坐标系、图吧坐标系等，估计也是在GCJ02基础上加密而成的。
 */

public class CoordinateConvertUtil {

    /**
     * 火星坐标系
     */
    public static final String GCJ02 = "gcj02";
    /**
     * 百度坐标系
     */
    public static final String BD09 = "bd09";


    private static double PI = 3.14159265358979324 * 3000.0 / 180.0;

    /**
     * 高德经纬度转换成百度经纬度(火星坐标系转换成百度坐标系)
     * 备注：
     *      由于不同的地图服务，拥有自己的一套坐标系，如高德地图使用的坐标系是：火星坐标系 (GCJ-02)，百度地图使用的坐标系是：百度坐标系 (BD-09)
     *      在给出一对经纬度时，同时需要明确这是基于哪种坐标系，同一经纬度，在不同的坐标系上的定位不同
     *
     * @param gd_lat 经度
     * @param gd_lon 纬度
     * @return
     */
    public static double[] gcj02_To_Bd09(double gd_lat, double gd_lon) {
        double[] bd_lat_lon = new double[2];
        double x = gd_lon, y = gd_lat;
        double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * PI);
        double theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * PI);
        bd_lat_lon[1] = z * Math.cos(theta) + 0.0065;
        bd_lat_lon[0] = z * Math.sin(theta) + 0.006;
        return bd_lat_lon;
    }

    /**
     * 百度经纬度转换成高德经纬度(百度坐标系转换成火星坐标系)
     * 备注：
     *      由于不同的地图服务，拥有自己的一套坐标系，如高德地图使用的坐标系是：火星坐标系 (GCJ-02)，百度地图使用的坐标系是：百度坐标系 (BD-09)
     *      在给出一对经纬度时，同时需要明确这是基于哪种坐标系，同一经纬度，在不同的坐标系上的定位不同
     *
     * @param bd_lat 经度
     * @param bd_lon 纬度
     * @return
     */
    public static double[] bd09_To_Gcj02(double bd_lat, double bd_lon) {
        double[] gd_lat_lon = new double[2];
        double x = bd_lon - 0.0065, y = bd_lat - 0.006;
        double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * PI);
        double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * PI);
        gd_lat_lon[0] = z * Math.sin(theta);
        gd_lat_lon[1] = z * Math.cos(theta);
        return gd_lat_lon;
    }

    public static void main(String args[]) {
        String gcj_lat = "113.129479";
        String gcj_lng = "22.56464";
        double[] bd_lat_lng = gcj02_To_Bd09(Double.parseDouble(gcj_lat), Double.parseDouble(gcj_lng));
        System.out.println("bd Lat:" + bd_lat_lng[0] + ", bd Lng:" + bd_lat_lng[1]);

        String bd_lat = "113.135992";
        String bd_lng = "22.570661";
        //double[] gcj_lat_lng = bd09_To_Gcj02(Double.parseDouble(bd_lat), Double.parseDouble(bd_lng));
        //System.out.println("gcj Lat:" + gcj_lat_lng[0] + ", gcj Lng:" + gcj_lat_lng[1]);
    }
    
}
