package com.example.coroutinesplayground.common

interface BaseAdapterItem {
    companion object {
        const val NO_ID: Long = -1
    }

    fun matches(other: BaseAdapterItem): Boolean
    fun same(other: BaseAdapterItem): Boolean
}

interface KotlinBaseAdapterItem<out T : Any> :
    BaseAdapterItem {

    override fun matches(other: BaseAdapterItem) = when (other) {
        is KotlinBaseAdapterItem<*> -> itemId() == other.itemId()
        else -> false
    }

    override fun same(other: BaseAdapterItem): Boolean = this == other

    fun itemId(): T
}
