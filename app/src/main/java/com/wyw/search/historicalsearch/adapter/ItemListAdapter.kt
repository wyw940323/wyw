// (c)2016 Flipboard Inc, All Rights Reserved.

package com.example.administrator.kotlinexample.adapter

import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import cc.shinichi.library.ImagePreview

import com.bumptech.glide.Glide
import com.example.administrator.kotlinexample.modle.GankBeauty
import com.wyw.search.historicalsearch.R
import com.wyw.search.historicalsearch.adapter.MyListener
import com.wyw.search.historicalsearch.util.Util


class ItemListAdapter(val activitys: AppCompatActivity) : RecyclerView.Adapter<ItemListAdapter.DebounceViewHolder>(){
    private var listener : MyListener? = null
    internal var images: ArrayList<GankBeauty> = arrayListOf()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DebounceViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.grid_item, parent, false)
        return DebounceViewHolder(view,listener)
    }

    override fun onBindViewHolder(holder: DebounceViewHolder, position: Int) {
        val debounceViewHolder = holder as DebounceViewHolder

        val image = images[position]
        Glide.with(holder.itemView.context).load(image.url).into(debounceViewHolder.imageIv)
        debounceViewHolder.descriptionTv.setText(image.createdAt)
//        debounceViewHolder.imageIv.setOnClickListener{
//            //查看单张图片
//            ImagePreview
//                    .getInstance()
//                    .setContext(activitys)
//                    .setIndex(0)
//                    .setImageInfoList(Util.getImageInfoList(image.url))
//                    .setShowDownButton(true)
//                    .setLoadStrategy(ImagePreview.LoadStrategy.NetworkAuto)
//                    .setFolderName("BigImageViewDownload")
//                    .setScaleLevel(1, 3, 5)
//                    .setZoomTransitionDuration(300)
//                    .setShowCloseButton(true)
//                    .start()
//        }
    }

    override fun getItemCount(): Int {
        return if (images == null) 0 else images.size
    }

    fun setItems(imagess: ArrayList<GankBeauty>) {
        if(imagess.size>0){
            this.images.addAll(imagess)
        }else{
            this.images.clear()
        }
        notifyDataSetChanged()
    }

     class DebounceViewHolder(itemView: View,private var item : MyListener?) : RecyclerView.ViewHolder(itemView),View.OnClickListener {
         var descriptionTv: TextView = itemView.findViewById<TextView>(R.id.descriptionTv)
         var imageIv: ImageView = itemView.findViewById<ImageView>(R.id.imageIv)

         init {
             imageIv.setOnClickListener(this)
         }
         override fun onClick(v: View) {
             item!!.itemListener(v,position)
         }
    }

    fun getListener(item: MyListener){
        listener = item
    }



}
