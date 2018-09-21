// (c)2016 Flipboard Inc, All Rights Reserved.

package com.example.administrator.kotlinexample.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import com.bumptech.glide.Glide
import com.example.administrator.kotlinexample.modle.GankBeauty
import com.wyw.search.historicalsearch.R
import android.widget.AdapterView.OnItemClickListener
import android.text.method.Touch.onTouchEvent
import android.view.MotionEvent
import android.text.method.Touch.onTouchEvent
import android.view.GestureDetector
import android.support.v4.view.GestureDetectorCompat
import android.widget.AdapterView
import android.widget.Toast


class ItemListAdapter : RecyclerView.Adapter<ItemListAdapter.DebounceViewHolder>(){
    private var mClickListener: OnItemClickListener? = null
    private val mGestureDetector: GestureDetectorCompat? = null
    internal var images: List<GankBeauty>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DebounceViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.grid_item, parent, false)
        return DebounceViewHolder(view)
    }

    override fun onBindViewHolder(holder: DebounceViewHolder?, position: Int) {
        val debounceViewHolder = holder as DebounceViewHolder

        val image = images!![position]
        Glide.with(holder.itemView.context).load(image.url).into(debounceViewHolder.imageIv)
        debounceViewHolder.descriptionTv.setText(image.createdAt)
    }

    override fun getItemCount(): Int {
        return if (images == null) 0 else images!!.size
    }

    fun setItems(images: List<GankBeauty>?) {
        this.images = images
        notifyDataSetChanged()
    }

     class DebounceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
         var descriptionTv: TextView = itemView.findViewById<TextView>(R.id.descriptionTv)
         var imageIv: ImageView = itemView.findViewById<ImageView>(R.id.imageIv)
    }


}
