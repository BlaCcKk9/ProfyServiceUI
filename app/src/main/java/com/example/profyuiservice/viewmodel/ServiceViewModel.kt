package com.example.profyuiservice.viewmodel


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.profyuiservice.R
import com.example.profyuiservice.model.Service


class ServiceViewModel : ViewModel() {

    val service = MutableLiveData<List<Service>>()

    fun fetchService(){
        // მონაცემები არ მაქვს დინამიურად, ასე რომ მომიწია შემექმნა ლოკალურად
        val serviceList = arrayListOf(
            Service("Chandelier", "This is a Chandelier", R.drawable.ic_circle),
            Service("Install", "This is an Install", R.drawable.ic_circle),
            Service("Lorem Ipsum", "This is a Lorem Ipsum", R.drawable.ic_circle)
        )
        service.value = serviceList

    }







}



