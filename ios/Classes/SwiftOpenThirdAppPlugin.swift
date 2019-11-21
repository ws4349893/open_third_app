import Flutter
import UIKit
import MapKit

public class SwiftOpenThirdAppPlugin: NSObject, FlutterPlugin {
  public static func register(with registrar: FlutterPluginRegistrar) {
    let _channel = FlutterMethodChannel(name: "open_third_app", binaryMessenger: registrar.messenger())
    let instance = SwiftOpenThirdAppPlugin()
    registrar.addMethodCallDelegate(instance, channel: _channel)
  }

  public func handle(_ call: FlutterMethodCall, result: @escaping FlutterResult) {
    if(call.method == "checkMapAndNavigation"){
        var map = call.arguments as? Dictionary<String, String>
        checkMapAndNavigation(lat: map?["lat"] ?? "0", lng: map?["lng"] ?? "0", loactionName: map?["locationName"] ?? "")
    }else if(call.method == "oppenAppStore"){
        let url = call.arguments as? String
        if let u = url {
            gotoAppStore(urlStr: u)
        }
    } else if(call.method == "hasInstallApp"){
        let url = call.arguments as? String
        if let u = url {
           result(UIApplication.shared.canOpenURL(URL.init(string: u)!)) 
        }
    } else if(call.method == "toSelfSetting"){
        if let url = URL(string: UIApplication.openSettingsURLString){
            UIApplication.shared.openURL(url)
        }
        
    }
  }
    //跳转到应用的AppStore页页面
    func gotoAppStore(urlStr: String) {
        let url = URL(string: urlStr)
        if let u = url {
            UIApplication.shared.openURL(u)
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
