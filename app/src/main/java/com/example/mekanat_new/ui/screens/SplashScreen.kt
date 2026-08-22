package com.example.mekanat_new.ui.screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.mekanat_new.ui.components.MekanatLogoVector
import com.example.mekanat_new.ui.theme.DarkBg
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onSplashFinished: () -> Unit
) {
    var targetInkProgress by remember { mutableFloatStateOf(0f) }

    val animatedInkProgress by animateFloatAsState(
        targetValue = targetInkProgress,
        animationSpec = tween(durationMillis = 1800, easing = FastOutSlowInEasing),
        label = "ink_drawing_anim"
    )

    // Trigger stroke progression sequence with pure clean logo
    LaunchedEffect(Unit) {
        delay(120)
        targetInkProgress = 1f
        delay(2200)
        onSplashFinished()
    }

    val infiniteTransition = rememberInfiniteTransition(label = "splash_pulse")
    val gentleScale by infiniteTransition.animateFloat(
        initialValue = 0.985f,
        targetValue = 1.015f,
        animationSpec = infiniteRepeatable(
            animation = tween(1600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "gentle_scale"
    )

    val isDark = MaterialTheme.colorScheme.background == DarkBg

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(if (isDark) DarkBg else Color.White)
            .clickable { onSplashFinished() }
            .testTag("splash_screen"),
        contentAlignment = Alignment.Center
    ) {
        // Pure clean SVG logo emblem — strictly 1:1 aspect ratio, no distortion, no text
        Box(
            modifier = Modifier
                .size(240.dp)
                .aspectRatio(1f)
                .scale(gentleScale)
                .padding(12.dp),
            contentAlignment = Alignment.Center
        ) {
            MekanatLogoVector(
                modifier = Modifier.fillMaxSize(),
                showText = false,
                isMonochromeDark = isDark,
                animationProgress = animatedInkProgress
            )
        }
    }
}




