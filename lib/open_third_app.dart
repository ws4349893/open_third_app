import 'dart:async';
import 'dart:io';
import 'package:flutter/services.dart';

class OpenThirdApp {

  static const MethodChannel _channel = const MethodChannel('open_third_app');

  static Future checkMapAndNavigation(String lat,String lng,String address) async {
    await _channel.invokeMethod("checkMapAndNavigation",{"lat":lat,"lng":lng,"locationName":address,});
  }

  static Future installApk(String apkPath) async{
    if(Platform.isAndroid){
      _channel.invokeMethod("installApk",apkPath);
    }
  }
  
  static Future openAppStore(String appStoreUrl) async{
    if(Platform.isIOS){
      _channel.invokeMethod("oppenAppStore",appStoreUrl);
    }
  }

  static Future<bool> hasInstallApp(String packageName) async{
    return _channel.invokeMethod("hasInstallApp",packageName);
  }
  static Future<bool> toSelfSetting()async{
    return _channel.invokeMethod("toSelfSetting");
  }
  ///
  /// IOS 使用 URLScheme
  /// IOS 使用 packageName
  /// 
  static Future<bool> openApp(String packageName) async{
    if(Platform.isIOS){
      return _channel.invokeMethod("oppenAppStore",packageName);
    } else {
      return _channel.invokeMethod("openApp",packageName);
    }
  }
}
