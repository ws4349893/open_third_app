//
//  AKLocationConverter.swift
//  DayLife
//
//  Created by AK on 2016/12/20.
//  Copyright © 2016年 影子恋人. All rights reserved.
//

import CoreLocation


class AKLocationConverter: NSObject {
    
    static let pi = 3.14159265358979324
    
    // MARK: - Krasovsky 1940
    //
    // a = 6378245.0, 1/f = 298.3
    // b = a * (1 - f)
    // ee = (a^2 - b^2) / a^2;
    
    static let a = 6378245.0
    static let ee = 0.00669342162296594323
    
    // MARK: - Check location is out of China
    
    private static func el_outOfChina(location: CLLocation) -> Bool {
        let lat = location.coordinate.latitude
        let lon = location.coordinate.longitude
        if lat < 0.8293 || lat > 55.8271 {
            return true
        }
        if lon < 72.004 || lon > 137.8347 {
            return true
        }
        return false
    }
    
    // MARK: - World Geodetic System -> Mars Geodetic System
    
    private static func el_convert(wgs84: CLLocationCoordinate2D) -> CLLocationCoordinate2D {
        var gcj02Location = CLLocationCoordinate2D(latitude: 0.0, longitude: 0.0)
        
        let wgs84Location = CLLocation.init(latitude: wgs84.latitude, longitude: wgs84.longitude)
        
        let wgs84Lat = wgs84Location.coordinate.latitude
        let wgs84Lon = wgs84Location.coordinate.longitude
        var dLat = el_convertLat(x: wgs84Lon - 105.0, y: wgs84Lat - 35.0)
        var dLon = el_convertLon(x: wgs84Lon - 105.0, y: wgs84Lat - 35.0)
        let radLat = wgs84Lat / 180.0 * Double.pi
        var magic = sin(radLat)
        magic = 1 - ee * magic * magic
        let sqrtMagic = sqrt(magic)
        dLat = (dLat * 180.0) / ((a * (1 - ee)) / (magic * sqrtMagic) * Double.pi)
        dLon = (dLon * 180.0) / (a / sqrtMagic * cos(radLat) * Double.pi)
        let gcj02Lat = wgs84Lat + dLat
        let gcj02Lon = wgs84Lon + dLon
        gcj02Location = CLLocationCoordinate2D(latitude: gcj02Lat, longitude: gcj02Lon)
        return gcj02Location
    }
    
    private static func el_convertLat(x: Double, y: Double) -> Double {
        var ret = -100.0 + 2.0 * x + 3.0 * y + 0.2 * y * y + 0.1 * x * y + 0.2 * sqrt(fabs(x))
        ret += (20.0 * sin(6.0 * x * Double.pi) + 20.0 * sin(2.0 * x * Double.pi)) * 2.0 / 3.0
        ret += (20.0 * sin(y * Double.pi) + 40.0 * sin(y / 3.0 * Double.pi)) * 2.0 / 3.0
        ret += (160.0 * sin(y / 12.0 * Double.pi) + 320 * sin(y * Double.pi / 30.0)) * 2.0 / 3.0
        return ret
    }
    
    private static func el_convertLon(x: Double, y: Double) -> Double {
        var ret = 300.0 + x + 2.0 * y + 0.1 * x * x + 0.1 * x * y + 0.1 * sqrt(fabs(x))
        ret += (20.0 * sin(6.0 * x * Double.pi) + 20.0 * sin(2.0 * x * Double.pi)) * 2.0 / 3.0
        ret += (20.0 * sin(x * Double.pi) + 40.0 * sin(x / 3.0 * Double.pi)) * 2.0 / 3.0
        ret += (150.0 * sin(x / 12.0 * Double.pi) + 300.0 * sin(x / 30.0 * Double.pi)) * 2.0 / 3.0
        return ret
    }

    
    /// WGS-84坐标 转 GCJ-02坐标
    ///
    /// - Parameters:
    ///   - wgLat: WGS纬度
    ///   - wgLon: WGS经度
    /// - Returns: 转换后的GCJ-02坐标(火星坐标)
    class func WGS84ToGCJ02(wgLat:Double, wgLon:Double) -> CLLocationCoordinate2D{
        let initialLocation = CLLocationCoordinate2D(latitude: wgLat, longitude: wgLon)
        //调用方法 WGS-84 转 GCJ-02
        let convertedLocation = self.el_convert(wgs84: initialLocation)
        return convertedLocation
    }
    
    /// GCJ-02坐标 转 BD-09坐标
    ///
    /// - Parameters:
    ///   - gcLat: GCJ纬度
    ///   - gcLon: GCJ经度
    /// - Returns: 转换后的BD-09坐标(百度)
    class func GCJ02ToBd09(gcLat:Double, gcLon:Double) -> CLLocationCoordinate2D{
        
        let initialLocation = CLLocationCoordinate2D(latitude: gcLat, longitude: gcLon)
        
        //计算公式: GCJ-02 转 BD-09
        var bdPt = CLLocationCoordinate2D()
        let PI = 3.14159265358979324 * 3000.0 / 180.0;
        let x = initialLocation.longitude, y = initialLocation.latitude
        let z = sqrt(x * x + y * y) + 0.00002 * sin(y * PI);
        let theta = atan2(y, x) + 0.000003 * cos(x * PI);
        bdPt.longitude = z * cos(theta) + 0.0065;
        bdPt.latitude = z * sin(theta) + 0.006;
        return bdPt;
    }
    
    /// WGS-84坐标 转 BD-09坐标
    ///
    /// - Parameters:
    ///   - wgLat: WGS纬度
    ///   - wgLon: WGS经度
    /// - Returns: 转换后的BD-09坐标(百度)
    class func WGS84ToBd09(wgLat:Double, wgLon:Double) -> CLLocationCoordinate2D{
        let initialLocation = CLLocationCoordinate2D(latitude: wgLat, longitude: wgLon)
        //调用方法 WGS-84 转 GCJ-02
        let gcj02Location = self.el_convert(wgs84: initialLocation)
        
        //计算公式: GCJ-02 转 BD-09
        let convertedLocation = self.GCJ02ToBd09(gcLat: gcj02Location.latitude, gcLon: gcj02Location.longitude)
        
        return convertedLocation
    }
    
    /// BD-09坐标 转 GCJ-02坐标
    ///
    /// - Parameter 
    ///   - coordinate: BD-09经纬度
    /// - Returns: 火星坐标
    class func BD09ToGCJ02(_ coordinate:CLLocationCoordinate2D) -> CLLocationCoordinate2D {
        // 国测局GCJ-02坐标体系（谷歌、高德、腾讯），百度坐标BD-09体系
        // 将 BD-09 坐标转换成 GCJ-02  坐标
        let x_pi = 3.14159265358979324 * 3000.0 / 180.0
        let x = coordinate.longitude - 0.0065
        let y = coordinate.latitude - 0.006
        let z = sqrt(x*x + y*y) - 0.00002*sin(y * x_pi)
        let theta = atan2(y,x) - 0.000003*cos(x * x_pi)
        let gg_lat = z*sin(theta)
        let gg_lon = z*cos(theta)
        return CLLocationCoordinate2DMake(gg_lat, gg_lon)
    }
    
}
