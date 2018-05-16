package com.example.trackblockview

/**
 * Created by anweshmishra on 17/05/18.
 */

import android.view.View
import android.content.Context
import android.graphics.*
import android.view.MotionEvent

class TrackBlockView (ctx : Context) : View(ctx) {

    private val renderer : Renderer = Renderer(this)

    private val paint : Paint = Paint(Paint.ANTI_ALIAS_FLAG)

    override fun onDraw(canvas : Canvas) {
        renderer.render(canvas, paint)
    }

    override fun onTouchEvent(event : MotionEvent) : Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                renderer.handleTap()
            }
        }
        return true
    }

    data class State (var prevScale : Float = 0f, var j : Int = 0, var dir : Float = 0f) {

        val scales : Array<Float> = arrayOf(0f, 0f, 0f)

        fun update(stopcb : (Float) -> Unit) {
            scales[j] += dir * 0.1f
            if (Math.abs(scales[j] - prevScale) > 1) {
                scales[j] = prevScale + dir
                j += dir.toInt()
                if (j == scales.size || j == -1) {
                    j -= dir.toInt()
                    dir = 0f
                    prevScale = scales[j]
                    stopcb(scales[j])
                }
            }
        }

        fun startUpdating(startcb : () -> Unit) {
            if (dir == 0f) {
                dir = 1 - 2 * prevScale
                startcb()
            }
        }
    }

    data class Animator (var view : View, var animated : Boolean = false) {

        fun animate(updatecb : () -> Unit) {
            if (animated) {
                updatecb()
                try {
                    Thread.sleep(50)
                    view.invalidate()
                } catch (ex : Exception) {

                }
            }
        }

        fun start() {
            if (!animated) {
                animated = true
                view.postInvalidate()
            }
        }

        fun stop() {
            if (animated) {
                animated = false
            }
        }
    }

    data class TrackBlock(var i : Int, val state : State = State()) {

        fun draw(canvas : Canvas, paint : Paint) {
            val w : Float = canvas.width.toFloat()
            val h : Float = canvas.height.toFloat()
            val size : Float = Math.min(w, h)/20
            paint.color = Color.WHITE
            for (i in 0..1) {
                canvas.save()
                canvas.translate(w/2, h/2)
                canvas.rotate(-90f * i)
                canvas.save()
                val path = Path()
                path.addRect(RectF(-size/2, -size/2, size/2, -size/2 + w/2 * (1 - state.scales[i * 2])), Path.Direction.CW)
                canvas.clipPath(path)
                for (j in 0..9) {
                    val gap : Float = (w/20)
                    canvas.save()
                    canvas.translate(0f, gap / 2 + gap * i)
                    canvas.drawRect(RectF(-gap/8, -gap/4, gap/8, gap/4), paint)
                    canvas.restore()
                }
                canvas.restore()
                paint.color = Color.parseColor("#f39c12")
                canvas.save()
                canvas.translate(0f, w/2 * (1 - state.scales[0]) - w/2 * state.scales[2])
                canvas.rotate(-90f * state.scales[1])
                canvas.drawRect(RectF(-size/4, -size/2, size/4, size/2), paint)
                canvas.restore()
                canvas.restore()
            }
        }

        fun update(stopcb : (Float) -> Unit) {
            state.update(stopcb)
        }

        fun startUpdating(startcb : () -> Unit) {
            state.startUpdating(startcb)
        }

    }

    data class Renderer (var view : TrackBlockView) {

        val trackBlock : TrackBlock = TrackBlock(0)

        val animator : Animator = Animator(view)

        fun render(canvas : Canvas, paint : Paint) {
            canvas.drawColor(Color.parseColor("#212121"))
            trackBlock.draw(canvas, paint)
            animator.animate {
                trackBlock.update {
                    animator.stop()
                }
            }
        }

        fun handleTap() {
            trackBlock.startUpdating {
                animator.start()
            }
        }
    }
}