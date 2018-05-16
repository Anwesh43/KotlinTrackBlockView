package com.example.trackblockview

/**
 * Created by anweshmishra on 17/05/18.
 */

import android.view.View
import android.content.Context
import android.graphics.*
import android.view.MotionEvent

class TrackBlockView (ctx : Context) : View(ctx) {

    override fun onDraw(canvas : Canvas) {

    }

    override fun onTouchEvent(event : MotionEvent) : Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {

            }
        }
        return true
    }
}