package com.wyw.search.historicalsearch;

import android.app.Activity;
import android.content.Context;
import android.text.SpannableStringBuilder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.wyw.search.historicalsearch.util.Util;


public class HistorSerarchPopupWindow extends PopupWindow {
    private boolean canDismiss = false;

    public Activity context;
    public TagOnClickListener onClickListener;
    public LinearLayout defaultBtnLi;
    public View popWindow;

    public TextView upTagTxt;
    public TextView downTagTxt;
    public TextView pleaseCheckTxt;

    public Button leftBtn;
    public Button rightBtn;
    public Button knowBtn;

    public String tagValue = "";
    public String leftValue = "";
    public String rightValue = "";
    public String knowValue = "";
    public String checkValue = "";
    public String upTag = "温馨提醒";
    public SpannableStringBuilder builder = null;

    public HistorSerarchPopupWindow(){

    }

    public static void showDeleteHistoricalRecordsPopupWindow(View parentView, Activity context,String  content,String left,String right,String upTag, TagOnClickListener onClickListener) {
        new HistorSerarchPopupWindow(context,content,left,right,upTag, onClickListener).showAtLocation(parentView, Gravity.CENTER, 0, 0);
    }

    public HistorSerarchPopupWindow(Activity context,String  content,String left,String right,String upTag,TagOnClickListener onClickListener) {
        this.context = context;
        this.onClickListener = onClickListener;
        this.tagValue = content;
        this.leftValue = left;
        this.rightValue = right;
        this.upTag = upTag;
        initView();
    }

    public void initView() {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        popWindow = inflater.inflate(R.layout.tag_main_popup_window_view, null);

        int parentHeight = context.getWindowManager().getDefaultDisplay().getHeight();
        int parentWidth = context.getWindowManager().getDefaultDisplay().getWidth();

        this.setContentView(popWindow);

        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);

        this.setFocusable(true);
        this.setOutsideTouchable(false);

        //刷新状态
        this.update();

        Util.setBackgroundAlpha(0.5f, context);
        this.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                Util.setBackgroundAlpha(1f, context);
            }
        });
        //设置载入和消失的动画
        //this.setAnimationStyle(R.style.AnimationPreview);

        leftBtn = (Button) popWindow.findViewById(R.id.leftBtn);
        rightBtn = (Button) popWindow.findViewById(R.id.rightBtn);

        upTagTxt = (TextView) popWindow.findViewById(R.id.upTag);
        downTagTxt = (TextView) popWindow.findViewById(R.id.downTag);
        pleaseCheckTxt = (TextView) popWindow.findViewById(R.id.pleaseCheck);
        defaultBtnLi = (LinearLayout) popWindow.findViewById(R.id.defaultBtnLi);
        knowBtn = (Button) popWindow.findViewById(R.id.knowBtn);

        bindInfoAndListener();

    }

    public void bindInfoAndListener() {


        if (isShowDoubleBtn()) {
            defaultBtnLi.setVisibility(View.VISIBLE);
            knowBtn.setVisibility(View.GONE);
        } else {
            defaultBtnLi.setVisibility(View.GONE);
            knowBtn.setVisibility(View.VISIBLE);
        }
        leftBtn.setText(leftValue);
        rightBtn.setText(rightValue);
        if (builder == null) {
            downTagTxt.setText(tagValue);
        } else {
            downTagTxt.setText(builder);
        }
        pleaseCheckTxt.setText(checkValue);
        knowBtn.setText(knowValue);
        upTagTxt.setText(upTag);

        leftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickListener.onClick(0);

                doDismiss();
            }
        });
        rightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickListener.onClick(1);
                doDismiss();
            }
        });
        knowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickListener.onClick(2);
                doDismiss();
            }
        });
    }



    public void doDismiss() {
        this.canDismiss = true;
        dismiss();
    }

    @Override
    public void dismiss() {
        if (canDismiss) {
            super.dismiss();
            this.canDismiss = false;
        }
    }

    public boolean isShowDoubleBtn() {
        return true;
    }

    public interface TagOnClickListener {
        void onClick(int result);
    }
}
