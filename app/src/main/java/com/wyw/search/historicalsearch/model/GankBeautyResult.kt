// (c)2016 Flipboard Inc, All Rights Reserved.

package com.example.administrator.kotlinexample.modle

import com.google.gson.annotations.SerializedName

class GankBeautyResult {
    var error: Boolean = false
    @SerializedName("results")
    lateinit var beauties: ArrayList<GankBeauty>
}
