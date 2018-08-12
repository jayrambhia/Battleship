package com.fenchtose.battleship

fun <T> List<T>.add(t: T): List<T> {
    val mutable = toMutableList()
    mutable.add(t)
    return mutable
}

fun <T> List<T>.remove(t: T): List<T> {
    val mutable = toMutableList()
    mutable.remove(t)
    return mutable
}
