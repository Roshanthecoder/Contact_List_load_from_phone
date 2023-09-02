package com.example.user.contactlist.model

import android.widget.ImageView
import androidx.databinding.BaseObservable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

data class Contact(
    var name: String? = null,
    var phoneNumber: String? = null,
    var photoUri: String? = null
) : BaseObservable()


fun loadImage1(view: ImageView, imageUrl: String?) {
    Glide.with(view.context)
        .load(imageUrl)
        .apply(RequestOptions.circleCropTransform())
        .into(view)
}