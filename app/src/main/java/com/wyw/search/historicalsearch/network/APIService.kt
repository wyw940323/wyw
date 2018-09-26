package com.example.administrator.kotlinexample.net

import com.example.administrator.kotlinexample.modle.GankBeautyResult
import com.example.administrator.kotlinexample.modle.ZhuangbiImage
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import rx.Observable

/**
 * Created by Administrator on 2018/9/10.
 */
interface APIService {

    @GET("search")
    fun getZhangbiImage(@Query("q") query: String): Observable<ArrayList<ZhuangbiImage>>

    @GET("data/{content}/{number}/{page}")
    fun getBeauties(@Path("content") content: String,@Path("number") number: Int, @Path("page") page: Int): Observable<GankBeautyResult>

}