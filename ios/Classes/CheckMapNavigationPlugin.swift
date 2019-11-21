//
//  CheckMapNavigationPlugin.swift
//  open_third_app
//
//  Created by WangShu on 2019/4/29.
//import Flutter
import UIKit
import MapKit
public class CheckMapNavigationPlugin: NSObject, FlutterPlugin {
    public static func register(with registrar: FlutterPluginRegistrar) {
        let channel = FlutterMethodChannel(name: "check_map_navigation", binaryMessenger: registrar.messenger())
        let instance = SwiftOpenThirdAppPlugin()
        registrar.addMethodCallDelegate(instance, channel: channel)
    }
    
    public func handle(_ call: FlutterMethodCall, result: @escaping FlutterResult) {
        if(call.method == "checkMapAndNavigation"){
            var map = call.arguments as? Dictionary<String, String>
            checkMapAndNavigation(lat: map?["lat"] ?? "0", lng: map?["lng"] ?? "0", loactionName: map?["locationName"] ?? "")
        }
    }
    
    func checkMapAndNavigation(lat: String,lng: String ,loactionName: String ){
        let shopLoc = CLLocation(latitude: Double(lat)!, longitude: Double(lng)!)
        if var topController = UIApplication.shared.keyWindow?.rootViewController {
            while let presentedViewController = topController.presentedViewController {
                topController = presentedViewController
            }
            //弹出提示框 选择地图
            let navTool = CHNavigationTool(toLocation: shopLoc, viewController: topController, title: loactionName);
            navTool.showNavigationView()
        }
        
    }
    
}
