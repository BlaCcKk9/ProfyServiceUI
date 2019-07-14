package com.example.profyuiservice.util

import android.view.View

// საჭირო ღილაკები რომელსაც მერე გადავტვირთავთ შესაბამის ფრაგმენტზე
interface ServiceClickListener {
    fun onScrollClicked(v: View)
    fun onAddPhotoClicked(v: View)
    fun onPlusClicked(v: View)
    fun onMinusClicked(v: View)
}