package com.example.linhongshen.getprop;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private SideBar sideBar;
    private TextView dialog;
    private ClearEditText mClearEditText;

    /**
     * 汉字转换成拼音的类
     */
    private CharacterParser characterParser;

    ListView appInfoListView = null;
    List<AppInfo> appInfos = null;
    AppInfosAdapter infosAdapter = null;

    /**
     * 根据拼音来排列ListView里面的数据类
     */
    private PinyinComparator pinyinComparator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout);


        initViews();
        updateUI(appInfos);


    }

    public void updateUI(List<AppInfo> appInfos){
        if(null != appInfos){
            infosAdapter = new AppInfosAdapter(getApplication(), appInfos);
            appInfoListView.setAdapter(infosAdapter);
        }
    }

    // 获取包名信息
    public List<AppInfo> getAppInfos(){
        PackageManager pm = getApplication().getPackageManager();
        List<PackageInfo>  packgeInfos = pm.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES);
        appInfos = new ArrayList<AppInfo>();
        /* 获取应用程序的名称，不是包名，而是清单文件中的labelname
            String str_name = packageInfo.applicationInfo.loadLabel(pm).toString();
            appInfo.setAppName(str_name);
         */

        for(PackageInfo packgeInfo : packgeInfos){

            if((packgeInfo.applicationInfo.flags& ApplicationInfo.FLAG_SYSTEM)==0){
                String appName = packgeInfo.applicationInfo.loadLabel(pm).toString();
                String packageName = packgeInfo.packageName;
                Drawable drawable = packgeInfo.applicationInfo.loadIcon(pm);
                Intent appIntent = pm.getLaunchIntentForPackage(packgeInfo.packageName);// 获取该应用安装包的Intent，用于启动该应用
                String appVersionCode = String.valueOf(packgeInfo.versionCode);
                String appVersionName = packgeInfo.versionName;

                AppInfo appInfo = new AppInfo(appName, packageName,drawable,appIntent,appVersionCode,appVersionName);

                //汉字转换成拼音
                String pinyin = characterParser.getSelling(appName);
                String sortString = pinyin.substring(0, 1).toUpperCase();

                // 正则表达式，判断首字母是否是英文字母
                if(sortString.matches("[A-Z]")){
                    appInfo.setSortLetters(sortString.toUpperCase());
                }else{
                    appInfo.setSortLetters("#");
                }

                appInfos.add(appInfo);
            }

        }
        return appInfos;
    }


    private void initViews() {
        //实例化汉字转拼音类
        characterParser = CharacterParser.getInstance();

        pinyinComparator = new PinyinComparator();

        sideBar = (SideBar) findViewById(R.id.sidrbar);
        dialog = (TextView) findViewById(R.id.dialog);
        sideBar.setTextView(dialog);

        //设置右侧触摸监听
        sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {
                //该字母首次出现的位置
                int position = infosAdapter.getPositionForSection(s.charAt(0));
                if(position != -1){
                    appInfoListView.setSelection(position);
                }

            }
        });

        appInfoListView = (ListView)this.findViewById(R.id.appinfo_list);
        appInfoListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                AppInfo appInfo = appInfos.get(position);
                Toast.makeText(MainActivity.this, appInfo.getAppVersionName()+"-"+appInfo.getAppVersionCode(getApplication()), Toast.LENGTH_SHORT).show();
                startActivity(appInfo.appIntent);
            }
        });


        appInfos = getAppInfos();

        // 根据a-z进行排序源数据
        Collections.sort(appInfos, pinyinComparator);
        infosAdapter = new AppInfosAdapter(this, appInfos);
        appInfoListView.setAdapter(infosAdapter);


        mClearEditText = (ClearEditText) findViewById(R.id.filter_edit);

        //根据输入框输入值的改变来过滤搜索
        mClearEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
                filterData(s.toString());

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }


//    /**
//     * 为ListView填充数据
//     * @param date
//     * @return
//     */
//    private List<AppInfo> filledData(String [] date){
//
//
//        List<AppInfo> mSortList =
//                new ArrayList<AppInfo>();
//
//        for(int i=0; i<count; i++){
//            AppInfo sortModel = new AppInfo();
//            sortModel.setAppName(date[i]);
//            //汉字转换成拼音
//            String pinyin = characterParser.getSelling(date[i]);
//            String sortString = pinyin.substring(0, 1).toUpperCase();
//
//            // 正则表达式，判断首字母是否是英文字母
//            if(sortString.matches("[A-Z]")){
//                sortModel.setSortLetters(sortString.toUpperCase());
//            }else{
//                sortModel.setSortLetters("#");
//            }
//
//            mSortList.add(sortModel);
//        }
//        return mSortList;
//
//    }

    /**
     * 根据输入框中的值来过滤数据并更新ListView
     * @param filterStr
     */
    private void filterData(String filterStr){
        List<AppInfo> filterDateList = new ArrayList<AppInfo>();

        if(TextUtils.isEmpty(filterStr)){
            filterDateList = appInfos;
        }else{
            filterDateList.clear();
            for(AppInfo sortModel : appInfos){
                String name = sortModel.getAppName();
                Intent intent = sortModel.appIntent;
                if(name.indexOf(filterStr.toString()) != -1 || characterParser.getSelling(name).startsWith(filterStr.toString())){
                    sortModel.appIntent = intent;
                    Log.i("aaaaaaa",intent.toString());
                    filterDateList.add(sortModel);
                }
            }
        }

        // 根据a-z进行排序
        Collections.sort(filterDateList, pinyinComparator);
        infosAdapter.updateListView(filterDateList);
        appInfoListView = (ListView)this.findViewById(R.id.appinfo_list);
        final List<AppInfo> finalFilterDateList = filterDateList;
        appInfoListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                AppInfo appInfo = finalFilterDateList.get(position);
                Toast.makeText(MainActivity.this, appInfo.getAppVersionName()+"-"+appInfo.getAppVersionCode(getApplication()), Toast.LENGTH_SHORT).show();
                startActivity(appInfo.appIntent);
            }
        });

    }

}
