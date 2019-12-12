package com.example.coroutinesplayground.common

import androidx.recyclerview.widget.DiffUtil
import com.example.coroutinesplayground.common.BaseAdapterItem

class DiffUtilCallback : DiffUtil.ItemCallback<BaseAdapterItem>() {
    override fun areItemsTheSame(
        oldItem: BaseAdapterItem,
        newItem: BaseAdapterItem
    ): Boolean {
        return newItem.matches(oldItem)
    }

    override fun areContentsTheSame(
        oldItem: BaseAdapterItem,
        newItem: BaseAdapterItem
    ): Boolean {
        return newItem.same(oldItem)
    }


}