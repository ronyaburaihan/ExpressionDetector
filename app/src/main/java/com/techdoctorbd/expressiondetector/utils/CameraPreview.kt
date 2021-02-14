package com.techdoctorbd.expressiondetector.utils

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import com.google.android.gms.vision.CameraSource

class CameraPreview(context: Context, attributeSet: AttributeSet) :
    ViewGroup(context, attributeSet) {

    //private val surfaceView: SurfaceView
    private var startRequested = false
    private var surfaceAvailability = false
    private var cameraSource: CameraSource? = null
    private var graphicsOverlay: GraphicsOverlay? = null

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        TODO("Not yet implemented")
    }

}