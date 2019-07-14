package com.example.profyuiservice.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.profyuiservice.R
import com.example.profyuiservice.databinding.ServiceTitleItemBinding
import com.example.profyuiservice.model.Service


class TitleAdapter (private var serviceList: ArrayList<Service>)
    : RecyclerView.Adapter<TitleAdapter.ViewHolder>(){

    // ლისტის განახლება
    fun updateServiceList(newServiceList: List<Service>){
        serviceList.clear()
        serviceList.addAll(newServiceList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflate გავუკეთეთ view-ს რომელშიც არის DataBinding და დავაბრუნეთ
        val inflater = LayoutInflater.from(parent.context)
        val view = DataBindingUtil.inflate<ServiceTitleItemBinding>(inflater, R.layout.service_title_item, parent, false)
        return ViewHolder(view)
    }
    // დავაბრუნეთ ლისტის ზომა
    override fun getItemCount(): Int = serviceList.size



    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // მივაბით Data view-ს  DataBinding-ის გამოყენებით
        holder.view.service = serviceList[position]

    }

    class ViewHolder(var view: ServiceTitleItemBinding): RecyclerView.ViewHolder(view.root)

}
