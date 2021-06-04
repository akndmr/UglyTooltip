package io.akndmr.uglytooltip.sample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import io.akndmr.ugly_tooltip.R.*
import io.akndmr.ugly_tooltip.TooltipBuilder
import io.akndmr.ugly_tooltip.TooltipDialog
import io.akndmr.ugly_tooltip.TooltipObject
import io.akndmr.uglytooltip.R

class MainActivity : AppCompatActivity() {

    var tooltipDialog: TooltipDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initShowCaseDialog()

        findViewById<Button>(R.id.button).setOnClickListener {
            startTooltips()
        }
    }

    private fun initShowCaseDialog() {
        tooltipDialog = TooltipBuilder()
            .setPackageName(packageName)
            .titleTextColorRes(R.color.white)
            .textColorRes(R.color.white)
            .shadowColorRes(color.shadow)
            .titleTextSizeRes(dimen.title_size)
            .textSizeRes(dimen.text_normal)
            .spacingRes(dimen.spacing_normal)
            .backgroundContentColorRes(color.darker_gray)
            .circleIndicatorBackgroundDrawableRes(drawable.selector_circle)
            .prevStringRes(string.previous)
            .nextStringRes(string.next)
            .finishStringRes(string.finish)
            .useCircleIndicator(true)
            .clickable(true)
            .useArrow(true)
            .useSkipWord(false)
            .setFragmentManager(this.supportFragmentManager)
            .lineColorRes(color.line_color)
            .lineWidthRes(dimen.line_width)
            .shouldShowIcons(true)
            .build()
    }

    fun startTooltips() {
        val showCaseList: ArrayList<TooltipObject> = ArrayList()

        showCaseList.add(
            TooltipObject(
                findViewById<Button>(R.id.button),
                null,
                "This is a button indeed."
            )
        )

        showCaseList.add(
            TooltipObject(
                findViewById<TextView>(R.id.textView),
                "This is example title",
                "This HTML description point to <font color=\"#FFC300\"> a TextView </font> as you can see.<br/><br/> Lorem ipsum dolor sit amet, consectetur adipiscing elit. Suo enim quisque studio maxime ducitur. Scio enim esse quosdam, qui quavis lingua philosophari possint; Animum autem reliquis rebus ita perfecit, ut corpus; Quo modo autem optimum, si bonum praeterea nullum est?"
            )
        )


        showCaseList.add(
            TooltipObject(
                findViewById<ImageView>(R.id.imageView),
                "This is example title",
                "This description point to <font color=\"#FFC300\">. This is yellow text </font> and this is white."
            )
        )

        tooltipDialog?.show(this   , supportFragmentManager, "SHOWCASE_TAG",  showCaseList);

    }
}