package io.hotplane.hotplane

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.pm.PackageInfo
import android.graphics.Bitmap
import android.util.Base64
import android.util.Log
import android.view.MotionEvent
import androidx.window.layout.WindowMetricsCalculator
import com.jraska.falcon.Falcon
import io.github.g00fy2.versioncompare.Version
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream


object HotPlane {
    private lateinit var accessCode: String
    private var currentPage: String = ""
    private var width: Int = 0
    private var height: Int = 0
    private var data: Data = Data(0, "", "", "", arrayListOf())
    private var objectCount: Int = 0
    private var version: String = ""

    private const val MAX_OBJECT : Int = 10
    private const val DEF_WIDTH: Int = 320
    private const val DEF_HEIGHT: Int = 480

    fun setAccessCode(ac : String, app: Application) {
        accessCode = ac
        val sp = app.getSharedPreferences("hotplane", Context.MODE_PRIVATE)
        val spId = sp.getString("hpid", "")
        if (spId.isNullOrEmpty()) {
            with(sp.edit()) {
                putString("hpid", getRandomString(6)).apply()
            }
        }
    }

    fun setWidthHeight(width: Int, height: Int) {
        HotPlane.width = width
        HotPlane.height = height
    }

    fun setDataPoint(motionEvent: MotionEvent?, activity: Activity) {
        if (currentPage != activity.localClassName) {
            currentPage = activity.localClassName

            // check screen version status
            val pInfo: PackageInfo =
                activity.packageManager.getPackageInfo(activity.packageName, 0)
            version = pInfo.versionName

            // check sharedpref
            val sharedPref = activity.getSharedPreferences("hotplane", Context.MODE_PRIVATE)
            val spVersion = sharedPref.getString("hp_" + activity.localClassName, "")
            if (spVersion.isNullOrEmpty() || Version(version) > Version(spVersion)) {
                with(sharedPref.edit()) {
                    putString("hp_" + activity.localClassName, version).apply()
                }
                // do screenshot
                screenshot(activity, version)
            }
        }

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
                data.version = version
                data.base = getRandomString(7)
                val sp = activity.getSharedPreferences("hotplane", Context.MODE_PRIVATE)
                data.uniqueId = sp.getString("hpid", "").toString()

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

    private fun screenshot(activity: Activity, version: String) {
        val bitmap = Falcon.takeScreenshotBitmap(activity)
        val sb = bitmapToString(bitmap)

        CoroutineScope(Dispatchers.IO).launch {
            val response = HotPlaneApi.getInstance().getScreen("application/json", accessCode, activity.localClassName, version)
            response.body()?.message?.let {
                m -> Log.e("updating", m)
            }
            response.body()?.let { m ->
                if(m.canUpdate) {
                    Log.e("updating", "Yeah")
                    val body = NewScreen(activity.localClassName, version, sb)
                    val updateResponse = HotPlaneApi.getInstance().postScreen("application/json", accessCode, body)
                    updateResponse.body()?.message?.let { n ->
                        Log.e("the response", n)
                    }
                }
            }
        }

    }

    private fun bitmapToString(bitmap: Bitmap): String {
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos)
        val b = baos.toByteArray()
        return Base64.encodeToString(b, Base64.DEFAULT)
    }

    fun getRandomString(length: Int) : String {
        val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
        return (1..length)
            .map { allowedChars.random() }
            .joinToString("")
    }
}