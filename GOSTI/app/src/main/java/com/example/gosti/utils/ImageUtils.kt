package com.example.gosti.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64

object ImageUtils {

    fun decodeBase64(image: String): Bitmap {
        val bytes = Base64.decode(image, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
    }
}