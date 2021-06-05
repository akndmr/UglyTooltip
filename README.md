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
	   implementation 'com.github.akndmr:UglyTooltip:v1.0.4-beta'
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
            .shadowColorRes(color.shadow) // Overlay canvas color
            .titleTextSizeRes(dimen.title_size)
            .textSizeRes(dimen.text_normal)
            .spacingRes(dimen.spacing_normal)
            .backgroundContentColorRes(color.darker_gray) // Tooltip background color
            .circleIndicatorBackgroundDrawableRes(drawable.selector_circle)
            .prevString(string.previous)
            .nextString(nextStringText = "Sonraki") // Optional nextStringRes or nextStringText
            .finishString(finishStringText = "Bitir da!")
            .useCircleIndicator(true) 
            .clickable(true) // Click anywhere to continue
            .useArrow(true) // Optional tooltip pointing arrow
            .useSkipWord(false) // Optional tooltip skip option
            .setFragmentManager(this.supportFragmentManager)
            .lineColorRes(color.line_color) // Optional tooltip button seperator line color
            .lineWidthRes(dimen.line_width) // Optional tooltip button seperator line width
            .shouldShowIcons(true) // Optional tooltip next/prev icons
            .setTooltipRadius(dimen.tooltip_radius) //Optional tooltip corder radius
            .showSpotlight(true) //Optional spotlight
            .build()
    }
```

Create a list of TooltipObjects and show tooltip dialog.

```kotlin
fun startUglyTooltips() {
        val tooltips: ArrayList<TooltipObject> = ArrayList()

        tooltips.add(
            TooltipObject(
                findViewById<ImageView>(R.id.iv3),
                null,
                "No title, just description, simple text."
            )
        )

        tooltips.add(
            TooltipObject(
                findViewById<TextView>(R.id.tvTest2),
                null,
                "No title, just description, simple text.",
                tooltipContentPosition = TooltipContentPosition.RIGHT
            )
        )

        tooltips.add(
            TooltipObject(
                findViewById<TextView>(R.id.tvTest3),
                "<font color=\"#FFC300\"> an ImageView </font>",
                "This HTML description point to <font color=\"#FFC300\"> an ImageView </font> as you can see.<br/><br/> Lorem ipsum dolor sit amet, consectetur adipiscing elit.",
                tooltipContentPosition = TooltipContentPosition.LEFT
            )
        )

        tooltips.add(
            TooltipObject(
                findViewById<ImageView>(R.id.iv4),
                "This is a title",
                "This is a description, but a little longer than number 3 but shorter than number 5 that you will see soon."
            )
        )

        tooltips.add(
            TooltipObject(
                findViewById<TextView>(R.id.tvTest),
                "This is a title",
                "This is a description, but a little longer than number 3 but shorter than number 5 that you will see soon."
            )
        )

        tooltips.add(
            TooltipObject(
                findViewById<ImageView>(R.id.iv5),
                "This is another title",
                "This HTML description point to <font color=\"#FFC300\"> an ImageView </font> as you can see.<br/><br/> Lorem ipsum dolor sit amet, consectetur adipiscing elit. Suo enim quisque studio maxime ducitur. Scio enim esse quosdam, qui quavis lingua philosophari possint; Animum autem reliquis rebus ita perfecit, ut corpus; Quo modo autem optimum, si bonum praeterea nullum est?"
            )
        )

        tooltips.add(
            TooltipObject(
                findViewById<ImageView>(R.id.iv6),
                "This is another one",
                "This description point to number 6. <font color=\"#FFC300\"> This is yellow text </font> and this is white.",
                tooltipContentPosition = TooltipContentPosition.UNDEFINED,
                tintBackgroundColor = ResourcesCompat.getColor(resources, R.color.blue, null),
                null
            )
        )

        tooltips.add(
            TooltipObject(
                findViewById<TextView>(R.id.tvTest4),
                "<font color=\"#FFC300\"> an ImageView </font>",
                "This HTML description point to <font color=\"#FFC300\"> an ImageView </font> as you can see.<br/><br/> Lorem ipsum dolor sit amet, consectetur adipiscing elit.",
                tooltipContentPosition = TooltipContentPosition.LEFT
            )
        )

        tooltipDialog?.show(this, supportFragmentManager, "SHOWCASE_TAG", tooltips)

    }
```



