package com.example.cardflipperexample.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.GraphicsLayerScope
import androidx.compose.ui.graphics.graphicsLayer
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin

    enum class FlipOrientation {
        VERTICAL,              // Vertical flip
        HORIZONTAL,            // Horizontal flip
        DIAGONAL_LTR,          // Diagonal flip from top-left to bottom-right
        ROTATE_45,             // 45-degree rotation
        ROTATE_135,            // 135-degree rotation
        ROTATE_90,             // Fixed 90-degree rotation
        ROTATE_360,            // 360-degree rotation
        SPIN_XY,               // Spinning flip around both X and Y axis
        FLIP_Z_AXIS,           // Z-axis flip (3D effect)
        CUSTOM,                // Custom rotation/flip
        ZOOM_IN_OUT,           // Zoom in/out effect
        WOBBLE,                // Wobble effect (falling and jiggling)
        FADE_IN_OUT,           // Fade effect in/out (changes alpha value)
        SHRINK_FLIP,           // Shrink flip (shrinks on flip)
        FLIP_BOUNCE            // Bounce effect while flipping
    }


object CardFlipper {

    @Composable
    fun FlipperCardView(
        modifier: Modifier = Modifier,
        flipCard: Boolean,
        flipOrientation: FlipOrientation = FlipOrientation.HORIZONTAL,
        frontContent: @Composable () -> Unit,
        backContent: @Composable () -> Unit
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = modifier
        ) {
            FlipCardUINew(
                modifier = Modifier.fillMaxWidth(),
                frontContent = { frontContent() },
                backContent = { backContent() },
                flipToBack = flipCard,
                flipOrientation = flipOrientation,
            )
            return

        }
    }


    @Composable
    private fun FlipCardUINew(
        modifier: Modifier = Modifier,
        frontContent: @Composable () -> Unit,
        backContent: @Composable () -> Unit,
        flipToBack: Boolean,
        flipOrientation: FlipOrientation
    ) {
        val rotation by animateFloatAsState(
            targetValue = if (flipToBack) 180f else 0f,
            animationSpec = tween(durationMillis = 700), label = ""
        )

        Box(
            modifier = modifier
                .graphicsLayer {
                    cameraDistance = calculateCameraDistance(size, density)
                }
        ) {
            if (rotation <= 90f) {
                Box(
                    modifier = Modifier.graphicsLayer {
                        applyFlipRotation(flipOrientation, rotation)
                        cameraDistance = calculateCameraDistance(size, density)
                    }
                ) {
                    frontContent()
                }
            }

            if (rotation > 89f) {
                Box(
                    modifier = Modifier.graphicsLayer {
                        applyFlipRotation(flipOrientation, rotation - 180f)
                        cameraDistance = calculateCameraDistance(size, density)

                    }
                ) {
                    backContent()
                }
            }
        }
    }


    private fun GraphicsLayerScope.applyFlipRotation(
        orientation: FlipOrientation,
        rotation: Float
    ) {
        when (orientation) {
            FlipOrientation.HORIZONTAL -> rotationY = rotation
            FlipOrientation.VERTICAL -> rotationX = rotation
            FlipOrientation.DIAGONAL_LTR -> {
                rotationX = rotation / 2
                rotationY = rotation / 2
            }

            FlipOrientation.ROTATE_45 -> rotationZ = rotation * 0.25f
            FlipOrientation.ROTATE_135 -> rotationZ = rotation * 0.75f
            FlipOrientation.ROTATE_90 -> rotationZ = rotation * 0.5f
            FlipOrientation.ROTATE_360 -> rotationZ = rotation
            FlipOrientation.SPIN_XY -> {
                rotationX = rotation
                rotationY = rotation
            }

            FlipOrientation.FLIP_Z_AXIS -> rotationZ = rotation
            FlipOrientation.CUSTOM -> {
                rotationX = rotation * 0.3f
                rotationY = rotation * 0.4f
                rotationZ = rotation * 0.2f
            }

            // Wobble effect (fall from top and jiggle)
            FlipOrientation.WOBBLE -> {
                // Drop from top (by using translationY) and apply a wobble effect on scaling
                scaleX = 1f + sin(rotation / 10f) * 0.05f  // Horizontal wobble
            }


            // Zoom in/out effect (smooth zoom in and out based on rotation)
            FlipOrientation.ZOOM_IN_OUT -> {
                rotationY = rotation
                scaleX = 1f + abs(rotation / 180f) * 0.3f
                scaleY = 1f + abs(rotation / 180f) * 0.3f
            }

            // Fade in/out effect (alpha changes with rotation)
            FlipOrientation.FADE_IN_OUT -> {
                alpha = if (rotation <= 90f) 1f else 0.2f // Fade out after 90 degrees
            }

            // Shrink flip effect (shrinks the card during flip)
            FlipOrientation.SHRINK_FLIP -> {
                rotationY = rotation
                scaleX = 1f - abs(rotation / 180f)
                scaleY = 1f - abs(rotation / 180f)
            }

            // Flip bounce effect (bouncy flip animation)
            FlipOrientation.FLIP_BOUNCE -> {
                rotationY = rotation
                scaleX = 1f + sin(rotation / 10f) * 0.2f  // Horizontal wobble

            }
        }
    }

    // Function to calculate the camera distance based on content size and density
    private fun calculateCameraDistance(size: Size, density: Float): Float {
        // Use the width or height of the content for the camera distance
        val maxDimension = maxOf(size.width, size.height)
        val cameraDistance = (maxDimension * density)

        return cameraDistance / 18
    }

}

