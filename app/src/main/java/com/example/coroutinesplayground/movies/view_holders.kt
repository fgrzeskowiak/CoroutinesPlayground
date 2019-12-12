package com.example.coroutinesplayground.movies

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.coroutinesplayground.R
import com.example.coroutinesplayground.common.BaseAdapterItem
import com.example.coroutinesplayground.common.ViewHolderManager
import com.example.coroutinesplayground.view.clicks
import kotlinx.android.synthetic.main.movie_item.view.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

class MovieHolderManager(private val parentScope: CoroutineScope) :
    ViewHolderManager {
    override fun matches(baseAdapterItem: BaseAdapterItem) = baseAdapterItem is MovieAdapterItem

    override fun createViewHolder(parent: ViewGroup, inflater: LayoutInflater) =
        ViewHolder(
            inflater.inflate(
                R.layout.movie_item,
                parent,
                false
            )
        )

    inner class ViewHolder(itemView: View) :
        ViewHolderManager.BaseViewHolder<MovieAdapterItem>(itemView), CoroutineScope {

        private var job: CoroutineContext? = null
        override val coroutineContext: CoroutineContext
            get() = job ?: EmptyCoroutineContext

        override fun bind(item: MovieAdapterItem) {

            job = Job() + Dispatchers.Main

            itemView.movie_item_title.text = item.title

            itemView.clicks()
                .onEach { item.onClick() }
                .launchIn(this)

//            itemView.clicks()
//                .onEach { item.onClick() }
//                .launchIn(parentScope)

//            itemView.setOnClickListener { item.onClick() }
        }

        override fun onViewRecycled() {
            super.onViewDetachedFromWindow()
            job?.cancel()
        }
    }
}