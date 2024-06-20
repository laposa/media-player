package com.laposa.domain.utils

class MyQueue<T>(private val maxSize: Int) {

    private val list = mutableListOf<T>()

    fun add(element: T) {
        if (list.size == maxSize) {
            list.removeAt(list.size - 1)
        }
        list.add(0, element)
    }

    fun getAll(): List<T> {
        return list.toList()
    }

    override fun toString(): String {
        return list.toString()
    }

    override fun equals(other: Any?): Boolean {
        return when (other) {
            is MyQueue<*> -> other.list == this.list
            else -> false
        }
    }

    override fun hashCode(): Int {
        var result = maxSize
        result = 31 * result + list.hashCode()
        super.hashCode()
        return result
    }

    companion object {
        fun <T> fromList(list: List<T>, size: Int? = null): MyQueue<T> {
            return MyQueue<T>(size ?: list.size).apply {
                list.reversed().forEach {
                    add(it)
                }
            }
        }
    }
}