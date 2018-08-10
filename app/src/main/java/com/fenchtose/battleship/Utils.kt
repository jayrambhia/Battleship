package com.fenchtose.battleship

fun <T> List<T>.add(t: T): List<T> {
    val mutable = toMutableList()
    mutable.add(t)
    return mutable
}