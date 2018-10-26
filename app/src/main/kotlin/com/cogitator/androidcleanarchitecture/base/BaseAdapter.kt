package com.cogitator.androidcleanarchitecture.base

import android.databinding.ViewDataBinding
import android.support.v7.util.DiffUtil
import android.view.ViewGroup
import android.support.v7.recyclerview.extensions.ListAdapter

/**
 * @author Ankit Kumar on 01/10/2018
 */

abstract class BaseAdapter<M, VB : ViewDataBinding>(callback: DiffUtil.ItemCallback<M>) : ListAdapter<M, BaseViewHolder<VB>>(callback) {

    override fun onBindViewHolder(holder: BaseViewHolder<VB>, position: Int) {
        bind(holder.binding, getItem(position), position)
        holder.binding.executePendingBindings()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<VB> {
        val binding = createBinding(parent, viewType)
        return BaseViewHolder(binding)
    }

    abstract fun createBinding(parent: ViewGroup, viewType: Int): VB

    protected abstract fun bind(binding: VB, item: M, position: Int)

}