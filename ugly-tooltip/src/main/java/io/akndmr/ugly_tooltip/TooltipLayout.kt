package io.akndmr.ugly_tooltip

import android.content.Context
import android.graphics.*
import android.os.Build
import android.text.Html
import android.text.Spanned
import android.text.TextUtils
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.widget.FrameLayout
import android.widget.TextView
import androidx.annotation.Nullable
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatImageView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import io.akndmr.ugly_tooltip.R.*


/**
 * Created by Akın DEMİR on 2.06.2021.
 * Copyright (c) 2021
 */


class TooltipLayout : FrameLayout {

    // customized attribute
    private var layoutRes = 0
    private var textColor = 0
    private var titleTextColor = 0
    private var shadowColor = 0
    private var textSize = 0f
    private var textTitleSize = 0f
    private var spacing = 0
    private var arrowMargin = 0
    private var arrowWidth = 0
    private var useCircleIndicator = false

    private var isCancelable = false
    private var hasSkipWord = false

    private var prevString: String? = null
    private var nextString: String? = null
    private var finishString: String? = null
    private var skipString: String? = null
    private var shouldShowIcons: Boolean = false

    private var prevDrawableRes: Int = 0
    private var nextDrawableRes: Int = 0
    private var lineWidthRes: Int = 0
    private var lineColorRes: Int = 0

    private var backgroundContentColor = 0
    private var circleBackgroundDrawableRes = 0

    private var tooltipRadius: Int = 0

    // View
    private var viewGroup: ViewGroup? = null
    private var bitmap: Bitmap? = null
    private var lastTutorialView: View? = null
    private var viewPaint: Paint? = null

    // listener
    private var tooltipListener: TooltipListener? = null

    var tooltipContentPosition: TooltipContentPosition? = null

    private var highlightLocX = 0f
    private var highlightLocY = 0f

    // determined if this is last chain
    private var isStart = false
    private var isLast = false

    // path for arrow
    private var path: Path? = null
    private var arrowPaint: Paint? = null
    private var textViewTitle: TextView? = null
    private var textViewDesc: TextView? = null
    private var prevButton: TextView? = null
    private var nextButton: TextView? = null
    private var prevImageView: AppCompatImageView? = null
    private var nextImageView: AppCompatImageView? = null
    private var lineView: View? = null
    private var viewGroupIndicator: ViewGroup? = null

    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(context: Context, builder: TooltipBuilder?) : super(context) {
        init(context, builder)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(context, null)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
        init(context, null)
    }


    private fun init(context: Context, @Nullable builder: TooltipBuilder?) {
        visibility = View.GONE
        if (isInEditMode) {
            return
        }
        applyAttrs(context, builder)

        //setBackground, color
        initFrame()

        // setContentView
        initContent(context, builder)
        isClickable = isCancelable
        isFocusable = isCancelable
        if (isCancelable) {
            setOnClickListener { onNextClicked() }
        }
    }

    private fun onNextClicked() {
        if (tooltipListener != null) {
            if (isLast) {
                this@TooltipLayout.tooltipListener!!.onComplete()
            } else {
                this@TooltipLayout.tooltipListener!!.onNext()
            }
        }
    }

    private fun initFrame() {
        setWillNotDraw(false)
        viewPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        viewPaint!!.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
        arrowPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        arrowPaint!!.color = backgroundContentColor
        arrowPaint!!.style = Paint.Style.FILL
        setBackgroundColor(shadowColor)
    }

    fun setTooltipListener(showCaseListener: TooltipListener?) {
        this.tooltipListener = showCaseListener
    }

    @Throws(Throwable::class)
    fun showTutorial(
        view: View?,
        title: String?,
        text: String?,
        currentTutorIndex: Int,
        tutorsListSize: Int,
        tooltipContentPosition: TooltipContentPosition?,
        tintBackgroundColor: Int,
        customTarget: IntArray?,
        radius: Int
    ) {
        isStart = currentTutorIndex == 0
        isLast = currentTutorIndex == tutorsListSize - 1
        this.tooltipContentPosition = tooltipContentPosition
        if (bitmap != null) {
            bitmap!!.recycle()
        }
        if (TextUtils.isEmpty(title)) {
            textViewTitle!!.visibility = View.GONE
        } else {
            textViewTitle!!.text = fromHtml(title)
            textViewTitle!!.visibility = View.VISIBLE
        }
        textViewDesc!!.text = fromHtml(text)

        if (prevButton != null) {
            if (isStart) {
                if (hasSkipWord) {
                    prevButton!!.text = skipString
                    prevButton!!.visibility = View.VISIBLE
                } else {
                    prevButton!!.visibility = View.INVISIBLE
                }
            } else {
                if (!shouldShowIcons) {
                    prevButton!!.text = prevString
                    prevButton!!.visibility = View.VISIBLE
                }
            }
        }

        if (prevImageView != null) {
            if (shouldShowIcons) {
                if (isStart) {
                    if (hasSkipWord) {
                        prevButton!!.text = skipString
                        prevButton!!.visibility = View.VISIBLE
                        prevImageView!!.visibility = View.GONE
                    } else {
                        prevButton!!.visibility = View.GONE
                        prevImageView!!.visibility = View.INVISIBLE
                    }
                } else {
                    prevImageView!!.visibility = View.VISIBLE
                }
            }
        }

        if (nextButton != null) {
            if (isLast) {
                nextButton!!.text = finishString
            } else if (currentTutorIndex < tutorsListSize - 1) { // has next
                nextButton!!.text = nextString
            }
        }

        if (nextImageView != null) {
            if (shouldShowIcons) {
                if (isLast) {
                    nextButton!!.text = finishString
                    nextButton!!.visibility = View.VISIBLE
                    nextImageView!!.visibility = View.GONE
                } else if (currentTutorIndex < tutorsListSize - 1) { // has next
                    nextButton!!.visibility = View.GONE
                    nextImageView!!.visibility = View.VISIBLE
                }
            }
        }

        makeCircleIndicator(!isStart || !isLast, currentTutorIndex, tutorsListSize)
        if (view == null) {
            lastTutorialView = null
            bitmap = null
            highlightLocX = 0f
            highlightLocY = 0f
            moveViewToCenter()
        } else {
            lastTutorialView = view

            if (tintBackgroundColor == 0) {
                bitmap = getBitmapFromView(view)
            } else {
                val bitmapTemp: Bitmap? = getBitmapFromView(view)
                val bigBitmap = Bitmap.createBitmap(
                    view.measuredWidth,
                    view.measuredHeight, Bitmap.Config.ARGB_8888
                )
                val bigCanvas = Canvas(bigBitmap)
                bigCanvas.drawColor(tintBackgroundColor)
                val paint = Paint()

                if (bitmapTemp != null) {
                    bigCanvas.drawBitmap(bitmapTemp, 0f, 0f, paint)
                }
                bitmap = bigBitmap
                bitmapTemp?.recycle()
                bigBitmap?.recycle()
            }

            //set custom target to view
            if (customTarget != null && customTarget.isNotEmpty() && bitmap != null) {
                try {
                    if (customTarget.size == 2) {
                        bitmap = TooltipViewHelper.getCroppedBitmap(bitmap!!, customTarget, radius)
                    } else if (customTarget.size == 4) {
                        bitmap = TooltipViewHelper.getCroppedBitmap(bitmap!!, customTarget)
                    }
                } catch (e: Exception) {
                    Log.e("TooltipLayout", e.stackTraceToString())
                }

                highlightLocX = customTarget[0] - radius.toFloat()
                highlightLocY = customTarget[1] - radius.toFloat()
            } else { // use view location as target
                val location = IntArray(2)
                view.getLocationInWindow(location)
                highlightLocX = location[0].toFloat()
                highlightLocY =
                    location[1] - TooltipViewHelper.getStatusBarHeight(context).toFloat()
            }
            this.viewTreeObserver.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    if (this@TooltipLayout.bitmap != null) {
                        moveViewBasedHighlight(
                            this@TooltipLayout.highlightLocX,
                            this@TooltipLayout.highlightLocY,
                            this@TooltipLayout.highlightLocX + this@TooltipLayout.bitmap!!.width,
                            this@TooltipLayout.highlightLocY + this@TooltipLayout.bitmap!!.height
                        )
                        this@TooltipLayout.viewTreeObserver
                            .removeOnGlobalLayoutListener(this)
                        invalidate()
                    }
                }
            })
        }
        this.visibility = View.VISIBLE
    }

    fun getBitmapFromView(view: View): Bitmap? {
        var bitmap =
            Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        var canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }

    fun hideTutorial() {
        this.visibility = View.INVISIBLE
    }

    private fun makeCircleIndicator(
        hasMoreOneCircle: Boolean,
        currentTutorIndex: Int,
        tutorsListSize: Int
    ) {
        if (useCircleIndicator && viewGroupIndicator != null) {
            if (hasMoreOneCircle) { // has more than 1 circle
                // already has circle indicator
                if (viewGroupIndicator!!.childCount == tutorsListSize) {
                    for (i in 0 until tutorsListSize) {
                        val viewCircle: View = viewGroupIndicator!!.getChildAt(i)
                        viewCircle.isSelected = i == currentTutorIndex
                    }
                } else { //reinitialize, the size is different
                    viewGroupIndicator!!.removeAllViews()
                    val inflater = LayoutInflater.from(context)
                    for (i in 0 until tutorsListSize) {
                        val viewCircle: View = inflater.inflate(
                            layout.circle_view,
                            viewGroupIndicator,
                            false
                        )
                        viewCircle.setBackgroundResource(circleBackgroundDrawableRes)
                        if (i == currentTutorIndex) {
                            viewCircle.isSelected = true
                        }
                        viewGroupIndicator!!.addView(viewCircle)
                    }
                }
            } else {
                viewGroupIndicator!!.removeAllViews()
            }
        }
    }

    fun closeTutorial() {
        visibility = View.GONE
        if (bitmap != null) {
            bitmap!!.recycle()
            bitmap = null
        }
        if (lastTutorialView != null) {
            lastTutorialView = null
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        recycleResources()
    }

    override fun onDraw(canvas: Canvas) {
        if (bitmap == null || bitmap!!.isRecycled) {
            return
        }
        super.onDraw(canvas)
        canvas.drawBitmap(bitmap!!, highlightLocX, highlightLocY, viewPaint)

        // drawArrow
        if (path != null && arrowPaint != null && viewGroup!!.visibility == View.VISIBLE) {
            canvas.drawPath(path!!, arrowPaint!!)
        }
    }

    private fun applyAttrs(context: Context, @Nullable builder: TooltipBuilder?) {
        // set Default value before check builder (might be null)
        layoutRes = layout.tutorial_view
        textColor = Color.WHITE
        titleTextColor = Color.WHITE
        textTitleSize = resources.getDimension(dimen.title_size)
        textSize = resources.getDimension(dimen.text_normal)
        shadowColor = ContextCompat.getColor(context, color.shadow)
        spacing = resources.getDimension(dimen.spacing_normal).toInt()
        arrowMargin = spacing / 3
        arrowWidth = (1.5 * spacing).toInt()
        backgroundContentColor = Color.BLACK
        circleBackgroundDrawableRes = drawable.selector_circle
        prevString = getContext().getString(string.previous)
        nextString = getContext().getString(string.next)
        finishString = getContext().getString(string.finish)
        skipString = getContext().getString(string.skip)
        prevDrawableRes = drawable.ic_left_arrow
        nextDrawableRes = drawable.ic_right_arrow
        lineWidthRes = resources.getDimension(dimen.line_width).toInt()
        lineColorRes = ContextCompat.getColor(context, color.line_color)
        tooltipRadius = resources.getDimension(dimen.tooltip_radius).toInt()

        if (builder == null) {
            return
        }
        layoutRes = if (builder.getLayoutRes() != 0) builder.getLayoutRes() else layoutRes

        tooltipRadius = if (builder.getTooltipRadius() != 0) {
            resources.getDimension(builder.getTooltipRadius()).toInt()
        } else {
            tooltipRadius
        }

        prevDrawableRes = if (builder.getPrevDrawableRes() != 0) {
            builder.getPrevDrawableRes()
        } else {
            prevDrawableRes
        }

        shouldShowIcons = builder.shouldShowIcons()

        nextDrawableRes = if (builder.getNextDrawableRes() != 0) {
            builder.getNextDrawableRes()
        } else {
            nextDrawableRes
        }

        lineColorRes = if (builder.getLineColorRes() != 0) {
            ContextCompat.getColor(context, builder.getLineColorRes())
        } else {
            lineColorRes
        }

        lineWidthRes = if (builder.getLineWidthRes() != 0) {
            resources.getDimension(builder.getLineWidthRes()).toInt()
        } else {
            lineWidthRes
        }

        textColor = if (builder.getTextColorRes() != 0) ContextCompat.getColor(
            context,
            builder.getTextColorRes()
        ) else textColor
        titleTextColor = if (builder.getTitleTextColorRes() != 0) ContextCompat.getColor(
            context,
            builder.getTitleTextColorRes()
        ) else titleTextColor
        textTitleSize =
            if (builder.getTitleTextSizeRes() != 0) resources.getDimension(builder.getTitleTextSizeRes()) else textTitleSize
        textSize =
            if (builder.getTextSizeRes() != 0) resources.getDimension(builder.getTextSizeRes()) else textSize
        backgroundContentColor =
            if (builder.getBackgroundContentColorRes() != 0) ContextCompat.getColor(
                context,
                builder.getBackgroundContentColorRes()
            ) else backgroundContentColor
        shadowColor = if (builder.getShadowColorRes() != 0) ContextCompat.getColor(
            context,
            builder.getShadowColorRes()
        ) else shadowColor
        spacing = if (builder.getSpacingRes() != 0) resources.getDimension(builder.getSpacingRes())
            .toInt() else spacing
        circleBackgroundDrawableRes =
            if (builder.getCircleIndicatorBackgroundDrawableRes() != 0) builder.getCircleIndicatorBackgroundDrawableRes() else circleBackgroundDrawableRes
        prevString =
            when {
                builder.getPrevStringRes() != 0 -> getContext().getString(builder.getPrevStringRes())
                builder.getPrevStringText().isNotEmpty() -> builder.getPrevStringText()
                else -> prevString
            }
        nextString =
            when {
                builder.getNextStringRes() != 0 -> getContext().getString(builder.getNextStringRes())
                builder.getNextStringText().isNotEmpty() -> builder.getNextStringText()
                else -> nextString
            }
        finishString =
            when {
                builder.getFinishStringRes() != 0 -> getContext().getString(builder.getFinishStringRes())
                builder.getFinishStringText().isNotEmpty() -> builder.getFinishStringText()
                else -> finishString
            }
        skipString =
            when {
                builder.getSkipStringRes() != 0 -> getContext().getString(builder.getSkipStringRes())
                builder.getSkipStringText().isNotEmpty() -> builder.getSkipStringText()
                else -> skipString
            }
        useCircleIndicator = builder.useCircleIndicator()
        hasSkipWord = builder.isUseSkipWord()
        isCancelable = builder.isClickable()
        if (builder.isUseArrow()) {
            arrowMargin = spacing / 3
            arrowWidth =
                if (builder.getArrowWidth() != 0) resources.getDimension(builder.getArrowWidth())
                    .toInt() else arrowWidth
        } else {
            arrowMargin = 0
            arrowWidth = 0
        }
    }

    private fun initContent(context: Context, builder: TooltipBuilder?) {
        viewGroup = LayoutInflater.from(context).inflate(layoutRes, this, false) as ViewGroup

        if (builder != null) {
            val view_group_tutor_content = resources.getIdentifier(
                "view_group_tutor_content",
                "id",
                if (builder.getPackageName() != null) builder.getPackageName() else context.packageName
            )
            val text_title = resources.getIdentifier(
                "text_title",
                "id",
                if (builder.getPackageName() != null) builder.getPackageName() else context.packageName
            )
            val text_description = resources.getIdentifier(
                "text_description",
                "id",
                if (builder.getPackageName() != null) builder.getPackageName() else context.packageName
            )
            val view_line = resources.getIdentifier(
                "view_line",
                "id",
                if (builder.getPackageName() != null) builder.getPackageName() else context.packageName
            )
            val text_previous = resources.getIdentifier(
                "text_previous",
                "id",
                if (builder.getPackageName() != null) builder.getPackageName() else context.packageName
            )
            val text_next = resources.getIdentifier(
                "text_next",
                "id",
                if (builder.getPackageName() != null) builder.getPackageName() else context.packageName
            )
            val ic_previous = resources.getIdentifier(
                "ic_previous",
                "id",
                if (builder.getPackageName() != null) builder.getPackageName() else context.packageName
            )
            val ic_next = resources.getIdentifier(
                "ic_next",
                "id",
                if (builder.getPackageName() != null) builder.getPackageName() else context.packageName
            )
            val view_group_indicator = resources.getIdentifier(
                "view_group_indicator",
                "id",
                if (builder.getPackageName() != null) builder.getPackageName() else context.packageName
            )

            val viewGroupTutorContent: CardView =
                viewGroup!!.findViewById(view_group_tutor_content) as CardView
            viewGroupTutorContent.setCardBackgroundColor(backgroundContentColor)
            viewGroupTutorContent.radius = tooltipRadius.toFloat()
            //TooltipViewHelper.setBackgroundColor(viewGroupTutorContent, backgroundContentColor)
            textViewTitle = viewGroupTutorContent.findViewById(text_title)
            textViewTitle = viewGroupTutorContent.findViewById(text_title)
            textViewTitle!!.setTextColor(titleTextColor)
            textViewTitle!!.setTextSize(TypedValue.COMPLEX_UNIT_PX, textTitleSize)
            textViewDesc = viewGroupTutorContent.findViewById(text_description)
            textViewDesc!!.setTextColor(textColor)
            textViewDesc!!.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize)
            val line: View = viewGroupTutorContent.findViewById(view_line)
            if (line != null) {
                line.setBackgroundColor(textColor)
            }
            prevButton = viewGroupTutorContent.findViewById(text_previous)
            nextButton = viewGroupTutorContent.findViewById(text_next)
            prevImageView = viewGroupTutorContent.findViewById(ic_previous)
            nextImageView = viewGroupTutorContent.findViewById(ic_next)
            lineView = viewGroupTutorContent.findViewById(view_line)

            viewGroupIndicator = viewGroupTutorContent.findViewById(view_group_indicator)
        }

        if (lineView != null) {
            lineView!!.setBackgroundColor(lineColorRes)
            lineView!!.layoutParams.height = lineWidthRes
        }

        if (prevButton != null) {
            prevButton!!.text = prevString
            prevButton!!.setTextColor(textColor)
            prevButton!!.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize)
            prevButton!!.setOnClickListener {
                if (tooltipListener != null) {
                    if (this@TooltipLayout.isStart && hasSkipWord) {
                        this@TooltipLayout.tooltipListener!!.onComplete()
                    } else {
                        this@TooltipLayout.tooltipListener!!.onPrevious()
                    }
                }
            }
            if (shouldShowIcons) {
                prevButton!!.visibility = GONE
            }
        }
        if (nextButton != null) {
            nextButton!!.setTextColor(textColor)
            nextButton!!.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize)
            nextButton!!.setOnClickListener { onNextClicked() }
            if (shouldShowIcons) {
                nextButton!!.visibility = GONE
            }
        }

        if (prevImageView != null) {
            if (shouldShowIcons) {
                //There is icon, hide text
                if (prevButton != null) {
                    prevButton!!.visibility = View.GONE
                    prevImageView!!.setImageResource(prevDrawableRes)
                    prevImageView!!.visibility = View.VISIBLE
                    prevImageView!!.setOnClickListener {
                        if (tooltipListener != null) {
                            if (this@TooltipLayout.isStart && hasSkipWord) {
                                this@TooltipLayout.tooltipListener!!.onComplete()
                            } else {
                                this@TooltipLayout.tooltipListener!!.onPrevious()
                            }
                        }
                    }
                }
            }
        }

        if (nextImageView != null) {
            if (shouldShowIcons) {
                //There is icon, hide text
                if (nextButton != null) {
                    nextButton!!.visibility = View.GONE
                    nextImageView!!.setImageResource(nextDrawableRes)
                    nextImageView!!.visibility = View.VISIBLE
                    nextImageView!!.setOnClickListener { onNextClicked() }
                }
            }
        }

        this.addView(viewGroup)
    }

    private fun moveViewBasedHighlight(
        highlightXstart: Float,
        highlightYstart: Float,
        highlightXend: Float,
        highlightYend: Float
    ) {
        if (tooltipContentPosition === TooltipContentPosition.UNDEFINED) {
            val widthCenter = this.width / 2
            val heightCenter = this.height / 2
            tooltipContentPosition = if (highlightYend <= heightCenter) {
                TooltipContentPosition.BOTTOM
            } else if (highlightYstart >= heightCenter) {
                TooltipContentPosition.TOP
            } else if (highlightXend <= widthCenter) {
                TooltipContentPosition.RIGHT
            } else if (highlightXstart >= widthCenter) {
                TooltipContentPosition.LEFT
            } else { // not fit anywhere
                // if bottom is bigger, put to bottom, else put it on top
                if (this.height - highlightYend > highlightYstart) {
                    TooltipContentPosition.BOTTOM
                } else {
                    TooltipContentPosition.TOP
                }
            }
        }
        val layoutParams: LayoutParams
        when (tooltipContentPosition) {
            TooltipContentPosition.RIGHT -> {
                val expectedWidth = width - highlightXend - 2 * spacing
                viewGroup!!.measure(
                    MeasureSpec.makeMeasureSpec(expectedWidth.toInt(), MeasureSpec.EXACTLY),
                    MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
                )
                val viewGroupHeight = viewGroup!!.measuredHeight
                layoutParams = LayoutParams(
                    expectedWidth.toInt(),
                    LayoutParams.WRAP_CONTENT,
                    Gravity.RIGHT
                )
                layoutParams.rightMargin = spacing
                layoutParams.leftMargin = spacing
                layoutParams.bottomMargin = 0

                // calculate diff top height between object and the content;
                val hightLightHeight = highlightYend - highlightYstart
                val diffHeight = hightLightHeight - viewGroupHeight

                // check top margin. top should not out of window
                val expectedTopMargin = highlightYstart + diffHeight / 2
                checkMarginTopBottom(expectedTopMargin.toInt(), layoutParams, viewGroupHeight)
                setLayoutViewGroup(layoutParams)
                if (arrowWidth == 0) {
                    path = null
                } else {
                    val highLightCenterY = (highlightYend + highlightYstart) / 2
                    val recalcArrowWidth =
                        getRecalculateArrowWidth(highLightCenterY.toInt(), height)
                    if (recalcArrowWidth == 0) {
                        path = null
                    } else {
                        path = Path()
                        path?.moveTo(highlightXend + arrowMargin, highLightCenterY)
                        path?.lineTo(
                            highlightXend + spacing,
                            highLightCenterY - arrowWidth / 2
                        )
                        path!!.lineTo(
                            highlightXend + spacing,
                            highLightCenterY + arrowWidth / 2
                        )
                        path!!.close()
                    }
                }
            }
            TooltipContentPosition.LEFT -> {
                val expectedWidth = highlightXstart - 2 * spacing
                viewGroup!!.measure(
                    MeasureSpec.makeMeasureSpec(expectedWidth.toInt(), MeasureSpec.EXACTLY),
                    MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
                )
                val viewGroupHeight = viewGroup!!.measuredHeight
                layoutParams = LayoutParams(
                    expectedWidth.toInt(),
                    LayoutParams.WRAP_CONTENT,
                    Gravity.LEFT
                )
                layoutParams.leftMargin = spacing
                layoutParams.rightMargin = spacing
                layoutParams.bottomMargin = 0

                // calculate diff top height between object and the content;
                val hightLightHeight = highlightYend - highlightYstart
                val diffHeight = hightLightHeight - viewGroupHeight

                // check top margin. top should not out of window
                val expectedTopMargin = highlightYstart + diffHeight / 2
                checkMarginTopBottom(expectedTopMargin.toInt(), layoutParams, viewGroupHeight)
                setLayoutViewGroup(layoutParams)
                if (arrowWidth == 0) {
                    path = null
                } else {
                    val highLightCenterY = (highlightYend + highlightYstart) / 2
                    val recalcArrowWidth =
                        getRecalculateArrowWidth(highLightCenterY.toInt(), height)
                    if (recalcArrowWidth == 0) {
                        path = null
                    } else {
                        path = Path()
                        path!!.moveTo(highlightXstart - arrowMargin, highLightCenterY)
                        path!!.lineTo(
                            highlightXstart - spacing,
                            highLightCenterY - arrowWidth / 2
                        )
                        path!!.lineTo(
                            highlightXstart - spacing,
                            highLightCenterY + arrowWidth / 2
                        )
                        path!!.close()
                    }
                }
            }
            TooltipContentPosition.BOTTOM -> {
                layoutParams = LayoutParams(
                    LayoutParams.MATCH_PARENT,
                    LayoutParams.WRAP_CONTENT,
                    Gravity.TOP
                )
                layoutParams.topMargin = (highlightYend + spacing).toInt()
                layoutParams.leftMargin = spacing
                layoutParams.rightMargin = spacing
                layoutParams.bottomMargin = 0
                setLayoutViewGroup(layoutParams)
                if (arrowWidth == 0) {
                    path = null
                } else {
                    val highLightCenterX = (highlightXend + highlightXstart) / 2
                    val recalcArrowWidth = getRecalculateArrowWidth(highLightCenterX.toInt(), width)
                    if (recalcArrowWidth == 0) {
                        path = null
                    } else {
                        path = Path()
                        path!!.moveTo(highLightCenterX, highlightYend + arrowMargin)
                        path!!.lineTo(
                            highLightCenterX - recalcArrowWidth / 2,
                            highlightYend + spacing
                        )
                        path!!.lineTo(
                            highLightCenterX + recalcArrowWidth / 2,
                            highlightYend + spacing
                        )
                        path!!.close()
                    }
                }
            }
            TooltipContentPosition.TOP -> {
                layoutParams = LayoutParams(
                    LayoutParams.MATCH_PARENT,
                    LayoutParams.WRAP_CONTENT,
                    Gravity.BOTTOM
                )
                layoutParams.bottomMargin = (height - highlightYstart + spacing).toInt()
                layoutParams.topMargin = 0
                layoutParams.leftMargin = spacing
                layoutParams.rightMargin = spacing
                setLayoutViewGroup(layoutParams)
                if (arrowWidth == 0) {
                    path = null
                } else {
                    val highLightCenterX = (highlightXend + highlightXstart) / 2
                    val recalcArrowWidth = getRecalculateArrowWidth(highLightCenterX.toInt(), width)
                    if (recalcArrowWidth == 0) {
                        path = null
                    } else {
                        /**
                         *   Line to draws below to lines:
                         *    ___
                         *    \
                         *
                         *    When path closed, it's a filled triangle
                         *          ▼
                         */
                        path = Path()
                        path!!.moveTo(highLightCenterX, highlightYstart - arrowMargin)
                        path!!.lineTo(
                            highLightCenterX - recalcArrowWidth / 2,
                            highlightYstart - spacing
                        )
                        path!!.lineTo(
                            highLightCenterX + recalcArrowWidth / 2,
                            highlightYstart - spacing
                        )
                        path!!.close()
                    }
                }
            }
            TooltipContentPosition.UNDEFINED -> moveViewToCenter()
        }
    }

    private fun setLayoutViewGroup(params: LayoutParams) {
        viewGroup!!.visibility = View.INVISIBLE
        viewGroup!!.addOnLayoutChangeListener(object : OnLayoutChangeListener {
            override fun onLayoutChange(
                v: View?,
                left: Int,
                top: Int,
                right: Int,
                bottom: Int,
                oldLeft: Int,
                oldTop: Int,
                oldRight: Int,
                oldBottom: Int
            ) {
                this@TooltipLayout.viewGroup!!.visibility = View.VISIBLE
                this@TooltipLayout.viewGroup!!.removeOnLayoutChangeListener(this)
            }
        })
        viewGroup!!.layoutParams = params
        invalidate()
    }

    private fun getRecalculateArrowWidth(highlightCenter: Int, maxWidthOrHeight: Int): Int {
        var recalcArrowWidth = arrowWidth
        val safeArrowWidth = spacing + arrowWidth / 2
        if (highlightCenter < safeArrowWidth ||
            highlightCenter > maxWidthOrHeight - safeArrowWidth
        ) {
            recalcArrowWidth = 0
        }
        return recalcArrowWidth
    }

    private fun moveViewToCenter() {
        tooltipContentPosition = TooltipContentPosition.UNDEFINED
        val layoutParams = LayoutParams(
            LayoutParams.MATCH_PARENT,
            LayoutParams.WRAP_CONTENT,
            Gravity.CENTER
        )
        layoutParams.rightMargin = spacing
        layoutParams.leftMargin = spacing
        layoutParams.bottomMargin = spacing
        layoutParams.topMargin = spacing
        setLayoutViewGroup(layoutParams)
        path = null
    }

    private fun checkMarginTopBottom(
        expectedTopMargin: Int,
        layoutParams: LayoutParams,
        viewHeight: Int
    ) {
        if (expectedTopMargin < spacing) {
            layoutParams.topMargin = spacing
        } else {
            // check bottom margin. bottom should not out of window
            val prevActualHeight = expectedTopMargin + viewHeight + spacing
            if (prevActualHeight > height) {
                val diff = prevActualHeight - height
                layoutParams.topMargin = expectedTopMargin - diff
            } else {
                layoutParams.topMargin = expectedTopMargin
            }
        }
    }

    private fun recycleResources() {
        if (bitmap != null) {
            bitmap!!.recycle()
        }
        bitmap = null
        lastTutorialView = null
        viewPaint = null
    }

    private fun fromHtml(html: String?): Spanned? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY)
        } else {
            Html.fromHtml(html)
        }
    }


}