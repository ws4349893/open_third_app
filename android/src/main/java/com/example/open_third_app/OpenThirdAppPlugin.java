package com.example.open_third_app;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.net.Uri;
import android.os.Build;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.example.open_third_app.utils.MapUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.PluginRegistry;

public class OpenThirdAppPlugin implements MethodChannel.MethodCallHandler {
    static Activity context;

    public static void registerWith(PluginRegistry.Registrar registrar) {
        context = registrar.activity();
        final MethodChannel channel = new MethodChannel(registrar.messenger(), "open_third_app");
        channel.setMethodCallHandler(new OpenThirdAppPlugin());
    }

    @Override
    public void onMethodCall(MethodCall methodCall, MethodChannel.Result result) {
        if(methodCall.method .equals("checkMapAndNavigation")){
            Map<String,String> param = (Map<String, String>) methodCall.arguments;
            MapUtil mapUtil = new MapUtil(context, Double.parseDouble(param.get("lat")), Double.parseDouble(param.get("lng")));
            mapUtil.showMapCheckDialog();
        } else if(methodCall.method.equals("installApk")){
            String apkPath = (String) methodCall.arguments;
            installApk(apkPath);
        } else if(methodCall.method.equals("hasInstallApp")){
            String packageName = (String) methodCall.arguments;
            result.success(isAvilible(context,packageName));
        } else if(methodCall.method.equals("openApp")){
            String packageName = (String) methodCall.arguments;
            result.success(openApp(context,packageName));
        } else if(methodCall.method.equals("getLocalMaps")){
            String [] maps = MapUtil.getMapName();
            String mapsStr = "";
            if(maps.length>0){
                for (String mapName :
                        maps) {
                    mapsStr += mapName +",";

                }
                if(mapsStr.length()>1){
                    mapsStr = mapsStr.substring(0,mapsStr.length()-1);
                }
            }
            result.success(mapsStr);
        } else if(methodCall.method.equals("navigationWithMap")){
            Map<String,String> param = (Map<String, String>) methodCall.arguments;
            MapUtil mapUtil = new MapUtil(context, Double.parseDouble(param.get("lat")), Double.parseDouble(param.get("lng")));
            String mapName = param.get("mapName");
            mapUtil.openMap(mapName);
        } else if(methodCall.method.equals("toSelfSetting")){
            toSelfSetting(context);
        }
    }

    public static void toSelfSetting(Context context) {
        Intent mIntent = new Intent();
         mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
         if (Build.VERSION.SDK_INT >= 9) {
          mIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
          mIntent.setData(Uri.fromParts("package", context.getPackageName(), null));
         } else if (Build.VERSION.SDK_INT <= 8) {
          mIntent.setAction(Intent.ACTION_VIEW);
          mIntent.setClassName("com.android.settings", "com.android.setting.InstalledAppDetails");
          mIntent.putExtra("com.adroid.settings.ApplicationPkgName", context.getPackageName());
        }
        context.startActivity(mIntent);
    }
    public boolean openApp(Context context,String packageName){
        PackageManager packageManager = context.getPackageManager();
        Intent intent= packageManager.getLaunchIntentForPackage(packageName);
        if(intent==null){
            Toast.makeText(context, "未安装", Toast.LENGTH_LONG).show();
            return false;
        }else{
            context.startActivity(intent);
            return true;
        }
    }

    /**
     * 检查手机上是否安装了指定的软件
     * @param context
     * @param packageName
     * @return
     */
    public boolean isAvilible(Context context, String packageName) {
        final PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
        List<String> packageNames = new ArrayList<String>();

        if (packageInfos != null) {
            for (int i = 0; i < packageInfos.size(); i++) {
                String packName = packageInfos.get(i).packageName;
                packageNames.add(packName);
            }
        }
        // 判断packageNames中是否有目标程序的包名，有TRUE，没有FALSE
        return packageNames.contains(packageName);
    }

    private void installApk(String saveFileName) {
        File file = new File(saveFileName);
        if (!file.exists())
            return;
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Context context1 = context.getApplicationContext();
            PackageManager pm = context1.getPackageManager();
            ComponentName providerComponentName = new ComponentName(context1, androidx.core.content.FileProvider.class);

            try {

                // Fetch the provider info using the component name from the PackageManager
                // This throws an exception if the provider isn't registered.
                ProviderInfo providerInfo = pm.getProviderInfo(providerComponentName, 0);
                String authority = providerInfo.authority;
                Uri contentUri = FileProvider.getUriForFile(context.getApplicationContext(), authority, file);
                intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
            } catch (Exception e) {
                Toast.makeText(context1, "安装失败", Toast.LENGTH_SHORT).show();
            }
        } else {
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        }
        context.startActivity(intent);
    }
}
