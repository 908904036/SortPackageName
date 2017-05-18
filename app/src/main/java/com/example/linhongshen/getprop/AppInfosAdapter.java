package com.example.linhongshen.getprop;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import java.util.List;

/**
 * Created by linhongshen on 2017/5/15.
 */

public class AppInfosAdapter extends BaseAdapter implements SectionIndexer {

    Context context;
    List<AppInfo> appInfos;

    public AppInfosAdapter(){}

    public AppInfosAdapter(Context context , List<AppInfo> infos ){
        this.context = context;
        this.appInfos = infos;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public List<AppInfo> getAppInfos() {
        return appInfos;
    }

    public void setAppInfos(List<AppInfo> appInfos) {
        this.appInfos = appInfos;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        int count = 0;
        if(null != appInfos){
            return appInfos.size();
        };
        return count;
    }

    @Override
    public Object getItem(int index) {
        // TODO Auto-generated method stub
        return appInfos.get(index);
    }

    @Override
    public long getItemId(int index) {
        // TODO Auto-generated method stub
        return index;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup arg2) {
        // TODO Auto-generated method stub
        ViewHolder viewHolder = null;
        if(null == convertView){
            viewHolder = new ViewHolder();
            LayoutInflater mInflater = LayoutInflater.from(context);
            convertView = mInflater.inflate(R.layout.app_info_item, null);
            viewHolder.appIconImg = (ImageView)convertView.findViewById(R.id.app_icon);
            viewHolder.appNameText = (TextView)convertView.findViewById(R.id.app_info_name);
            viewHolder.appPackageText = (TextView)convertView.findViewById(R.id.app_info_package_name);
            viewHolder.appVersionCode = (TextView)convertView.findViewById(R.id.app_versioncode);
            viewHolder.appVersionName = (TextView)convertView.findViewById(R.id.app_versionname);
            viewHolder.tvLetter = (TextView) convertView.findViewById(R.id.catalog);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }
        if(null != appInfos){
            viewHolder.appIconImg.setBackground(appInfos.get(position).getDrawable());
            viewHolder.appNameText.setText(appInfos.get(position).getAppName());
            viewHolder.appPackageText.setText(appInfos.get(position).getPackageName());
            viewHolder.appVersionCode.setText(String.valueOf(appInfos.get(position).getAppVersionCode(context)));
            viewHolder.appVersionName.setText(appInfos.get(position).getAppVersionName());
            //根据position获取分类的首字母的Char ascii值
            int section = getSectionForPosition(position);

            //如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
            if(position == getPositionForSection(section)){
                viewHolder.tvLetter.setVisibility(View.VISIBLE);
                viewHolder.tvLetter.setText(appInfos.get(position).getSortLetters());
            }else{
                viewHolder.tvLetter.setVisibility(View.GONE);
            }
        }

        return convertView;
    }

    /**
     * 当ListView数据发生变化时,调用此方法来更新ListView
     * @param list
     */
    public void updateListView(List<AppInfo> list){
        this.appInfos = list;
        notifyDataSetChanged();
    }

    private final class ViewHolder{
        ImageView appIconImg;
        TextView appNameText;
        TextView tvLetter;
        TextView appPackageText;
        TextView appVersionCode;
        TextView appVersionName;
    }

    /**
     * 提取英文的首字母，非英文字母用#代替。
     *
     * @param str
     * @return
     */
    private String getAlpha(String str) {
        String  sortStr = str.trim().substring(0, 1).toUpperCase();
        // 正则表达式，判断首字母是否是英文字母
        if (sortStr.matches("[A-Z]")) {
            return sortStr;
        } else {
            return "#";
        }
    }

    /**
     * 根据ListView的当前位置获取分类的首字母的Char ascii值
     */
    public int getSectionForPosition(int position) {
        return appInfos.get(position).getSortLetters().charAt(0);
    }

    /**
     * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
     */
    public int getPositionForSection(int section) {
        for (int i = 0; i < getCount(); i++) {
            String sortStr = appInfos.get(i).getSortLetters();
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == section) {
                return i;
            }
        }

        return -1;
    }

    @Override
    public Object[] getSections() {
        return null;
    }

}