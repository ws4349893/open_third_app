//package com.example.open_third_app;
//
//import android.content.Context;
//import android.content.Intent;
//import android.net.Uri;
//import android.os.Build;
//
//import com.example.open_third_app.utils.MapUtil;
//
//import java.io.File;
//import java.util.Map;
//
//import io.flutter.plugin.common.MethodCall;
//import io.flutter.plugin.common.MethodChannel;
//import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
//import io.flutter.plugin.common.MethodChannel.Result;
//import io.flutter.plugin.common.PluginRegistry.Registrar;
////import android.support.v4.content.FileProvider;
//import androidx.core.content.FileProvider;
//
//public class CusOpenThirdAppPlugin implements MethodCallHandler {
//    static Context context;
//
//    public static void registerWith(Registrar registrar, Context ctx) {
//        context = ctx;
//
//        final MethodChannel channel = new MethodChannel(registrar.messenger(), "open_third_app");
//        channel.setMethodCallHandler(new CusOpenThirdAppPlugin());
//    }
//
//    @Override
//    public void onMethodCall(MethodCall methodCall, Result result) {
//        if(methodCall.method .equals("checkMapAndNavigation")){
//            Map<String,String> param = (Map<String, String>) methodCall.arguments;
//            MapUtil mapUtil = new MapUtil(context, Double.parseDouble(param.get("lat")), Double.parseDouble(param.get("lng")));
//            mapUtil.showMapCheckDialog();
//        } else if(methodCall.method.equals("installApk")){
//            String apkPath = (String) methodCall.arguments;
//            installApk(apkPath);
//        }
//    }
//
//
//    private void installApk(String saveFileName) {
//        File file = new File(saveFileName);
//        if (!file.exists())
//            return;
//        Intent intent = new Intent(Intent.ACTION_VIEW);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//            Uri contentUri = FileProvider.getUriForFile(context.getApplicationContext(), "com.titilife.halfoff.provider", file);
//            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
//        } else {
//            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
//        }
//        context.startActivity(intent);
//    }
//}
