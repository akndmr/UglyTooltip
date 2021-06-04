# UglyTooltip

Forked from [Showcase](https://github.com/tokopedia/ShowCase) library and after kotlinization and refactoring, published as a new library.


[![](https://jitpack.io/v/akndmr/UglyTooltip.svg)](https://jitpack.io/#akndmr/UglyTooltip)


## Installation

Project level gradle

```bash
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```

App level gradle

```bash
dependencies {
	   implementation 'com.github.akndmr:UglyTooltip:v1.0.3'
	}
```

## How to

Init UglyTooltip with below custom properties.

You can set `shouldShowIcons` flag as false to show texts(previous and next) instead.

```kotlin
private fun initUglyTooltip() {
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
            .prevString(string.previous)
            .nextString(nextStringText = "Sonraki")
            .finishString(finishStringText = "Bitir da!")
            .useCircleIndicator(true)
            .clickable(true)
            .useArrow(true)
            .useSkipWord(false)
            .setFragmentManager(this.supportFragmentManager)
            .lineColorRes(color.line_color)
            .lineWidthRes(dimen.line_width)
            .shouldShowIcons(true)
            .setTooltipRadius(dimen.tooltip_radius)
            .build()
    }
```

Create a list of TooltipObjects and show tooltip dialog.

```kotlin
fun startUglyTooltips() {
        val tooltips: ArrayList<TooltipObject> = ArrayList()

        tooltips.add(
            TooltipObject(
                findViewById<Button>(R.id.button),
                null,
                "This is a button indeed."
            )
        )

        tooltips.add(
            TooltipObject(
                findViewById<TextView>(R.id.textView),
                "This is example title",
                "This HTML description point to <font color=\"#FFC300\"> a TextView </font> as you can see.<br/><br/> Lorem ipsum dolor sit amet, consectetur adipiscing elit. Suo enim quisque studio maxime ducitur. Scio enim esse quosdam, qui quavis lingua philosophari possint; Animum autem reliquis rebus ita perfecit, ut corpus; Quo modo autem optimum, si bonum praeterea nullum est?"
            )
        )


        tooltips.add(
            TooltipObject(
                findViewById<ImageView>(R.id.imageView),
                "This is example title",
                "This description point to <font color=\"#FFC300\">. This is yellow text </font> and this is white."
            )
        )

        tooltipDialog?.show(this   , supportFragmentManager, "SHOWCASE_TAG",  tooltips);

    }
```



