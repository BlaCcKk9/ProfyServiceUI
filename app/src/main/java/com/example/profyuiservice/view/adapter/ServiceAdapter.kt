package com.example.profyuiservice.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.profyuiservice.R
import com.example.profyuiservice.databinding.ServiceTextItemBinding
import com.example.profyuiservice.model.Service

class ServiceAdapter (private var serviceList: ArrayList<Service>):
    RecyclerView.Adapter<ServiceAdapter.ViewHolder>() {
    var counter = 0
    // ლისტის განახლება
    fun updateService(newServiceList: List<Service>){
        serviceList.clear()
        serviceList.addAll(newServiceList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflate გავუკეთეთ view-ს რომელშიც არის DataBinding და დავაბრუნეთ
        val inflater = LayoutInflater.from(parent.context)
        val view = DataBindingUtil.inflate<ServiceTextItemBinding>(inflater, R.layout.service_text_item, parent, false)
        return ViewHolder(view)
    }

    // დავაბრუნეთ ლისტის ზომა
    override fun getItemCount(): Int = serviceList.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        counter++
        // მივაბით Data view-ს  DataBinding-ის გამოყენებით
        holder.view.service = serviceList[position]

        // ჯობია არ გაარჩიოთ ;დდ იმედია შემინდობთ ;დ
        if(counter == 1)
            holder.view.upVerticalLine.visibility = View.GONE
        if(counter == serviceList.size)
            holder.view.downVerticalLine.visibility = View.GONE
    }

    class ViewHolder(val view: ServiceTextItemBinding): RecyclerView.ViewHolder(view.root)

}
