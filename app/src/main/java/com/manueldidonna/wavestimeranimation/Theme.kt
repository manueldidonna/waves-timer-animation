/*
 * Copyright (C) 2021 Manuel Di Donna
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  he Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.manueldidonna.wavestimeranimation

import androidx.compose.animation.core.Spring
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Shapes
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.systemuicontroller.rememberSystemUiController

internal const val SpringDefaultDampingRatio = Spring.DampingRatioMediumBouncy
internal const val SpringDefaultStiffness = Spring.StiffnessLow

@Composable
fun JetpackComposeTemplateTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colors = if (darkTheme) DarkColorPalette else LightColorPalette,
        shapes = Shapes,
        content = content
    )
}

@Composable
fun EdgeToEdgeContent(content: @Composable () -> Unit) {
    val isLightTheme = MaterialTheme.colors.isLight
    val controller = rememberSystemUiController()
    SideEffect {
        controller.setSystemBarsColor(color = Color.Transparent, darkIcons = isLightTheme)
    }
    ProvideWindowInsets(content = content)
}

private val AndroidBlueColor = Color(0xff9bbed3)
private val AndroidBlueDarkerColor = Color(0xff073042)

private val DarkColorPalette = darkColors(
    primary = AndroidBlueColor,
    primaryVariant = AndroidBlueColor,
    secondary = Color(0xff86e6a9),
    surface = AndroidBlueColor.copy(alpha = 0.08f).compositeOver(Color(0xff121212)),
    background = AndroidBlueColor.copy(alpha = 0.08f).compositeOver(Color(0xff121212))
)

private val LightColorPalette = lightColors(
    primary = AndroidBlueDarkerColor,
    primaryVariant = AndroidBlueDarkerColor,
    secondary = Color(0xff3ddc84)
)

private val Shapes = Shapes(
    small = RoundedCornerShape(16.dp),
    medium = RoundedCornerShape(24.dp),
    large = RoundedCornerShape(24.dp)
)
