package com.example.mekanat_new.ui.screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mekanat_new.ui.components.MekanatLogoVector
import com.example.mekanat_new.ui.theme.CanvasLight
import com.example.mekanat_new.ui.theme.SignalRed
import com.example.mekanat_new.ui.theme.TextPrimaryLight
import com.example.mekanat_new.ui.theme.TextSecondaryLight
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onSplashFinished: () -> Unit
) {
    var inkProgress by remember { mutableFloatStateOf(0f) }
    var showTextAnim by remember { mutableFloatStateOf(0f) }

    // Sequential ink drawing and entrance animation
    LaunchedEffect(Unit) {
        val steps = 30
        for (i in 1..steps) {
            inkProgress = (i.toFloat() / steps.toFloat())
            delay(35)
        }
        delay(200)

        for (i in 1..20) {
            showTextAnim = (i.toFloat() / 20f)
            delay(25)
        }

        delay(1300)
        onSplashFinished()
    }

    val infiniteTransition = rememberInfiniteTransition(label = "splash_glow")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.98f,
        targetValue = 1.02f,
        animationSpec = infiniteRepeatable(
            animation = tween(1400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(CanvasLight)
            .clickable { onSplashFinished() }
            .testTag("splash_screen"),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(24.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(220.dp)
                    .scale(pulseScale),
                contentAlignment = Alignment.Center
            ) {
                MekanatLogoVector(
                    modifier = Modifier.fillMaxSize(),
                    showText = false,
                    isMonochromeDark = false,
                    animationProgress = inkProgress
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "መካናት",
                style = MaterialTheme.typography.displayMedium.copy(
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    fontSize = 40.sp,
                    color = TextPrimaryLight.copy(alpha = showTextAnim.coerceIn(0f, 1f)),
                    letterSpacing = 2.sp
                ),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "የኢትዮጵያ ኦርቶዶክስ ተዋሕዶ ቅዱሳት መካናትና ንግሥ",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = TextSecondaryLight.copy(alpha = showTextAnim.coerceIn(0f, 1f)),
                    fontSize = 13.5.sp,
                    fontWeight = FontWeight.Medium
                ),
                textAlign = TextAlign.Center
            )
        }
    }
}


