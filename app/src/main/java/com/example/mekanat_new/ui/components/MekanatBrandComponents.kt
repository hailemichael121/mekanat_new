package com.example.mekanat_new.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp

import com.example.mekanat_new.ui.theme.BrandEmber
import com.example.mekanat_new.ui.theme.BrandEmberInk
import com.example.mekanat_new.ui.theme.BrandEmberInkDark
import com.example.mekanat_new.ui.theme.CrimsonPulse
import com.example.mekanat_new.ui.theme.GoldFlame
import com.example.mekanat_new.ui.theme.GoldFlameInk
import com.example.mekanat_new.ui.theme.MekanatDataTypography
import com.example.mekanat_new.ui.theme.WayfindingTeal

/**
 * Mekanät Brand Design System Component Suite
 * Follows exact specifications in /BRANDING.md
 */

/**
 * 04 — Primary Button (Ember #FF5A1F, white text, 12dp radius)
 */
@Composable
fun MekanatPrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
    enabled: Boolean = true,
    testTag: String = "btn_primary"
) {
    val haptic = LocalHapticFeedback.current
    Button(
        onClick = {
            if (enabled) {
                haptic.vibrateClick()
                onClick()
            }
        },
        enabled = enabled,
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = BrandEmber,
            contentColor = Color.White,
            disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
        ),
        modifier = modifier
            .height(48.dp)
            .testTag(testTag)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if (leadingIcon != null) {
                leadingIcon()
                Spacer(modifier = Modifier.width(8.dp))
            }
            Text(
                text = text,
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp
                )
            )
            if (trailingIcon != null) {
                Spacer(modifier = Modifier.width(8.dp))
                trailingIcon()
            }
        }
    }
}

/**
 * 04 — Route Button (Wayfinding Teal #0FB2A0, white text, 12dp radius with MekanatIconRoute)
 */
@Composable
fun MekanatRouteButton(
    text: String = "Start Route",
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    testTag: String = "btn_route"
) {
    val haptic = LocalHapticFeedback.current
    Button(
        onClick = {
            if (enabled) {
                haptic.vibrateClick()
                onClick()
            }
        },
        enabled = enabled,
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = WayfindingTeal,
            contentColor = Color.White,
            disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
        ),
        modifier = modifier
            .height(48.dp)
            .testTag(testTag)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            MekanatIconRoute(
                size = 18.dp,
                tint = Color.White
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = text.removePrefix("▲ ").trim(),
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            )
        }
    }
}

/**
 * 04 — Secondary Button (Transparent, 1.5dp border, text color)
 */
@Composable
fun MekanatSecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    leadingIcon: (@Composable () -> Unit)? = null,
    testTag: String = "btn_secondary"
) {
    val haptic = LocalHapticFeedback.current
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = Color.Transparent,
        border = androidx.compose.foundation.BorderStroke(1.5.dp, MaterialTheme.colorScheme.outline),
        modifier = modifier
            .height(48.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable {
                haptic.vibrateClick()
                onClick()
            }
            .testTag(testTag)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.padding(horizontal = 20.dp)
        ) {
            if (leadingIcon != null) {
                leadingIcon()
                Spacer(modifier = Modifier.width(8.dp))
            }
            Text(
                text = text,
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    }
}

/**
 * 04 — Ghost Button (Sunken bg-sunk background, text color)
 */
@Composable
fun MekanatGhostButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    leadingIcon: (@Composable () -> Unit)? = null,
    testTag: String = "btn_ghost"
) {
    val haptic = LocalHapticFeedback.current
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
        modifier = modifier
            .height(44.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable {
                haptic.vibrateClick()
                onClick()
            }
            .testTag(testTag)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            if (leadingIcon != null) {
                leadingIcon()
                Spacer(modifier = Modifier.width(8.dp))
            }
            Text(
                text = text,
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.5.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    }
}

/**
 * 04 — Danger Outline Button (Transparent, 1.5dp Crimson border, Crimson text)
 */
@Composable
fun MekanatDangerOutlineButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    testTag: String = "btn_danger_outline"
) {
    val haptic = LocalHapticFeedback.current
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = Color.Transparent,
        border = androidx.compose.foundation.BorderStroke(1.5.dp, CrimsonPulse),
        modifier = modifier
            .height(44.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable {
                haptic.vibrateClick()
                onClick()
            }
            .testTag(testTag)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.sp,
                    color = CrimsonPulse
                )
            )
        }
    }
}

/**
 * 04 — Circular Icon Button (44x44dp, 1.5dp border, Gold Flame when filled/saved)
 */
@Composable
fun MekanatIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isFilledGold: Boolean = false,
    contentDescription: String? = null,
    testTag: String = "icon_btn",
    icon: @Composable (tint: Color) -> Unit
) {
    val haptic = LocalHapticFeedback.current
    val bgColor = if (isFilledGold) GoldFlame else MaterialTheme.colorScheme.surface
    val borderColor = if (isFilledGold) GoldFlame else MaterialTheme.colorScheme.outline
    val iconTint = if (isFilledGold) GoldFlameInk else MaterialTheme.colorScheme.onSurface

    Surface(
        shape = CircleShape,
        color = bgColor,
        border = androidx.compose.foundation.BorderStroke(1.5.dp, borderColor),
        modifier = modifier
            .size(44.dp)
            .clip(CircleShape)
            .clickable {
                haptic.vibrateClick()
                onClick()
            }
            .testTag(testTag)
    ) {
        Box(
            modifier = Modifier.size(44.dp),
            contentAlignment = Alignment.Center
        ) {
            icon(iconTint)
        }
    }
}

/**
 * 05 — Distance Pill (IBM Plex Mono style Monospace, bg-sunk, 999dp pill)
 */
@Composable
fun MekanatDistancePill(
    distanceText: String,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(999.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
        modifier = modifier
    ) {
        Text(
            text = distanceText,
            style = MekanatDataTypography.distancePill.copy(
                color = MaterialTheme.colorScheme.onSurface
            ),
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
        )
    }
}

/**
 * 05 — Standard Tag / Category Pill
 */
@Composable
fun MekanatTag(
    text: String,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(999.dp),
        color = Color.Transparent,
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        modifier = modifier
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall.copy(
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 11.sp
            ),
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
        )
    }
}

/**
 * 05 — Nigs Feast Tag (Border Ember #FF5A1F, bg 12% alpha, text Ember Ink)
 */
@Composable
fun MekanatNigsTag(
    text: String,
    modifier: Modifier = Modifier
) {
    val isDark = MaterialTheme.colorScheme.background.value == 0xFF0E0D0C.toULong()
    val textColor = if (isDark) BrandEmberInkDark else BrandEmberInk

    Surface(
        shape = RoundedCornerShape(999.dp),
        color = BrandEmber.copy(alpha = 0.12f),
        border = androidx.compose.foundation.BorderStroke(1.dp, BrandEmber),
        modifier = modifier
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall.copy(
                color = textColor,
                fontWeight = FontWeight.SemiBold,
                fontSize = 11.sp
            ),
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
        )
    }
}

/**
 * 05 — Live Gubae Banner (Crimson 14% alpha bg, 1dp Crimson border, Crimson text, animated pulse dot)
 */
@Composable
fun MekanatLiveBanner(
    text: String = "Currently happening — Annual Gubae",
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "livePulse")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "livePulseAlpha"
    )

    Surface(
        shape = RoundedCornerShape(10.dp),
        color = CrimsonPulse.copy(alpha = 0.14f),
        border = androidx.compose.foundation.BorderStroke(1.dp, CrimsonPulse),
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 9.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(CrimsonPulse.copy(alpha = pulseAlpha))
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = text,
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 12.sp,
                    color = CrimsonPulse
                )
            )
        }
    }
}

/**
 * 05 — Church Card (Flat, bg-elev, 1dp border, 14dp radius, subtle shadow)
 */
@Composable
fun MekanatChurchCardContainer(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    testTag: String = "church_card",
    content: @Composable () -> Unit
) {
    val haptic = LocalHapticFeedback.current
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .clickable {
                haptic.vibrateClick()
                onClick()
            }
            .testTag(testTag)
    ) {
        Box(modifier = Modifier.padding(18.dp)) {
            content()
        }
    }
}

/**
 * 08 — Applied: Search Bar Mockup
 * bg-elev, 1.5dp border, 16dp radius, soft shadow, search icon, input field, and filter icon button.
 */
@Composable
fun MekanatSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onFilterClick: () -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "Search church, Tabot, or Nigs…",
    onSearch: (() -> Unit)? = null
) {
    val haptic = LocalHapticFeedback.current
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface,
        border = androidx.compose.foundation.BorderStroke(1.5.dp, MaterialTheme.colorScheme.outline),
        shadowElevation = 3.dp,
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp)
            .testTag("brand_search_bar")
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            MekanatIconSearch(
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                size = 19.dp
            )
            Spacer(modifier = Modifier.width(10.dp))
            androidx.compose.foundation.text.BasicTextField(
                value = query,
                onValueChange = onQueryChange,
                singleLine = true,
                textStyle = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface
                ),
                modifier = Modifier.weight(1f),
                decorationBox = { innerTextField ->
                    if (query.isEmpty()) {
                        Text(
                            text = placeholder,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        )
                    }
                    innerTextField()
                }
            )
            Spacer(modifier = Modifier.width(6.dp))
            Surface(
                shape = CircleShape,
                color = MaterialTheme.colorScheme.surfaceVariant,
                modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .clickable {
                        haptic.vibrateClick()
                        onFilterClick()
                    }
                    .testTag("search_filter_btn")
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    MekanatIconFilter(
                        tint = MaterialTheme.colorScheme.onSurface,
                        size = 16.dp
                    )
                }
            }
        }
    }
}

/**
 * 08 — Applied: Page Header Mockup
 * Standard page header with Back / Title / Share
 */
@Composable
fun MekanatPageHeader(
    title: String,
    onBack: () -> Unit,
    onShare: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
    trailingAction: (@Composable () -> Unit)? = null
) {
    val haptic = LocalHapticFeedback.current
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = MaterialTheme.colorScheme.surface,
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp)
            .testTag("brand_page_header")
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .clickable {
                        haptic.vibrateClick()
                        onBack()
                    },
                contentAlignment = Alignment.Center
            ) {
                MekanatIconBack(
                    tint = MaterialTheme.colorScheme.onSurface,
                    size = 20.dp
                )
            }

            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    letterSpacing = (-0.01).em,
                    color = MaterialTheme.colorScheme.onSurface
                ),
                maxLines = 1
            )

            Box(
                modifier = Modifier.size(32.dp),
                contentAlignment = Alignment.Center
            ) {
                if (trailingAction != null) {
                    trailingAction()
                } else if (onShare != null) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape)
                            .clickable {
                                haptic.vibrateClick()
                                onShare()
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        MekanatIconShare(
                            tint = MaterialTheme.colorScheme.onSurface,
                            size = 18.dp
                        )
                    }
                } else {
                    Spacer(modifier = Modifier.size(20.dp))
                }
            }
        }
    }
}

