package com.app.dairy

import android.app.Activity
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment

fun Context.toast(msg: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, msg, duration).show()
}

fun Fragment.log(msg: String) {
    Log.i("ffnet", msg)
}

fun Activity.log(msg: String) {
    Log.i("ffnet", msg)
}