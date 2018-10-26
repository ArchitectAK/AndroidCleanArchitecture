package com.cogitator.androidcleanarchitecture.base

import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView

/**
 * @author Ankit Kumar on 01/10/2018
 */

open class BaseViewHolder<out V : ViewDataBinding>(val binding: V) : RecyclerView.ViewHolder(binding.root)