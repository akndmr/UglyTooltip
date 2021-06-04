package io.akndmr.ugly_tooltip

import android.content.Context
import android.graphics.*
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.ShapeDrawable
import android.view.View
import android.view.ViewParent


/**
 * Created by Akın DEMİR on 2.06.2021.
 * Copyright (c) 2021
 */


object TooltipViewHelper {

    fun getRelativePositionRec(myView: View, root: ViewParent, location: IntArray) {
        if (myView.parent === root) {
            location[0] += myView.left
            location[1] += myView.top
        } else {
            location[0] += myView.left
            location[1] += myView.top
            getRelativePositionRec(myView.parent as View, root, location)
        }
    }

    fun setBackgroundColor(v: View, color: Int) {
        val background: Drawable? = v.background
        if (background is ShapeDrawable) {
            background.paint.color = color
        } else if (background is GradientDrawable) {
            background.setColor(color)
        } else {
            v.background = ColorDrawable(color)
            //v.setBackgroundColor(color)
        }
    }

    fun getCroppedBitmap(bitmap: Bitmap, centerLocation: IntArray, radius: Int): Bitmap? {
        val output = Bitmap.createBitmap(
            2 * radius,
            2 * radius, Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(output)
        val color = -0xbdbdbe
        val paint = Paint()
        val sourceRect = Rect(
            centerLocation[0] - radius,
            centerLocation[1] - radius,
            centerLocation[0] + radius,
            centerLocation[1] + radius
        )
        val destRect = Rect(0, 0, 2 * radius, 2 * radius)
        paint.isAntiAlias = true
        canvas.drawARGB(0, 0, 0, 0)
        paint.color = color
        canvas.drawCircle(
            radius.toFloat(), radius.toFloat(),
            radius.toFloat(), paint
        )
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        canvas.drawBitmap(bitmap, sourceRect, destRect, paint)
        return output
    }

    fun getCroppedBitmap(bitmap: Bitmap, rectLocation: IntArray): Bitmap? {
        val xStart = rectLocation[0]
        val yStart = rectLocation[1]
        val xEnd = rectLocation[2]
        val yEnd = rectLocation[3]
        val width = xEnd - xStart
        val height = yEnd - yStart
        val output = Bitmap.createBitmap(
            width,
            height, Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(output)
        val color = -0xbdbdbe
        val paint = Paint()
        val sourceRect = Rect(
            xStart,
            yStart,
            xEnd,
            yEnd
        )
        val destRect = Rect(0, 0, width, height)
        paint.isAntiAlias = true
        canvas.drawARGB(0, 0, 0, 0)
        paint.color = color
        canvas.drawRect(destRect, paint)
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        canvas.drawBitmap(bitmap, sourceRect, destRect, paint)
        return output
    }

    fun getStatusBarHeight(context: Context): Int {
        var height = 0
        val resId: Int =
            context.resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resId > 0) {
            height = context.resources.getDimensionPixelSize(resId)
        }
        return height
    }
}