//
//  CHNavigationTool.swift
//  carService
//
//  Created by GMobile No.2 on 16/5/25.
//  Copyright © 2016年 王灿辉. All rights reserved.
//

import UIKit
import MapKit

/// 导航工具类
class CHNavigationTool: NSObject,UIActionSheetDelegate {
    fileprivate var toLocation : CLLocation!
    fileprivate var viewController : UIViewController!
    fileprivate var title : String!
    fileprivate var isSetupBaiduMap : Bool = false
    fileprivate var isSetupGaoDeMap : Bool = false
    /// 创建单例
    static let sharedInstance: CHNavigationTool = CHNavigationTool()
    
    override init() {
        super.init()
    }
    
    /**
     构造函数
     currentLocation : 当前位置 （百度地图经纬度）
     toLocation : 目的地 （百度地图经纬度）
     viewController : 显示到的控制器
     title : 弹出窗口的标题
     */
    init(toLocation:CLLocation,viewController:UIViewController,title:String) {
        self.toLocation = toLocation
        self.viewController = viewController
        self.title = title
        super.init()
    }
    
    /// 显示导航视图
    func showNavigationView() {
        // 是否已安装百度地图
        isSetupBaiduMap = UIApplication.shared.canOpenURL(URL(string:"baidumap://map/")!) ? true : false
        // 是否已安装高德地图
        isSetupGaoDeMap = UIApplication.shared.canOpenURL(URL(string:"iosamap://")!) ? true : false
        
        var alertVC : UIAlertController!
        if UIDevice.current.model != "iPhone" {
            alertVC = UIAlertController(title: title, message: nil, preferredStyle: .alert)
        }else{
            alertVC = UIAlertController(title: title, message: nil, preferredStyle: UIAlertController.Style.actionSheet)
        }
        
        
        alertVC.addAction(UIAlertAction(title: "使用苹果自带地图导航", style: UIAlertAction.Style.default) { (_) in

            // 苹果自身地图导航
            let lat = self.toLocation.coordinate.latitude
            let lng = self.toLocation.coordinate.longitude
            let loc = CLLocationCoordinate2DMake(lat, lng)
            let currentLocation = MKMapItem.forCurrentLocation()
            let toLocation = MKMapItem(placemark:MKPlacemark(coordinate:loc,addressDictionary:nil))
            
            toLocation.name = self.title
            
            MKMapItem.openMaps(with: [currentLocation,toLocation], launchOptions: [MKLaunchOptionsDirectionsModeKey: MKLaunchOptionsDirectionsModeDriving,MKLaunchOptionsShowsTrafficKey: NSNumber(value: true)])
            
        })
        
        if isSetupGaoDeMap {
            alertVC.addAction(UIAlertAction(title: "使用高德地图导航", style: UIAlertAction.Style.default) { (_) in
                // 高德地图导航
                self.setupGaoDeMap(self.toLocation.coordinate)
            })
            
        }
        
        if isSetupBaiduMap {
            alertVC.addAction(UIAlertAction(title: "使用百度地图导航", style: UIAlertAction.Style.default) { (_) in
                // 百度坐标系：即GCJ02地理坐标系 转 百度坐标
                let toCoordinate =  AKLocationConverter.GCJ02ToBd09(gcLat: self.toLocation.coordinate.latitude, gcLon: self.toLocation.coordinate.longitude)
                // 百度地图导航
                self.setupBaiduMap(toCoordinate)
            })
        }
        alertVC.addAction(UIAlertAction(title: "取消", style: UIAlertAction.Style.cancel) { (_) in
//            self.viewController.dismiss(animated: true, completion: nil)
        })
        self.viewController.present(alertVC, animated: true, completion: nil)
        
    }
    
    // 调用高德地图
    fileprivate func setupGaoDeMap(_ toCoordinate:CLLocationCoordinate2D){
        // 调用地图路径规划的字符串
        
        let srcAppIdentifier = Bundle.main.bundleIdentifier ?? "JumpMapDemo";
        var urlStr = "iosamap://path?sourceApplication=DayLife&backScheme=\(srcAppIdentifier)"
        urlStr += "&dlat=" + "\(toCoordinate.latitude)" + "&dlon=" + "\(toCoordinate.longitude)" + "&dname=\(self.title ?? "")" + "&dev=0&m=3&t=0"
        
        
        urlStr = urlStr.addingPercentEncoding(withAllowedCharacters: CharacterSet.urlQueryAllowed)!
        let url = URL(string: urlStr)!
        
        // 手机安装有高德地图app
        if (UIApplication.shared.canOpenURL(url)) {
            UIApplication.shared.openURL(url)
        }
    }
    // 调用百度地图
    fileprivate func setupBaiduMap(_ toCoordinate:CLLocationCoordinate2D){
        // 调用地图路径规划的字符串
        
        let srcAppIdentifier = Bundle.main.bundleIdentifier ?? "JumpMapDemo";
        var urlStr = "baidumap://map/direction?origin={{我的位置}}&destination=name:\(self.title ?? "地图上的点")|latlng:\(toCoordinate.latitude),\(toCoordinate.longitude)&mode=driving&src=\(srcAppIdentifier)"
        
        print(urlStr);
        urlStr = urlStr.addingPercentEncoding(withAllowedCharacters: CharacterSet.urlQueryAllowed)!
        let url = URL(string: urlStr)!
        
        // 手机安装有百度地图ap
        if (UIApplication.shared.canOpenURL(url)) {
            UIApplication.shared.openURL(url)
        }
    }
    
    
}
