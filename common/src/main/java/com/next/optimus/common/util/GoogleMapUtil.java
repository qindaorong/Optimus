package com.next.optimus.common.util;


/**
 * GoogleMapUtil
 *
 * @author qindaorong
 * @date 2017/10/16
 */
public class GoogleMapUtil {
    
    /**
     * 计算出经纬度之间距离 （如果要转换成公里,需要乘以1.609344,若是海里需要乘以0.8684）
     * @param lat1   第一个位置经度
     * @param lon1  第一个位置纬度
     * @param lat2  第二个位置经度
     * @param lon2  第二个位置纬度
     * @return
     */
    public static double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2))
            + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2))
            * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        double miles = dist * 60 * 1.1515;
        return miles;
    }
    
    /**
     * 将角度转换为弧度
     * @param degree
     * @return
     */
    static double deg2rad(double degree) {
        return degree / 180 * Math.PI;
    }

    /**
     * 将弧度转换为角度
     * @param radian
     * @return
     */
    static double rad2deg(double radian) {
        return radian * 180 / Math.PI;
    }
}
