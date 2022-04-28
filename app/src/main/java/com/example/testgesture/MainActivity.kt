package com.example.testgesture

import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import io.hotplane.hotplane.HotPlane


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {

        HotPlane.setDataPoint(ev, this)
        ev?.let {
//            Log.d("Event", it.action.toString());
//            Log.d("Event location", it.x.toString() + ";" + it.y.toString())


        }

//        val windowMetrics = WindowMetricsCalculator.getOrCreate().computeCurrentWindowMetrics(this)
//        val currentBounds = windowMetrics.bounds // E.g. [0 0 1350 1800]
//        val width = currentBounds.width()
//        val height = currentBounds.height()
//        Log.d("width:height", "$width:$height")

        return super.dispatchTouchEvent(ev)
    }

    override fun dispatchGenericMotionEvent(ev: MotionEvent?): Boolean {
        ev?.let {
            Log.d("Event", it.action.toString());
            Log.d("Event location", it.x.toString() + ";" + it.y.toString())
        }

        return super.dispatchGenericMotionEvent(ev)
    }
}