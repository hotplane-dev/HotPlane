package io.hotplane.hotplane

import android.app.Activity
import android.util.Log
import android.view.MotionEvent
import androidx.window.layout.WindowMetricsCalculator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

object HotPlane {
    private lateinit var accessCode: String
    private var width: Int = 0
    private var height: Int = 0
    private var data: Data = Data(0, arrayListOf())
    private var objectCount: Int = 0

    private const val MAX_OBJECT : Int = 10
    private const val DEF_WIDTH: Int = 320
    private const val DEF_HEIGHT: Int = 480

    fun setAccessCode(ac : String) {
        accessCode = ac
    }

    fun setWidthHeight(width: Int, height: Int) {
        HotPlane.width = width
        HotPlane.height = height
    }

    fun setDataPoint(motionEvent: MotionEvent?, activity: Activity) {
        motionEvent?.let {
            if(it.action != 0) {
                return
            }

            if(width == 0 || height == 0) {
                val windowMetrics = WindowMetricsCalculator.getOrCreate().computeCurrentWindowMetrics(activity)
                val currentBounds = windowMetrics.bounds // E.g. [0 0 1350 1800]
                val theWidth = currentBounds.width()
                val theHeight = currentBounds.height()
//            Log.e("wh", theWidth.toString() + " " + theHeight.toString())

                setWidthHeight(theWidth, theHeight)
            }

            Log.e("coba cek", it.x.toString() + " " + width)

            val finX = (DEF_WIDTH * it.x / width).toInt()
            val finY = (DEF_HEIGHT * it.y / height).toInt()

            val point = "$finX:$finY"
            val name = activity.localClassName

            if(data.events.size == 0 || data.events.last().screenId != name) {
                val al = ArrayList<String>()
                al.add(point)
                data.events.add(Event(name, al))
            } else {
                data.events.last().points.add(point)
            }
            objectCount += 1

            if (objectCount >= MAX_OBJECT) {
                // do something here
                data.count = data.events.size
                CoroutineScope(Dispatchers.IO).launch {
                    val response = HotPlaneApi.getInstance()
                        .postEvents("application/json", accessCode, data)
                    response.body()?.message?.let { m -> Log.e("the response", m) }

                    withContext(Dispatchers.Main) {
                        // reset data
                        data.events = arrayListOf()
                        objectCount = 0
                    }
                }
            }
        }

    }
}