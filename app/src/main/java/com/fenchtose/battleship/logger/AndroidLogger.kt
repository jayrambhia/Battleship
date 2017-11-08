package com.fenchtose.battleship.logger

import android.util.Log

class AndroidLogger: Logger {
    override fun print(tag: String, s: String) {
        Log.d(tag, s)
    }

}