package com.wyw.search.historicalsearch

import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast

import com.example.administrator.kotlinexample.net.RetrofitManager
import com.wyw.search.historicalsearch.util.Util


import com.example.administrator.kotlinexample.adapter.ItemListAdapter
import com.example.administrator.kotlinexample.adapter.ZhuangbiListAdapter
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*

/**
 * Created by Administrator on 2018/9/20.
 */

class MainAcitivity : AppCompatActivity(), View.OnClickListener, SwipeRefreshLayout.OnRefreshListener{
    override fun onRefresh() {
        queryData(Util.changeObject2String(searchEdit!!.text))
    }

    private var mInflater: LayoutInflater? = null

    private var content = "" //保存点击键盘搜索或界面搜索时的文字
    private var hisRecords = ""

    internal var adapters = ItemListAdapter()
    internal var adapter1 = ZhuangbiListAdapter()
    private var index = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bindInfoAndListener()
    }

    protected fun bindInfoAndListener() {
        gridRv.layoutManager = GridLayoutManager(this, 2) as RecyclerView.LayoutManager?
//        gridRv.adapter = adapters
        gridRv.adapter = adapter1

        swipeRefreshLayout.setColorSchemeColors(Color.BLUE, Color.GREEN, Color.RED, Color.YELLOW)
        swipeRefreshLayout.isEnabled = true
        mInflater = LayoutInflater.from(this)
        swipeRefreshLayout.setOnRefreshListener(this)
        gridRv.setOnScrollListener(object : RecyclerView.OnScrollListener() {
            var lastVisibleItem: Int? = 0
            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem!! + 1 == adapters?.itemCount) {
                    index ++
                    search(index, content)
                    gridRv.scrollToPosition(0)
                }
            }

            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView?.layoutManager as LinearLayoutManager
                //最后一个可见的ITEM
                lastVisibleItem = layoutManager.findLastVisibleItemPosition()
            }
        })


        searchEdit!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable) {
                setSearchValueDeleteIconIsShow()
            }
        })

        searchEdit!!.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            // 修改回车键功能
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN) {
                if (Util.isNull(searchEdit!!.text)) {
                    println(Toast.makeText(this@MainAcitivity, "请输入搜索内容~", Toast.LENGTH_LONG))
                    Util.showKeyboard(this@MainAcitivity, true)
                    return@OnKeyListener false
                } else {
                    queryData(Util.changeObject2String(searchEdit!!.text))
                    his_search_ll!!.visibility = View.GONE
                    serarch_rl!!.visibility = View.VISIBLE
                    Util.save(this@MainAcitivity, "searchHistoricalRecordsBoss", Util.getHisRecords(hisRecords, Util.changeObject2String(searchEdit!!.text)))
                    initData()
                }
            }

            false
        })

        searchEdit!!.filters = arrayOf(Util.getInputFilter(this), InputFilter.LengthFilter(10))

        setSearchValueDeleteIconIsShow()
        initData()
        searchTv.setOnClickListener(this)
        deleteSearchValue.setOnClickListener(this)
        delete_img.setOnClickListener(this)


    }

    private fun setSearchValueDeleteIconIsShow() {
        if (!Util.isNull(searchEdit!!.text)) {
            deleteSearchValue!!.visibility = View.VISIBLE
            searchTv!!.text = "搜索"
        } else {
            deleteSearchValue!!.visibility = View.GONE
            searchTv!!.text = "取消"
        }
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.searchTv -> if ("取消" == searchTv!!.text) {
                (getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager)
                        .hideSoftInputFromWindow(searchEdit!!.windowToken,
                                0)
                finish()
            } else {
                queryData(Util.changeObject2String(searchEdit!!.text))
                his_search_ll!!.visibility = View.GONE
                serarch_rl!!.visibility = View.VISIBLE
                Util.save(this@MainAcitivity, "searchHistoricalRecordsBoss", Util.getHisRecords(hisRecords, Util.changeObject2String(searchEdit!!.text)))
                initData()
            }
            R.id.deleteSearchValue -> {
                searchEdit!!.setText("")
                setSearchValueDeleteIconIsShow()
                his_search_ll!!.visibility = View.VISIBLE
                serarch_rl!!.visibility = View.GONE
            }
            R.id.delete_img -> if (!Util.isFastDoubleClick()) {
                HistorSerarchPopupWindow.showDeleteHistoricalRecordsPopupWindow(delete_img, this@MainAcitivity, "确认删除全部搜索记录？", "取消", "删除", "删除搜索记录") { result ->
                    if (result == 1) {
                        Util.remove(this@MainAcitivity, "searchHistoricalRecordsBoss")
                        hisRecords = ""
                        initData()
                    }
                }
            }
        }
    }

    private fun initData() {
        if (Util.get(this@MainAcitivity, "searchHistoricalRecordsBoss") != "0") {
            hisRecords = Util.get(this@MainAcitivity, "searchHistoricalRecordsBoss")
        }
        val mVals = Util.getHisString(hisRecords)
        flowLayout!!.removeAllViews()
        for (i in mVals.indices) {
            if (!Util.isNull(mVals[i])) {
                val tv = mInflater!!.inflate(
                        R.layout.search_label_tv, flowLayout, false) as TextView
                tv.text = mVals[i]
                val str = tv.text.toString()
                //点击事件
                tv.setOnClickListener {
                    searchEdit!!.setText(str)
                    queryData(Util.changeObject2String(str))
                    his_search_ll!!.visibility = View.GONE
                    serarch_rl!!.visibility = View.VISIBLE
                }
                flowLayout!!.addView(tv)
            }
        }
    }

    fun queryData(content: String) {
        this.content = content
        setSearchValueDeleteIconIsShow()
        (getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager)
                .hideSoftInputFromWindow(currentFocus!!.windowToken,
                        0)

        index = 1
       // search(index, content)
        search(content)
    }

    fun search(pape: Int, content: String) {
        swipeRefreshLayout!!.isRefreshing = true
        RetrofitManager.builder("http://gank.io/api/")
                .getBannerGank(pape,content)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ images ->
                    swipeRefreshLayout!!.isRefreshing = false
                    adapters.setItems(images.beauties)
                    if(images.beauties.size>0){
                        none_data_li!!.visibility = View.GONE
                    }else{
                        none_data_li!!.visibility = View.VISIBLE

                    }
                }
                )
        swipeRefreshLayout!!.isRefreshing = false
        none_data_li!!.visibility = View.GONE
    }


    fun search(content: String) {
        swipeRefreshLayout!!.isRefreshing = true
        RetrofitManager.builder("http://www.zhuangbi.info/")
                .getBanner(content)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ images ->
                    swipeRefreshLayout!!.isRefreshing = false
                    adapter1.setImages(images)
                    if(images.size>0){
                        none_data_li!!.visibility = View.GONE
                    }else{
                        none_data_li!!.visibility = View.VISIBLE

                    }
                }
                )
        swipeRefreshLayout!!.isRefreshing = false
        none_data_li!!.visibility = View.GONE
    }

}
