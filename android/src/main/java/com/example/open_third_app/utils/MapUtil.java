package com.example.open_third_app.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

//import com.baidu.mapapi.model.LatLng;
//import com.titilife.android.libcommon.util.ToastUtils;

import com.example.open_third_app.R;

import java.io.File;

/**
 * 公司：天天生活科技有限公司
 * 作者： WangShu
 * 创建时间：  2018-11-17.
 * 描述：
 * 备注：
 */
class LatLng {
   public static double lat;
    public static double lng;
    LatLng(double lat,double lng){
        this.lat = lat;
        this.lng = lng;
    }
}


public class MapUtil {
    private Context ctx;
    private double lat;
    private double lng;
//    private static volatile MapUtil singeton;
//    public static MapUtil getInstance(Context ctx,double lat,double lng){
//        if(null == singeton){
//            synchronized (MapUtil.class){
//                if(null == singeton){
//                    singeton = new MapUtil(ctx,lat,lng);
//                }
//            }
//        }
//        return singeton;
//    }

    public MapUtil(Context ctx,double lat,double lng) {
        this.ctx = ctx;
        this.lat = lat;
        this.lng = lng;
    }
    private void show1() {
        String[] maps = getMapName();
        if(maps.length == 0) {
            Toast.makeText(ctx,"本机未安装百度、高德、腾讯地图！",Toast.LENGTH_SHORT).show();
        }
        else if(maps.length == 1){
            openMap(maps[0]);
        } else {
            final Dialog bottomDialog = new Dialog(ctx, R.style.BottomDialog);
            View contentView = LayoutInflater.from(ctx).inflate(R.layout.dialog_content_normal, null);
            bottomDialog.setContentView(contentView);
            ViewGroup.LayoutParams layoutParams = contentView.getLayoutParams();
            layoutParams.width = ctx.getResources().getDisplayMetrics().widthPixels;
            contentView.setLayoutParams(layoutParams);
            ImageView ivClose = contentView.findViewById(R.id.iv_close);
            ivClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bottomDialog.dismiss();
                }
            });
            LinearLayout line_bd = contentView.findViewById(R.id.line_bd);
            ImageView iv_bd = contentView.findViewById(R.id.iv_bd);

            LinearLayout line_gd = contentView.findViewById(R.id.line_gd);
            ImageView iv_gd = contentView.findViewById(R.id.iv_gd);

            LinearLayout line_tx = contentView.findViewById(R.id.line_tx);
            ImageView iv_tx = contentView.findViewById(R.id.iv_tx);

            line_bd.setVisibility(hasMap("百度") ? View.VISIBLE : View.GONE);
            line_gd.setVisibility(hasMap("高德") ? View.VISIBLE : View.GONE);
            line_tx.setVisibility(hasMap("腾讯") ? View.VISIBLE : View.GONE);

            Drawable drawableBd = getIcon("com.baidu.BaiduMap");
            if(null != drawableBd){
                iv_bd.setBackground(drawableBd);
            }

            Drawable drawableGd = getIcon("com.autonavi.minimap");
            if(null != drawableGd){
                iv_gd.setBackground(drawableGd);
            }
            Drawable drawableTx = getIcon("com.tencent.map");
            if(null != drawableTx){
                iv_tx.setBackground(drawableTx);
            }

            line_bd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openMap("百度");
                }
            });
            line_gd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openMap("高德");
                }
            });
            line_tx.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openMap("腾讯");
                }
            });
            bottomDialog.getWindow().setGravity(Gravity.BOTTOM);
            bottomDialog.setCanceledOnTouchOutside(true);
            bottomDialog.getWindow().setWindowAnimations(R.style.BottomDialog_Animation);
            bottomDialog.show();
        }
    }


    private Drawable getIcon(String pakgename) {
        PackageManager pm = ctx.getPackageManager();
        try {
            ApplicationInfo appInfo = pm.getApplicationInfo(pakgename, PackageManager.GET_META_DATA);
            return  pm.getApplicationIcon(appInfo);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    private boolean hasMap(String mapName){
        String[] mapNames = getMapName();
        for (String name :
                mapNames) {
            if ( name.equals(mapName) ){
                return true;
            }
        }
        return false;
    }

    public void showMapCheckDialog() {
        show1();
        /*final String[] mapName = getMapName();
        if(null == mapName) {
            return;
        }
        AlertDialog.Builder builder=new AlertDialog.Builder(ctx,AlertDialog.THEME_HOLO_LIGHT);
        builder.setTitle("请选择地图");
        builder.setItems(mapName, new DialogInterface.OnClickListener() {
            *//*int which 表示点击的item在string.xml文件字符串数组中的下标*//*
            @Override
            public void onClick(DialogInterface dialog, int which) {
                openMap(mapName[which]);
                dialog.dismiss();
            }
        });
        Dialog dlg = builder.create();
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dlg.show();*/
    }
    public void openMap(String mapName){
        LatLng gcLatLng = new LatLng(lat, lng);
        Intent naviIntent;
        switch (mapName)  {
            case "高德":
                naviIntent = new Intent();
                naviIntent.setAction(Intent.ACTION_VIEW);
                naviIntent.addCategory(Intent.CATEGORY_DEFAULT);
                Uri uri = Uri.parse("androidamap://navi?sourceApplication=天天五折&poiname=&lat="+gcLatLng.lat +"&lon="+gcLatLng.lng+"&dev=0&style=2");
                naviIntent.setData(uri);
                ctx.startActivity(naviIntent);
                break;
            case "百度":
                double[] latLng = CoordinateConvertUtil.gcj02_To_Bd09(lat,lng);
                naviIntent = new Intent("android.intent.action.VIEW", android.net.Uri.parse("baidumap://map/geocoder?location=" + latLng[0] + "," + latLng[1]));
                ctx.startActivity(naviIntent);
                break;
            case "腾讯":
                naviIntent = new Intent("android.intent.action.VIEW", android.net.Uri.parse("qqmap://map/routeplan?type=drive&from=&fromcoord=&to=目的地&tocoord=" + gcLatLng.lat + "," + gcLatLng.lng+ "&policy=0&referer=appName"));
                ctx.startActivity(naviIntent);
                break;
        }
    }
    public void diallPhone(String phoneNum) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse("tel:" + phoneNum);
        intent.setData(data);
        ctx.startActivity(intent);
    }

    public static String[] getMapName(){
        String str = "";
        if(isPackageInstalled("com.baidu.BaiduMap")){
            str += "百度,";
        }
        if(isPackageInstalled("com.autonavi.minimap")){
            str += "高德,";
        }
        if(isPackageInstalled("com.tencent.map")){
            str += "腾讯,";
        }
        if(str.length()>0){
            str = str.substring(0,str.length()-1);
            return str.split(",");
        } else {
            return null;
        }
    }

    private static boolean isPackageInstalled(String packageName) {
        return new File("/data/data/" + packageName).exists();
    }

    /**
     * 百度坐标系 (BD-09) 与 火星坐标系 (GCJ-02)的转换
     * 即 百度 转 谷歌、高德
     * @param latLng
     * @returns
     */
    public static LatLng BD09ToGCJ02(LatLng latLng) {
        double x_pi = 3.14159265358979324 * 3000.0 / 180.0;
        double x = latLng.lng - 0.0065;
        double y = latLng.lat - 0.006;
        double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * x_pi);
        double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * x_pi);
        double gg_lat = z * Math.sin(theta);
        double gg_lng = z * Math.cos(theta);
        return new LatLng(gg_lat, gg_lng);
    }

    // 计算两点距离
    private static final double EARTH_RADIUS = 6378137.0;

    /**
     * 计算两点经纬度距离（单位米）
     * @param lat_a
     * @param lng_a
     * @param lat_b
     * @param lng_b
     * @return
     */
    public static double distanceBetweenLocation(double lat_a, double lng_a, double lat_b, double lng_b) {
        double radLat1 = (lat_a * Math.PI / 180.0);
        double radLat2 = (lat_b * Math.PI / 180.0);
        double a = radLat1 - radLat2;
        double b = (lng_a - lng_b) * Math.PI / 180.0;
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
                + Math.cos(radLat1) * Math.cos(radLat2)
                * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000) / 10000;
        return s;
    }




}
