package com.example.testgesture

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.widget.Button
import io.hotplane.hotplane.HotPlane

class InsideActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inside)

        findViewById<Button>(R.id.insideButton).setOnClickListener {
            finish()
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {

        HotPlane.setDataPoint(ev, this)

        return super.dispatchTouchEvent(ev)
    }
}