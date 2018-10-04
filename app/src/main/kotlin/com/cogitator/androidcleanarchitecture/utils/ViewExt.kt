package com.cogitator.androidcleanarchitecture.utils

import android.content.ContextWrapper
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * @author Ankit Kumar (ankit.kumar@capitalplatforms.net) on 09/08/2018
 */


/**
 * The `view` is asking the container to tell about its parent activity. The operation is
 * performed by the `ContextWrapper`.
 */
fun Fragment.getParentActivity(): AppCompatActivity? {
    var context = this.context
    while (context is ContextWrapper) {
        if (context is AppCompatActivity) {
            return context
        }
        context = context.baseContext
    }
    return null
}

fun ViewGroup.inflate(layoutRes: Int) = LayoutInflater.from(context).inflate(layoutRes, this, false)

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.invisble() {
    visibility = View.INVISIBLE
}

fun View.gone() {
    visibility = View.GONE
}