package io.akndmr.ugly_tooltip

import android.view.View
import android.view.ViewGroup


/**
 * Created by Akın DEMİR on 2.06.2021.
 * Copyright (c) 2021
 */


class TooltipObject(val view: View?,
                    val title: String?,
                    val text: String?,
                    val tooltipContentPosition: TooltipContentPosition = TooltipContentPosition.UNDEFINED,
                    val tintBackgroundColor: Int = 0,
                    val scrollView: ViewGroup? = null) {

    private var location: IntArray = IntArray(0)
    private var radius = 0
    fun withCustomTarget(location: IntArray, radius: Int): TooltipObject {
        if (location.size != 2) {
            return this
        }
        this.location = location
        this.radius = radius
        return this
    }

    fun withCustomTarget(location: IntArray): TooltipObject {
        if (location.size != 4) {
            return this
        }
        this.location = location
        return this
    }

    fun getLocation(): IntArray? {
        return location
    }

    fun getRadius(): Int {
        return radius
    }
}