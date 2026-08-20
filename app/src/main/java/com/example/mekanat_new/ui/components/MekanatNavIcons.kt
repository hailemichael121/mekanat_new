package com.example.mekanat_new.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mekanat_new.ui.theme.SignalRed

/**
 * Convenient helper for subtle tactile haptic feedback throughout Mekanat.
 */
fun HapticFeedback.vibrateSubtle() {
    this.performHapticFeedback(HapticFeedbackType.TextHandleMove)
}

fun HapticFeedback.vibrateClick() {
    this.performHapticFeedback(HapticFeedbackType.LongPress)
}

/**
 * Custom SVG Vector: Orthodox Sanctuary & Monolithic Dome Navigation Icon
 */
@Composable
fun NavSanctuaryIcon(
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    size: Dp = 24.dp,
    tint: Color = MaterialTheme.colorScheme.primary
) {
    Canvas(modifier = modifier.size(size)) {
        val w = this.size.width
        val h = this.size.height
        val strokeW = if (isSelected) 2.2f else 1.8f

        // Top Ge'ez Cross on apex
        drawLine(
            color = tint,
            start = Offset(w * 0.5f, h * 0.08f),
            end = Offset(w * 0.5f, h * 0.28f),
            strokeWidth = strokeW,
            cap = StrokeCap.Round
        )
        drawLine(
            color = tint,
            start = Offset(w * 0.38f, h * 0.16f),
            end = Offset(w * 0.62f, h * 0.16f),
            strokeWidth = strokeW,
            cap = StrokeCap.Round
        )

        // Sanctuary Main Dome Roof (Curved arch)
        val roofPath = Path().apply {
            moveTo(w * 0.16f, h * 0.58f)
            cubicTo(
                w * 0.22f, h * 0.28f,
                w * 0.78f, h * 0.28f,
                w * 0.84f, h * 0.58f
            )
            close()
        }
        if (isSelected) {
            drawPath(path = roofPath, color = tint.copy(alpha = 0.22f), style = Fill)
        }
        drawPath(path = roofPath, color = tint, style = Stroke(width = strokeW, join = StrokeJoin.Round))

        // Lower Sanctuary Wall Base
        val baseLeft = w * 0.2f
        val baseRight = w * 0.8f
        val baseTop = h * 0.58f
        val baseBottom = h * 0.90f

        drawRoundRect(
            color = tint,
            topLeft = Offset(baseLeft, baseTop),
            size = Size(baseRight - baseLeft, baseBottom - baseTop),
            cornerRadius = CornerRadius(3f, 3f),
            style = Stroke(width = strokeW)
        )

        // Center Archway Portal (Monastery Door)
        val doorPath = Path().apply {
            moveTo(w * 0.40f, baseBottom)
            lineTo(w * 0.40f, h * 0.72f)
            cubicTo(
                w * 0.40f, h * 0.64f,
                w * 0.60f, h * 0.64f,
                w * 0.60f, h * 0.72f
            )
            lineTo(w * 0.60f, baseBottom)
        }
        if (isSelected) {
            drawPath(path = doorPath, color = tint, style = Fill)
        } else {
            drawPath(path = doorPath, color = tint, style = Stroke(width = strokeW))
        }
    }
}

/**
 * Custom SVG Vector: Sacred Tabot Ark & Bookmark Ribbon Navigation Icon
 */
@Composable
fun NavSavedIcon(
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    size: Dp = 24.dp,
    tint: Color = MaterialTheme.colorScheme.primary
) {
    Canvas(modifier = modifier.size(size)) {
        val w = this.size.width
        val h = this.size.height
        val strokeW = if (isSelected) 2.2f else 1.8f

        // Ribbon Body
        val ribbonPath = Path().apply {
            moveTo(w * 0.24f, h * 0.12f)
            lineTo(w * 0.76f, h * 0.12f)
            lineTo(w * 0.76f, h * 0.88f)
            lineTo(w * 0.50f, h * 0.70f)
            lineTo(w * 0.24f, h * 0.88f)
            close()
        }

        if (isSelected) {
            drawPath(path = ribbonPath, color = tint.copy(alpha = 0.25f), style = Fill)
            drawPath(path = ribbonPath, color = tint, style = Stroke(width = strokeW, join = StrokeJoin.Round))
        } else {
            drawPath(path = ribbonPath, color = tint, style = Stroke(width = strokeW, join = StrokeJoin.Round))
        }

        // Inner Holy Cross
        drawLine(
            color = tint,
            start = Offset(w * 0.50f, h * 0.26f),
            end = Offset(w * 0.50f, h * 0.56f),
            strokeWidth = strokeW,
            cap = StrokeCap.Round
        )
        drawLine(
            color = tint,
            start = Offset(w * 0.36f, h * 0.38f),
            end = Offset(w * 0.64f, h * 0.38f),
            strokeWidth = strokeW,
            cap = StrokeCap.Round
        )
    }
}

/**
 * Custom SVG Vector: Ethiopian Orthodox Liturgical Calendar Navigation Icon
 */
@Composable
fun NavCalendarIcon(
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    size: Dp = 24.dp,
    tint: Color = MaterialTheme.colorScheme.primary
) {
    Canvas(modifier = modifier.size(size)) {
        val w = this.size.width
        val h = this.size.height
        val strokeW = if (isSelected) 2.2f else 1.8f

        // Calendar Outer Body
        val calLeft = w * 0.18f
        val calRight = w * 0.82f
        val calTop = h * 0.20f
        val calBottom = h * 0.88f

        drawRoundRect(
            color = tint,
            topLeft = Offset(calLeft, calTop),
            size = Size(calRight - calLeft, calBottom - calTop),
            cornerRadius = CornerRadius(6f, 6f),
            style = Stroke(width = strokeW)
        )

        if (isSelected) {
            // Fill header section
            drawRoundRect(
                color = tint.copy(alpha = 0.28f),
                topLeft = Offset(calLeft, calTop),
                size = Size(calRight - calLeft, h * 0.20f),
                cornerRadius = CornerRadius(6f, 6f),
                style = Fill
            )
        }

        // Top Binder Rings (Parchment bindings)
        drawLine(
            color = tint,
            start = Offset(w * 0.35f, h * 0.10f),
            end = Offset(w * 0.35f, h * 0.24f),
            strokeWidth = strokeW * 1.2f,
            cap = StrokeCap.Round
        )
        drawLine(
            color = tint,
            start = Offset(w * 0.65f, h * 0.10f),
            end = Offset(w * 0.65f, h * 0.24f),
            strokeWidth = strokeW * 1.2f,
            cap = StrokeCap.Round
        )

        // Separator line
        drawLine(
            color = tint,
            start = Offset(calLeft, h * 0.40f),
            end = Offset(calRight, h * 0.40f),
            strokeWidth = strokeW
        )

        // Calendar Ge'ez Cross in center
        val crossCenterY = h * 0.64f
        drawLine(
            color = tint,
            start = Offset(w * 0.50f, crossCenterY - h * 0.12f),
            end = Offset(w * 0.50f, crossCenterY + h * 0.12f),
            strokeWidth = strokeW,
            cap = StrokeCap.Round
        )
        drawLine(
            color = tint,
            start = Offset(w * 0.38f, crossCenterY - h * 0.03f),
            end = Offset(w * 0.62f, crossCenterY - h * 0.03f),
            strokeWidth = strokeW,
            cap = StrokeCap.Round
        )
    }
}

/**
 * Custom SVG Vector: Pilgrim Staff & Prayer Aura Profile Navigation Icon
 */
@Composable
fun NavProfileIcon(
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    size: Dp = 24.dp,
    tint: Color = MaterialTheme.colorScheme.primary
) {
    Canvas(modifier = modifier.size(size)) {
        val w = this.size.width
        val h = this.size.height
        val strokeW = if (isSelected) 2.2f else 1.8f

        // Head / Aura
        drawCircle(
            color = tint,
            radius = w * 0.18f,
            center = Offset(w * 0.44f, h * 0.28f),
            style = if (isSelected) Fill else Stroke(width = strokeW)
        )

        // Pilgrim Cloak / Shoulders
        val cloakPath = Path().apply {
            moveTo(w * 0.18f, h * 0.86f)
            cubicTo(
                w * 0.20f, h * 0.52f,
                w * 0.68f, h * 0.52f,
                w * 0.70f, h * 0.86f
            )
        }
        drawPath(path = cloakPath, color = tint, style = Stroke(width = strokeW, cap = StrokeCap.Round))

        if (isSelected) {
            val filledCloak = Path().apply {
                moveTo(w * 0.18f, h * 0.86f)
                cubicTo(
                    w * 0.20f, h * 0.52f,
                    w * 0.68f, h * 0.52f,
                    w * 0.70f, h * 0.86f
                )
                close()
            }
            drawPath(path = filledCloak, color = tint.copy(alpha = 0.22f), style = Fill)
        }

        // Pilgrim Walking Staff with T-Cross (Maqomiya)
        val staffX = w * 0.82f
        drawLine(
            color = tint,
            start = Offset(staffX, h * 0.15f),
            end = Offset(staffX, h * 0.90f),
            strokeWidth = strokeW,
            cap = StrokeCap.Round
        )
        // Staff T-top
        drawLine(
            color = tint,
            start = Offset(staffX - w * 0.08f, h * 0.15f),
            end = Offset(staffX + w * 0.08f, h * 0.15f),
            strokeWidth = strokeW,
            cap = StrokeCap.Round
        )
    }
}

private data class NavTabItem(
    val index: Int,
    val label: String,
    val testTag: String,
    val icon: @Composable (isSelected: Boolean, tint: Color) -> Unit
)

/**
 * Minimalist, borderless Bottom Navigation Bar with subtle ambient shadow,
 * custom SVG vector icons, and smooth haptic feedback.
 */
@Composable
fun MekanatBottomBar(
    currentTab: Int,
    onTabSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val haptic = LocalHapticFeedback.current

    val tabs = listOf(
        NavTabItem(
            index = 0,
            label = "Sanctuaries",
            testTag = "nav_tab_map",
            icon = { isSelected, tint ->
                NavSanctuaryIcon(isSelected = isSelected, tint = tint, size = 24.dp)
            }
        ),
        NavTabItem(
            index = 1,
            label = "Saved",
            testTag = "nav_tab_bookmarks",
            icon = { isSelected, tint ->
                NavSavedIcon(isSelected = isSelected, tint = tint, size = 24.dp)
            }
        ),
        NavTabItem(
            index = 2,
            label = "Calendar",
            testTag = "nav_tab_calendar",
            icon = { isSelected, tint ->
                NavCalendarIcon(isSelected = isSelected, tint = tint, size = 24.dp)
            }
        ),
        NavTabItem(
            index = 3,
            label = "Pilgrim",
            testTag = "nav_tab_profile",
            icon = { isSelected, tint ->
                NavProfileIcon(isSelected = isSelected, tint = tint, size = 24.dp)
            }
        )
    )

    Surface(
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 0.dp,
        shadowElevation = 8.dp, // Clean, subtle ambient shadow
        shape = RectangleShape,
        // Completely borderless - no BorderStroke
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .testTag("bottom_navigation_bar")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            tabs.forEach { tab ->
                val isSelected = currentTab == tab.index
                val animatedTint by animateColorAsState(
                    targetValue = if (isSelected) SignalRed else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                    animationSpec = tween(durationMillis = 180),
                    label = "navItemTint"
                )

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {
                            haptic.vibrateClick()
                            onTabSelected(tab.index)
                        }
                        .testTag(tab.testTag),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        tab.icon(isSelected, animatedTint)

                        Spacer(modifier = Modifier.height(4.dp))

                        // Clean micro-indicator pill below the icon
                        Box(
                            modifier = Modifier
                                .size(width = 14.dp, height = 2.5.dp)
                                .background(
                                    color = if (isSelected) SignalRed else Color.Transparent,
                                    shape = RoundedCornerShape(2.dp)
                                )
                        )
                    }
                }
            }
        }
    }
}

