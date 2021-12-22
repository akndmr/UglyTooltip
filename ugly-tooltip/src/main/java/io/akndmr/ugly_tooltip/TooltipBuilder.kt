package io.akndmr.ugly_tooltip

import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.fragment.app.FragmentManager


/**
 * Created by Akın DEMİR on 2.06.2021.
 * Copyright (c) 2021
 */


class TooltipBuilder() : Parcelable {

    private var layoutRes = 0
    private var titleTextColorRes = 0
    private var textColorRes = 0
    private var shadowColorRes = 0
    private var titleTextSizeRes = 0
    private var textSizeRes = 0
    private var spacingRes = 0
    private var backgroundContentColorRes = 0
    private var circleIndicatorBackgroundDrawableRes = 0
    private var prevStringRes = 0
    private var prevStringText = ""
    private var nextStringText = ""
    private var finishStringText = ""
    private var skipStringText = ""
    private var nextStringRes = 0
    private var prevDrawableRes = 0
    private var nextDrawableRes = 0
    private var shouldShowIcons = false
    private var lineColorRes = 0
    private var lineWidthRes = 0
    private var finishStringRes = 0
    private var useCircleIndicator = true
    private var clickable = false
    private var useArrow = true
    private var arrowWidth = 0
    private var packageName: String? = null
    private var tooltipRadius: Int = 0
    private var showSpotlight: Boolean = true
    private var showBottomContainer: Boolean = true

    private var skipStringRes = 0
    private var useSkipWord = false

    private var childFragmentManager: FragmentManager? = null

    fun showBottomContainer(show: Boolean): TooltipBuilder {
        this.showBottomContainer = show
        return this
    }

    fun showBottomContainer(): Boolean {
        return this.showBottomContainer
    }

    fun setTooltipRadius(@DimenRes radius: Int): TooltipBuilder {
        this.tooltipRadius = radius
        return this
    }

    fun setPackageName(packageName: String?): TooltipBuilder {
        this.packageName = packageName
        return this
    }

    fun showSpotlight(shouldShow: Boolean): TooltipBuilder {
        this.showSpotlight = shouldShow
        return this
    }

    fun setFragmentManager(fm: FragmentManager): TooltipBuilder {
        this.childFragmentManager = fm
        return this
    }

    fun customView(customViewRes: Int): TooltipBuilder {
        layoutRes = customViewRes
        return this
    }

    fun textColorRes(@ColorRes textColorRes: Int): TooltipBuilder {
        this.textColorRes = textColorRes
        return this
    }

    fun titleTextColorRes(@ColorRes titleTextColorRes: Int): TooltipBuilder {
        this.titleTextColorRes = titleTextColorRes
        return this
    }

    fun shadowColorRes(@ColorRes shadowColorRes: Int): TooltipBuilder {
        this.shadowColorRes = shadowColorRes
        return this
    }

    fun useArrow(useArrow: Boolean): TooltipBuilder {
        this.useArrow = useArrow
        return this
    }

    fun useSkipWord(useSkipWord: Boolean): TooltipBuilder {
        this.useSkipWord = useSkipWord
        return this
    }

    fun textSizeRes(@DimenRes textSizeRes: Int): TooltipBuilder {
        this.textSizeRes = textSizeRes
        return this
    }

    fun titleTextSizeRes(@DimenRes titleTextSizeRes: Int): TooltipBuilder {
        this.titleTextSizeRes = titleTextSizeRes
        return this
    }

    fun spacingRes(@DimenRes spacingRes: Int): TooltipBuilder {
        this.spacingRes = spacingRes
        return this
    }

    fun arrowWidth(arrowWidth: Int): TooltipBuilder {
        this.arrowWidth = arrowWidth
        return this
    }

    fun backgroundContentColorRes(@ColorRes backgroundContentColorRes: Int): TooltipBuilder {
        this.backgroundContentColorRes = backgroundContentColorRes
        return this
    }

    fun circleIndicatorBackgroundDrawableRes(@DrawableRes circleIndicatorBackgroundDrawableRes: Int): TooltipBuilder {
        this.circleIndicatorBackgroundDrawableRes = circleIndicatorBackgroundDrawableRes
        return this
    }

    fun finishString(@StringRes finishStringRes: Int = 0, finishStringText: String = ""): TooltipBuilder {
        this.finishStringRes = finishStringRes
        this.finishStringText = finishStringText
        return this
    }

    fun prevString(@StringRes prevStringRes: Int = 0, prevStringText: String = ""): TooltipBuilder {
        this.prevStringRes = prevStringRes
        this.prevStringText = prevStringText
        return this
    }

    fun nextString(@StringRes nextStringRes: Int = 0, nextStringText: String = ""): TooltipBuilder {
        this.nextStringRes = nextStringRes
        this.nextStringText = nextStringText
        return this
    }

    fun skipString(@StringRes skipStringRes: Int = 0, skipStringText: String = ""): TooltipBuilder {
        this.skipStringRes = skipStringRes
        this.skipStringText = skipStringText
        return this
    }

    fun nextDrawableRes(@DrawableRes drawableRes: Int): TooltipBuilder {
        this.nextDrawableRes = drawableRes
        return this
    }

    fun prevDrawableRes(@DrawableRes drawableRes: Int): TooltipBuilder {
        this.prevDrawableRes = drawableRes
        return this
    }

    fun lineColorRes(@ColorRes colorRes: Int): TooltipBuilder {
        this.lineColorRes = colorRes
        return this
    }

    fun lineWidthRes(@DimenRes widthRes: Int): TooltipBuilder {
        this.lineWidthRes = widthRes
        return this
    }

    fun shouldShowIcons(shouldShow: Boolean): TooltipBuilder {
        this.shouldShowIcons = shouldShow
        return this
    }

    fun clickable(clickable: Boolean): TooltipBuilder {
        this.clickable = clickable
        return this
    }

    fun useCircleIndicator(useCircleIndicator: Boolean): TooltipBuilder {
        this.useCircleIndicator = useCircleIndicator
        return this
    }

    fun shouldShowSpotlight(): Boolean {
        return showSpotlight
    }

    fun getTooltipRadius(): Int {
        return tooltipRadius
    }

    fun getPackageName(): String? {
        return packageName
    }

    fun getTextColorRes(): Int {
        return textColorRes
    }

    fun getTitleTextColorRes(): Int {
        return titleTextColorRes
    }

    fun getTitleTextSizeRes(): Int {
        return titleTextSizeRes
    }

    fun getFinishStringRes(): Int {
        return finishStringRes
    }

    fun getFinishStringText(): String {
        return finishStringText
    }

    fun getSkipStringText(): String {
        return skipStringText
    }

    fun getNextStringText(): String {
        return nextStringText
    }

    fun getPrevStringText(): String {
        return prevStringText
    }

    fun getNextStringRes(): Int {
        return nextStringRes
    }

    fun getSkipStringRes(): Int {
        return skipStringRes
    }

    fun getPrevStringRes(): Int {
        return prevStringRes
    }

    fun getPrevDrawableRes(): Int {
        return prevDrawableRes
    }

    fun getNextDrawableRes(): Int {
        return nextDrawableRes
    }

    fun shouldShowIcons(): Boolean {
        return shouldShowIcons
    }

    fun getLineWidthRes(): Int {
        return lineWidthRes
    }

    fun getLineColorRes(): Int {
        return lineColorRes
    }

    fun useCircleIndicator(): Boolean {
        return useCircleIndicator
    }

    fun getShadowColorRes(): Int {
        return shadowColorRes
    }

    fun getTextSizeRes(): Int {
        return textSizeRes
    }

    fun getBackgroundContentColorRes(): Int {
        return backgroundContentColorRes
    }

    fun getCircleIndicatorBackgroundDrawableRes(): Int {
        return circleIndicatorBackgroundDrawableRes
    }

    fun isUseArrow(): Boolean {
        return useArrow
    }

    fun isUseSkipWord(): Boolean {
        return useSkipWord
    }

    fun getLayoutRes(): Int {
        return layoutRes
    }

    fun getArrowWidth(): Int {
        return arrowWidth
    }

    fun getSpacingRes(): Int {
        return spacingRes
    }

    fun isClickable(): Boolean {
        return clickable
    }

    fun build(): TooltipDialog? {
        return TooltipDialog.newInstance(this)
    }

    fun TooltipBuilder() {}

    protected fun TooltipBuilder(`in`: Parcel) {
        layoutRes = `in`.readInt()
        titleTextColorRes = `in`.readInt()
        textColorRes = `in`.readInt()
        shadowColorRes = `in`.readInt()
        titleTextSizeRes = `in`.readInt()
        textSizeRes = `in`.readInt()
        spacingRes = `in`.readInt()
        backgroundContentColorRes = `in`.readInt()
        circleIndicatorBackgroundDrawableRes = `in`.readInt()
        prevStringRes = `in`.readInt()
        nextStringRes = `in`.readInt()
        finishStringRes = `in`.readInt()
        useCircleIndicator = `in`.readByte().toInt() != 0
        clickable = `in`.readByte().toInt() != 0
        useArrow = `in`.readByte().toInt() != 0
        arrowWidth = `in`.readInt()
        skipStringRes = `in`.readInt()
        useSkipWord = `in`.readByte().toInt() != 0
    }
    
    constructor(parcel: Parcel) : this() {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TooltipBuilder> {
        override fun createFromParcel(parcel: Parcel): TooltipBuilder {
            return TooltipBuilder(parcel)
        }

        override fun newArray(size: Int): Array<TooltipBuilder?> {
            return arrayOfNulls(size)
        }
    }
}