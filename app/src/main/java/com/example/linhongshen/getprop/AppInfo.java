package com.example.linhongshen.getprop;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

/**
 * Created by linhongshen on 2017/5/15.
 */

public class AppInfo {

    String appName;
    String packageName;
    Drawable drawable;
    Intent appIntent;
    String appVersionCode;
    String appVersionName;
    private String sortLetters;  //显示数据拼音的首字母


    public AppInfo() {
    }

    public AppInfo(String appName) {
        this.appName = appName;
    }

    public AppInfo(String appName, String packageName) {
        this.appName = appName;
        this.packageName = packageName;
    }

    public AppInfo(String appName, String packageName, Drawable drawable) {
        this.appName = appName;
        this.packageName = packageName;
        this.drawable = drawable;
    }

    public AppInfo(String appName, String packageName, Drawable drawable, Intent appIntent) {
        this.appName = appName;
        this.packageName = packageName;
        this.drawable = drawable;
        this.appIntent = appIntent;
    }

    public AppInfo(String appName, String packageName, Drawable drawable, Intent appIntent, String appVersionCode, String appVersionName) {
        this.appName = appName;
        this.packageName = packageName;
        this.drawable = drawable;
        this.appIntent = appIntent;
        this.appVersionCode = appVersionCode;
        this.appVersionName = appVersionName;
    }

    public String getAppName() {
        if (null == appName)
            return "";
        else
            return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getPackageName() {
        if (null == packageName)
            return "";
        else
            return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public Drawable getDrawable() {
        return drawable;
    }

    public void setDrawable(Drawable drawable) {
        this.drawable = drawable;
    }

    public Intent getAppIntent() {
        return appIntent;
    }

    public void setAppIntent(Intent appIntent) {
        this.appIntent = appIntent;
    }

    public String getAppVersionCode(Context context)//获取版本号(内部识别号)
    {
        if (null == appVersionCode)
            return "0";
        else
            return appVersionCode;
//        try {
//            PackageInfo pi=context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
//            return pi.versionCode;
//        } catch (PackageManager.NameNotFoundException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//            return 0;
//        }
    }

    public void setAppVersionCode(String appVersionCode){
        this.appVersionCode = appVersionCode;
    }

    public String getAppVersionName(){
        if (null == appVersionName)
            return "0";
        else
            return appVersionName;
    }

    public void setAppVersionName(String appVersionName){
        this.appVersionName = appVersionName;
    }

    public String getSortLetters() {
        return sortLetters;
    }
    public void setSortLetters(String sortLetters) {
        this.sortLetters = sortLetters;
    }
}