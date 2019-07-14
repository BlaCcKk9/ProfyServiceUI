package com.example.profyuiservice.util

import android.widget.ImageView
import androidx.databinding.BindingAdapter



// სურათი მინიჭების ფუნქცია DataBinding-ით
@BindingAdapter("android:imageUrl")
fun loadImage(view: ImageView, image: Int) {
    view.setImageResource(image)
}