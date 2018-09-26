package com.wyw.search.historicalsearch;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.kotlinexample.adapter.ZhuangbiListAdapter;
import com.example.administrator.kotlinexample.net.RetrofitManager;
import com.wyw.search.historicalsearch.util.Util;


import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import kotlin.TypeCastException;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2018/9/20.
 */

public class NextActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {
    @BindView(R.id.search_vipname)
    ImageView searchVipname;
    @BindView(R.id.searchEdit)
    EditText searchEdit;
    @BindView(R.id.deleteSearchValue)
    ImageView deleteSearchValue;
    @BindView(R.id.vipparent)
    RelativeLayout vipparent;
    @BindView(R.id.searchTv)
    TextView searchTv;
    @BindView(R.id.delete_img)
    ImageView deleteImg;
    @BindView(R.id.flowLayout)
    FlowLayoutLabel flowLayout;
    @BindView(R.id.his_search_ll)
    LinearLayout hisSearchLl;
    @BindView(R.id.gridRv)
    RecyclerView gridRv;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.none_data_li)
    LinearLayout noneDataLi;
    @BindView(R.id.serarch_rl)
    RelativeLayout serarchRl;

    private LayoutInflater mInflater;
    private String content = "";
    private String hisRecords = "";
    ZhuangbiListAdapter adapter = new ZhuangbiListAdapter();
    private int index = 1;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        bindInfoAndListener();
    }

    private void bindInfoAndListener(){
        mInflater = LayoutInflater.from(this);
        gridRv.setLayoutManager(new GridLayoutManager(NextActivity.this, 2));
        gridRv.setAdapter(adapter);
        swipeRefreshLayout.setColorSchemeColors(Color.BLUE, Color.GREEN, Color.RED, Color.YELLOW);
        swipeRefreshLayout.setEnabled(false);
        swipeRefreshLayout.setOnRefreshListener(this);
        gridRv.setOnScrollListener(new RecyclerView.OnScrollListener() {
            int lastVisibleItem = 0;
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem+ 1 == adapter.getItemCount()) {
                    index ++;
                    search( content);
                    gridRv.scrollToPosition(0);
                }
            }
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                RecyclerView.LayoutManager var10000 = recyclerView != null?recyclerView.getLayoutManager():null;
                if(var10000 == null) {
                    throw new TypeCastException("null cannot be cast to non-null type android.support.v7.widget.LinearLayoutManager");
                } else {
                    LinearLayoutManager layoutManager = (LinearLayoutManager)var10000;
                    this.lastVisibleItem = Integer.valueOf(layoutManager.findLastVisibleItemPosition());
                }
            }
        });

        searchEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            public void afterTextChanged(Editable s) {
                setSearchValueDeleteIconIsShow();
            }
        });

        searchEdit.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // 修改回车键功能
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (Util.isNull(searchEdit.getText())) {
                       Toast.makeText(NextActivity.this, "请输入搜索内容~", Toast.LENGTH_LONG).show();
                        Util.showKeyboard(NextActivity.this, true);
                        return false;
                    } else {
                        queryData(Util.changeObject2String(searchEdit.getText()));
                        serarchRl.setVisibility(View.VISIBLE);
                        Util.save(NextActivity.this, "searchHistoricalRecordsBoss", Util.getHisRecords(hisRecords,Util.changeObject2String(searchEdit.getText())));
                        initData();
                    }
                }

                return false;
            }
        });

        searchEdit.setFilters((new InputFilter[]{Util.getInputFilter(this),new InputFilter.LengthFilter(10)}));

        setSearchValueDeleteIconIsShow();
        initData();
        searchTv.setOnClickListener(this);
        deleteSearchValue.setOnClickListener(this);
        deleteImg.setOnClickListener(this);
    }


   private void search(String content) {
        swipeRefreshLayout.setRefreshing(true);
        RetrofitManager.Companion.builder("http://www.zhuangbi.info/").getBanner(content).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe((Action1) (new Action1() {
            public void call(Object var1) {
                this.call((List) var1);
            }
            public final void call(List images) {
                swipeRefreshLayout.setRefreshing(false);
                adapter.setImages(images);
                if (images.size() > 0) {
                    noneDataLi.setVisibility(View.GONE);
                } else {
                    noneDataLi.setVisibility(View.VISIBLE);
                }

            }
        }));
        swipeRefreshLayout.setRefreshing(false);
        noneDataLi.setVisibility(View.GONE);
    }

    @Override
    public void onRefresh() {
        queryData(Util.changeObject2String(searchEdit.getText().toString()));
    }

    public void queryData(String content) {
        this.content = content;
        setSearchValueDeleteIconIsShow();
        ((InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        0);
        index = 1;
        search(content);
    }

    private void setSearchValueDeleteIconIsShow() {
        if (!Util.isNull(searchEdit.getText())) {
            deleteSearchValue.setVisibility(View.VISIBLE);
            searchTv.setText("搜索");
        } else {
            deleteSearchValue.setVisibility(View.GONE);
            searchTv.setText("取消");
        }
    }

    private void initData() {
        if (!(Util.get(NextActivity.this,"searchHistoricalRecordsBoss").equals("0"))) {
            hisRecords = Util.get(NextActivity.this,"searchHistoricalRecordsBoss");
        }
        String[] mVals = Util.getHisString(hisRecords);
        flowLayout.removeAllViews();
        for (int i = 0; i < mVals.length; i++) {
            if (!Util.isNull(mVals[i])) {
                TextView tv = (TextView) mInflater.inflate(
                        R.layout.search_label_tv, flowLayout, false);
                tv.setText(mVals[i]);
                final String str = tv.getText().toString();
                //点击事件
                tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        searchEdit.setText(str);
                        queryData(Util.changeObject2String(str));
                        hisSearchLl.setVisibility(View.GONE);
                        serarchRl.setVisibility(View.VISIBLE);
                    }
                });
                flowLayout.addView(tv);
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.searchTv:
                if ("取消".equals(searchTv.getText())) {
                    ((InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(searchEdit.getWindowToken(),
                                    0);
                    finish();
                } else {
                    queryData(Util.changeObject2String(searchEdit.getText()));
                    hisSearchLl.setVisibility(View.GONE);
                    serarchRl.setVisibility(View.VISIBLE);
                    Util.save(NextActivity.this, "searchHistoricalRecordsBoss", Util.getHisRecords(hisRecords,Util.changeObject2String(searchEdit.getText())));
                    initData();
                }
                break;
            case R.id.deleteSearchValue:
                searchEdit.setText("");
                setSearchValueDeleteIconIsShow();
                hisSearchLl.setVisibility(View.VISIBLE);
                serarchRl.setVisibility(View.GONE);
                break;
            case R.id.delete_img:
                if (!Util.isFastDoubleClick()) {
                    HistorSerarchPopupWindow.showDeleteHistoricalRecordsPopupWindow(deleteImg, NextActivity.this, "确认删除全部搜索记录？", "取消", "删除", "删除搜索记录", new HistorSerarchPopupWindow.TagOnClickListener() {
                        @Override
                        public void onClick(int result) {
                            if (result == 1) {
                                Util.remove(NextActivity.this, "searchHistoricalRecordsBoss");
                                hisRecords = "";
                                initData();
                            }
                        }
                    });
                }
                break;
        }
    }
}
