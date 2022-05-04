package com.example.testgesture

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import io.hotplane.hotplane.HotPlane


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.button).setOnClickListener {
            startActivity(Intent(this, InsideActivity::class.java))
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {

        HotPlane.setDataPoint(ev, this)

        return super.dispatchTouchEvent(ev)
    }
}