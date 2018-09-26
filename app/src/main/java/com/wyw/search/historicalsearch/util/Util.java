package com.wyw.search.historicalsearch.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.InputFilter;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cc.shinichi.library.bean.ImageInfo;

/**
 * wyw
 * Created by Administrator on 2018/9/20.
 */

public class Util {

    private static long lastClickTime;

    /**
     *
     * @param hisRecords 历史记录
     * @return
     */
    public static String[] getHisString(String hisRecords) {
        String[] mVals = hisRecords.split(",");
        return mVals;
    }

    /**
     *
     * @param hisRecords  历史记录
     * @param content 搜索的内容
     * @return   这么保存8条
     */
    public static String getHisRecords(String hisRecords,String content) {
        String all = null;
        if (hisRecords.length() > 0) {
            if (!hisRecords.contains(content)) {
                String[] mVals = hisRecords.split(",");
                if (mVals.length < 8) {
                    all = content + "," + hisRecords;
                } else {
                    all = content + "," + hisRecords.substring(0, hisRecords.lastIndexOf(","));
                }
            }else{
                all = hisRecords;
            }
        } else {
            all = content;
        }
        return all;
    }

    // 保存数据
    public static void save(Context context, String key, String value) {
        SharedPreferences sp = context.getSharedPreferences("searchBoss", Context.MODE_PRIVATE |Context.MODE_MULTI_PROCESS);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, value);
        editor.commit();
    }
    // 取出数据
    public static String get(Context context,String key) {
        String value = "0";
        SharedPreferences sp = context.getSharedPreferences("searchBoss", Context.MODE_PRIVATE |Context.MODE_MULTI_PROCESS);
        value = sp.getString(key, "0");
        return value;
    }
    //清空数据
    public static void remove(Context context, String key){
        SharedPreferences.Editor editor = context.getSharedPreferences("searchBoss", Context.MODE_PRIVATE |Context.MODE_MULTI_PROCESS).edit();
        editor.remove(key);
        editor.commit();
    }

    //搜索框限制
    public static InputFilter getInputFilter(final Activity activity){
        InputFilter inputFilter=new InputFilter() {
            Pattern pattern = Pattern.compile("[^a-zA-Z0-9\\u4E00-\\u9FA5_]");
            public CharSequence filter(CharSequence charSequence, int i, int i1, Spanned spanned, int i2, int i3) {
                Matcher matcher=  pattern.matcher(charSequence);
                if(!matcher.find()){
                    return null;
                }else{
                    Toast.makeText(activity,"只能输入汉字,英文，数字",Toast.LENGTH_LONG).show();
                    return "";
                }
            }
        };
        return inputFilter;
    }

    /**
     *
     * @param viewText 文本内容
     * @param text 搜索的字
     * @param color 颜色
     * @return
     */
    public static SpannableString setColorText(String viewText, String text, int color){
        SpannableString spannableString= new SpannableString(viewText);
        if(!viewText.isEmpty() && viewText.indexOf(text) != -1){
            spannableString.setSpan(new ForegroundColorSpan(color),viewText.indexOf(text),viewText.indexOf(text)+text.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE );
            return spannableString;
        }
        return spannableString;
    }

    public static String changeObject2String(Object value) {
        String result = "";
        if (value != null && !value.equals("")) {
            result = value.toString();
        }
        return result;
    }

    public static boolean isNull(Object value) {
        if (value != null && !changeObject2String(value).trim().isEmpty()) {
            return false;
        }
        return true;
    }

    //键盘隐藏
    public static void showKeyboard(Activity activity, boolean isShow) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (isShow) {
            if (activity.getCurrentFocus() == null) {
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            } else {
                imm.showSoftInput(activity.getCurrentFocus(), 0);
            }
        } else {
            if (activity.getCurrentFocus() != null) {
                imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    public static boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if ( 0 < timeD && timeD < 800) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    public static void setBackgroundAlpha(float alpha, Activity context) {
        Window mWindow = context.getWindow();
        WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        lp.alpha = alpha;
        mWindow.setAttributes(lp);
    }

    public static List<ImageInfo> getImageInfoList(ArrayList<String> images){
        ImageInfo imageInfo;
        final List<ImageInfo> imageInfoList = new ArrayList<>();
        for (String image : images) {
            imageInfo = new ImageInfo();
            imageInfo.setOriginUrl(image);// 原图
            imageInfo.setThumbnailUrl(image);// 缩略图，实际使用中，根据需求传入缩略图路径。如果没有缩略图url，可以将两项设置为一样，并隐藏查看原图按钮即可。
            imageInfoList.add(imageInfo);
            imageInfo = null;
        }
        return imageInfoList;
    }

    public static List<ImageInfo> getImageInfoList(String images){
        ImageInfo imageInfo;
        final List<ImageInfo> imageInfoList = new ArrayList<>();
            imageInfo = new ImageInfo();
            imageInfo.setOriginUrl(images);// 原图
            imageInfo.setThumbnailUrl(images);// 缩略图，实际使用中，根据需求传入缩略图路径。如果没有缩略图url，可以将两项设置为一样，并隐藏查看原图按钮即可。
            imageInfoList.add(imageInfo);
            imageInfo = null;
        return imageInfoList;
    }
}
