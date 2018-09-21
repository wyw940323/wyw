package com.example.administrator.kotlinexample.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.administrator.kotlinexample.modle.ZhuangbiImage
import com.wyw.search.historicalsearch.R

/**
 * Created by Administrator on 2018/9/7.
 */

class ZhuangbiListAdapter : RecyclerView.Adapter<ZhuangbiListAdapter.DebounceViewHolder>() {
    internal var images: List<ZhuangbiImage>? = null


    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): DebounceViewHolder {
        val view = LayoutInflater.from(parent!!.getContext()).inflate(R.layout.grid_item, parent, false)
        return DebounceViewHolder(view)
    }

    override fun getItemCount(): Int {
        return if (images == null) 0 else images!!.size
    }

    fun setImages(images: List<ZhuangbiImage>?) {
        this.images = images
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: DebounceViewHolder?, position: Int) {
        val debounceViewHolder = holder as DebounceViewHolder
        val (description, image_url) = images!!.get(position)
        Glide.with(holder.itemView.context).load(image_url).into(debounceViewHolder.imageIv)
        debounceViewHolder.descriptionTv.setText(description)
    }


    class DebounceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var descriptionTv: TextView = itemView.findViewById<TextView>(R.id.descriptionTv)
        var imageIv: ImageView = itemView.findViewById<ImageView>(R.id.imageIv)
    }

}