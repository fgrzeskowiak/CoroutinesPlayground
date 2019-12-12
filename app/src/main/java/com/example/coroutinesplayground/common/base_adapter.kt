package com.example.coroutinesplayground.common

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import arrow.core.getOrElse
import arrow.core.singleOrNone

class BaseAdapter(private val managers: List<ViewHolderManager>) :
    ListAdapter<BaseAdapterItem, ViewHolderManager.BaseViewHolder<BaseAdapterItem>>(
        DiffUtilCallback()
    ) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolderManager.BaseViewHolder<BaseAdapterItem> {
        return managers[viewType].createViewHolder(
            parent,
            LayoutInflater.from(parent.context)
        ) as ViewHolderManager.BaseViewHolder<BaseAdapterItem>
    }

    override fun onBindViewHolder(
        holder: ViewHolderManager.BaseViewHolder<BaseAdapterItem>,
        position: Int
    ) {
        holder.bind(getItem(position))
    }

    override fun getItemViewType(position: Int): Int {
        val baseAdapterItem = getItem(position)
        return managers.withIndex().singleOrNone {
            it.value.matches(baseAdapterItem)
        }.map { it.index }
            .getOrElse { throw RuntimeException("Unsupported item type: $baseAdapterItem") }
    }

    override fun onViewRecycled(holder: ViewHolderManager.BaseViewHolder<BaseAdapterItem>) {
        holder.onViewRecycled()
        super.onViewRecycled(holder)
    }

    override fun onViewDetachedFromWindow(holder: ViewHolderManager.BaseViewHolder<BaseAdapterItem>) {
        super.onViewDetachedFromWindow(holder)
        holder.onViewDetachedFromWindow()
    }
}

interface ViewHolderManager {
    fun matches(baseAdapterItem: BaseAdapterItem): Boolean
    fun createViewHolder(
        parent: ViewGroup,
        inflater: LayoutInflater
    ): BaseViewHolder<*>

    abstract class BaseViewHolder<in T : BaseAdapterItem>(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        open fun onViewRecycled() {
        }

        open fun onViewDetachedFromWindow() {
        }

        open fun onDetachedFromRecyclerView() {

        }

        abstract fun bind(item: T)
    }
}