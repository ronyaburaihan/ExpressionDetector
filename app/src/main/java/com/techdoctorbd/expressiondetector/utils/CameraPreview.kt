package com.techdoctorbd.expressiondetector.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import android.util.AttributeSet
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.ViewGroup
import com.google.android.gms.vision.CameraSource
import java.io.IOException

class CameraPreview(context: Context, attributeSet: AttributeSet) :
        ViewGroup(context, attributeSet) {

    private val surfaceView: SurfaceView
    private var startRequested = false
    private var surfaceAvailability = false
    private var cameraSource: CameraSource? = null
    private var graphicsOverlay: GraphicsOverlay? = null

    @Throws(IOException::class)
    fun start(cameraSource: CameraSource?) {
        if (cameraSource == null) {
            stop()
        }
        this.cameraSource = cameraSource

        if (this.cameraSource != null) {
            startRequested = true
            startIfReady()
        }
    }

    @Throws(IOException::class)
    fun start(cameraSource: CameraSource?, graphicsOverlay: GraphicsOverlay?) {
        this.graphicsOverlay = graphicsOverlay
        start(cameraSource)
    }

    private fun stop() {
        if (cameraSource != null) {
            cameraSource!!.stop()
        }
    }

    fun release() {
        if (cameraSource != null) {
            cameraSource!!.release()
            cameraSource = null
        }
    }

    @SuppressLint("MissingPermission")
    @Throws(IOException::class)
    private fun startIfReady() {
        if (startRequested && surfaceAvailability) {
            cameraSource!!.start()
            if (graphicsOverlay != null) {
                val size = cameraSource!!.previewSize
                val min = Math.min(size!!.width, size.height)
                val max = Math.max(size.width, size.height)

                if (isPortraitMode) {
                    graphicsOverlay!!.setCameraInfo(min, max, cameraSource!!.cameraFacing)
                } else {
                    graphicsOverlay!!.setCameraInfo(max, min, cameraSource!!.cameraFacing)
                }

                graphicsOverlay!!.clear()
            }

            startRequested = true
        }
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        TODO("Not yet implemented")
    }

    private val isPortraitMode: Boolean
        private get() {
            val orientation = context.resources.configuration.orientation
            if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                return true
            }
            return false
        }

    private inner class SurfaceCallback : SurfaceHolder.Callback {

        override fun surfaceCreated(holder: SurfaceHolder) {
            surfaceAvailability = true
            try {
                startIfReady()
            } catch (exception: IOException) {
                Log.e("CAMERA_PREVIEW", "Could not start camera source ${exception.localizedMessage}")
            }
        }

        override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {}

        override fun surfaceDestroyed(holder: SurfaceHolder) {
            surfaceAvailability = false
        }

    }

    init {
        surfaceView = SurfaceView(context)
        surfaceView.holder.addCallback(SurfaceCallback())
        addView(surfaceView)
    }
}