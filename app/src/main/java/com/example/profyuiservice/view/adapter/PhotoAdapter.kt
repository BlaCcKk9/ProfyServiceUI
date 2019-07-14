package com.example.profyuiservice.view.adapter

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.profyuiservice.R
import kotlinx.android.synthetic.main.service_photo_item.view.*

class PhotoAdapter(private var photoList: ArrayList<Bitmap>) :
    RecyclerView.Adapter<PhotoAdapter.ViewHolder>(){

    // ლისტის განახლება
    fun updatePhotoList(newPhotoList: List<Bitmap>){
        photoList.clear()
        photoList.addAll(newPhotoList)
        notifyDataSetChanged()
    }
    // inflate გავუკეთეთ შესაბამის View-ს
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.service_photo_item, parent, false))

    // დავაბრუნეთ ლისტის ზომა
    override fun getItemCount(): Int = photoList.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // ჩავსვით სურათი რომელიც ატვირთა user-მა
        holder.view.photo.setImageBitmap(photoList[position])
    }

    class ViewHolder (val view: View): RecyclerView.ViewHolder(view)

}


