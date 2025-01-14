package com.example.cardflipperexample.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.MarqueeAnimationMode
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cardflipperexample.R

object UiHelper {

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    fun HeadingText(
        text: String, modifier: Modifier = Modifier,
        fontColor: Int = R.color.black,
        textSize: TextUnit = 12.sp,
        textAlign: TextAlign = TextAlign.Start,
        overflow: TextOverflow = TextOverflow.Ellipsis,
        maxLines: Int = 0,
        lineHeight: TextUnit = 18.sp
    ) {
        Text(
            text = text,
            modifier = if (maxLines == 0) {
                modifier
            } else {
                modifier.basicMarquee(
                    iterations = Int.MAX_VALUE,
                    animationMode = MarqueeAnimationMode.Immediately
                )
            },
            style = TextStyle(
                lineHeight = lineHeight * (textSize.value.toInt() / 12),
                fontWeight = FontWeight.Bold
            ),
            fontSize = textSize,
            fontWeight = FontWeight.Bold,
            color = colorResource(fontColor),
            textAlign = textAlign,
            overflow = overflow,
            maxLines = maxLines.takeIf { it > 0 } ?: Int.MAX_VALUE
        )
    }

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    fun BodyText(
        text: String,
        modifier: Modifier = Modifier,
        fontColor: Int = R.color.black,
        textSize: TextUnit = 10.sp,
        fontFamily: FontFamily=FontFamily.Monospace,
        textAlign: TextAlign = TextAlign.Start,
        overflow: TextOverflow = TextOverflow.Ellipsis,
        maxLines: Int = Int.MAX_VALUE,
        lineHeight: TextUnit = 5.sp
    ) {
        val lH = ((lineHeight.value * 2.7) * (textSize.value / 12)).sp
        Text(
            text = text,
            modifier = modifier,
            fontSize = textSize,
            style = TextStyle(
                lineHeight = lH,
                fontWeight = FontWeight.Normal,
                fontFamily = fontFamily
            ),
            color = colorResource(fontColor),
            textAlign = textAlign,
            overflow = if (maxLines == 1) overflow else TextOverflow.Clip,
            maxLines = maxLines
        )
    }


    private var buttonCorners = 10.dp

    @Composable
    fun ThemeButton(
        rippleColor: Int = R.color.white,
        modifier: Modifier = Modifier,
        isEnabled: MutableState<Boolean> = mutableStateOf(true),
        disableColor: Int = android.R.color.darker_gray,
        cornerRadius: Dp = buttonCorners.value.toInt().dp,
        bodyText: @Composable () -> Unit,
        onClick: () -> Unit
    ) {
        buttonCorners = cornerRadius
        if (isEnabled.value) {
            Box(
                modifier = modifier
                    .background(
                        colorResource(R.color.accentColor),
                        RoundedCornerShape(buttonCorners)
                    )
                    .roundedRippleClick(rippleColor = colorResource(rippleColor)) { onClick.invoke() },
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    bodyText() // Invoke the composable body text content
                }
            }
        } else {
            Box(
                modifier = modifier
                    .background(
                        colorResource(disableColor),
                        RoundedCornerShape(buttonCorners)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    bodyText()
                }
            }
        }
    }

    @Composable
    fun Modifier.roundedRippleClick(
        cornerRadius: Dp = buttonCorners,
        rippleColor: Color = colorResource(R.color.accentColor),
        onClick: () -> Unit
    ): Modifier = composed {
        val interactionSource = remember { MutableInteractionSource() }
        val indication = rippleColor.takeUnless { it == Color.Unspecified }
            ?.let { rememberRipple(color = it) }
        val keyboardController = LocalSoftwareKeyboardController.current
        this
            .clip(RoundedCornerShape(cornerRadius))
            .indication(interactionSource, indication)
            .clickable(interactionSource = interactionSource, indication = null) {
                onClick()
                keyboardController?.hide()
            }
    }
}