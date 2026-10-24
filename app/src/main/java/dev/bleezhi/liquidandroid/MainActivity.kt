package dev.bleezhi.liquidandroid

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = android.graphics.Color.TRANSPARENT
        window.navigationBarColor = android.graphics.Color.TRANSPARENT
        window.decorView.systemUiVisibility = (android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
            android.view.View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
            android.view.View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION)
        setContent { MaterialTheme(colorScheme = darkColorScheme()) { LiquidGlassLab() } }
    }
}

@Composable
private fun LiquidGlassLab() {
    var refraction by remember { mutableFloatStateOf(0.62f) }
    var blur by remember { mutableFloatStateOf(0.24f) }
    var dispersion by remember { mutableFloatStateOf(0.18f) }
    var tint by remember { mutableFloatStateOf(0.12f) }
    var rim by remember { mutableFloatStateOf(0.72f) }
    var material by remember { mutableStateOf("Regular") }
    var showGrid by remember { mutableStateOf(true) }
    var showControls by remember { mutableStateOf(true) }

    Box(Modifier.fillMaxSize().background(Color(0xFF070B18))) {
        SceneBackdrop(showGrid, Modifier.fillMaxSize())
        Column(
            Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(horizontal = 22.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(54.dp))
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
                    Text("LIQUID / LAB", color = Color(0xFF8DAEFF), fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 3.sp)
                    Text("Liquid Glass", color = Color.White, fontSize = 32.sp, fontWeight = FontWeight.SemiBold)
                    Text("Optics playground", color = Color.White.copy(alpha = .58f), fontSize = 14.sp)
                }
                Surface(color = Color.White.copy(alpha = .09f), shape = CircleShape, border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = .18f))) {
                    Text(if (Build.VERSION.SDK_INT >= 33) "AGSL READY" else "CLASSIC", Modifier.padding(horizontal = 12.dp, vertical = 8.dp), color = Color(0xFFB9D2FF), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
            }
            Spacer(Modifier.height(28.dp))
            GlassPreview(refraction, blur, dispersion, tint, rim, material == "Clear", Modifier.fillMaxWidth().height(310.dp))
            Spacer(Modifier.height(14.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("Regular", "Clear", "Tinted").forEach { option ->
                    val selected = material == option
                    Surface(onClick = { material = option }, shape = RoundedCornerShape(50), color = if (selected) Color(0xFFDAE6FF) else Color.White.copy(alpha = .08f), border = androidx.compose.foundation.BorderStroke(1.dp, if (selected) Color.Transparent else Color.White.copy(alpha = .14f))) {
                        Text(option, Modifier.padding(horizontal = 18.dp, vertical = 10.dp), color = if (selected) Color(0xFF101A32) else Color.White.copy(alpha = .85f), fontSize = 13.sp, fontWeight = FontWeight.Medium)
                    }
                }
            }
            Spacer(Modifier.height(20.dp))
            if (showControls) {
                Surface(color = Color(0xC9161D30), shape = RoundedCornerShape(28.dp), border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = .14f))) {
                    Column(Modifier.fillMaxWidth().padding(20.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("Material properties", color = Color.White, fontSize = 17.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(1f))
                            Text("LIVE", color = Color(0xFF8CF0D0), fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 2.sp)
                        }
                        Spacer(Modifier.height(14.dp))
                        TuningSlider("Refraction", refraction, { refraction = it }, "Lens displacement")
                        TuningSlider("Backdrop blur", blur, { blur = it }, "Frosted transmission")
                        TuningSlider("Chromatic dispersion", dispersion, { dispersion = it }, "Spectral edge separation")
                        TuningSlider("Glass tint", tint, { tint = it }, "Material coloration")
                        TuningSlider("Rim lighting", rim, { rim = it }, "Specular edge intensity")
                        HorizontalDivider(color = Color.White.copy(alpha = .12f), modifier = Modifier.padding(vertical = 8.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Column(Modifier.weight(1f)) {
                                Text("Backdrop detail", color = Color.White, fontSize = 14.sp)
                                Text("High-frequency pattern makes lensing visible", color = Color.White.copy(alpha = .5f), fontSize = 11.sp)
                            }
                            Switch(checked = showGrid, onCheckedChange = { showGrid = it })
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Column(Modifier.weight(1f)) {
                                Text("Parameter panel", color = Color.White, fontSize = 14.sp)
                                Text("Hide controls for a clean preview", color = Color.White.copy(alpha = .5f), fontSize = 11.sp)
                            }
                            Switch(checked = showControls, onCheckedChange = { showControls = it })
                        }
                        Text("PREVIEW MODEL · NOT A DEVICE-WIDE SYSTEM MATERIAL", color = Color.White.copy(alpha = .34f), fontSize = 9.sp, letterSpacing = 1.sp, modifier = Modifier.padding(top = 8.dp))
                    }
                }
            } else {
                OutlinedButton(onClick = { showControls = true }) { Text("Show controls") }
            }
            Spacer(Modifier.height(28.dp))
        }
    }
}

@Composable
private fun TuningSlider(title: String, value: Float, onChange: (Float) -> Unit, detail: String) {
    Column(Modifier.padding(bottom = 8.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(title, color = Color.White, fontSize = 13.sp, modifier = Modifier.weight(1f))
            Text("%02d".format((value * 100).roundToInt()), color = Color(0xFFAFC6FF), fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
        }
        Slider(value = value, onValueChange = onChange, valueRange = 0f..1f, modifier = Modifier.height(28.dp), colors = SliderDefaults.colors(thumbColor = Color(0xFFE7EEFF), activeTrackColor = Color(0xFF9CB9FF), inactiveTrackColor = Color.White.copy(alpha = .14f)))
        Text(detail, color = Color.White.copy(alpha = .42f), fontSize = 10.sp)
    }
}

@Composable
private fun SceneBackdrop(grid: Boolean, modifier: Modifier = Modifier) {
    Canvas(modifier) {
        drawRect(brush = Brush.linearGradient(listOf(Color(0xFF152E58), Color(0xFF17152F), Color(0xFF3B1E49), Color(0xFF0C2835)), start = Offset.Zero, end = Offset(size.width, size.height)))
        drawCircle(Brush.radialGradient(listOf(Color(0xFF568BFF).copy(alpha = .55f), Color.Transparent), center = Offset(size.width * .18f, size.height * .28f), radius = size.width * .55f), radius = size.width * .55f, center = Offset(size.width * .18f, size.height * .28f))
        drawCircle(Brush.radialGradient(listOf(Color(0xFFFF6DAE).copy(alpha = .42f), Color.Transparent), center = Offset(size.width * .82f, size.height * .48f), radius = size.width * .48f), radius = size.width * .48f, center = Offset(size.width * .82f, size.height * .48f))
        if (grid) {
            val step = size.width / 8f
            for (i in -2..10) {
                val x = i * step
                drawLine(Color.White.copy(alpha = .13f), Offset(x, 0f), Offset(x + size.height * .18f, size.height), 1.dp.toPx())
            }
            val swatches = listOf(Color(0xFFFFB86B), Color(0xFF85E6D2), Color(0xFF9AAEFF), Color(0xFFFF86B7), Color(0xFFD5A6FF), Color(0xFFFFE7A0))
            for (row in 0..7) for (col in 0..5) {
                val w = size.width * .075f
                val x = col * size.width * .18f + if (row % 2 == 0) 0f else size.width * .05f
                val y = row * size.height * .105f + size.height * .04f
                drawRoundRect(swatches[(row + col) % swatches.size].copy(alpha = .78f), Offset(x, y), Size(w, w * .72f), androidx.compose.ui.geometry.CornerRadius(w * .18f))
            }
        }
    }
}

@Composable
private fun GlassPreview(refraction: Float, blur: Float, dispersion: Float, tint: Float, rim: Float, clear: Boolean, modifier: Modifier = Modifier) {
    var touch by remember { mutableStateOf(Offset(.72f, .28f)) }
    Box(modifier.clip(RoundedCornerShape(32.dp))) {
        SceneBackdrop(true, Modifier.fillMaxSize())
        // Stacked soft translucent layers create a responsive glass material preview.
        Canvas(Modifier.fillMaxSize().pointerInput(Unit) {
            detectDragGestures { change, _ ->
                touch = Offset((change.position.x / size.width).coerceIn(0f, 1f), (change.position.y / size.height).coerceIn(0f, 1f))
            }
        }) {
            val radius = 30.dp.toPx()
            val shape = androidx.compose.ui.graphics.Path().apply {
                addRoundRect(androidx.compose.ui.geometry.RoundRect(0f, 0f, size.width, size.height, androidx.compose.ui.geometry.CornerRadius(radius)))
            }
            val baseAlpha = (if (clear) .045f else .12f) + blur * .17f
            drawPath(shape, Color(0xFFEAF2FF).copy(alpha = baseAlpha + tint * .22f))
            drawPath(shape, brush = Brush.linearGradient(listOf(Color.White.copy(alpha = .30f + rim * .12f), Color(0xFFB9D5FF).copy(alpha = .06f), Color(0xFFEDBFFF).copy(alpha = .12f), Color.White.copy(alpha = .04f)), start = Offset.Zero, end = Offset(size.width, size.height)))
            // The inset bevel approximates the lens boundary; controls tune the visual response.
            val bevel = (5f + refraction * 22f).dp.toPx()
            drawRoundRect(Brush.linearGradient(listOf(Color.White.copy(alpha = .7f * rim), Color.White.copy(alpha = .03f), Color(0xFFB5C9FF).copy(alpha = .38f * rim), Color.White.copy(alpha = .12f))), Offset(bevel / 2, bevel / 2), Size(size.width - bevel, size.height - bevel), androidx.compose.ui.geometry.CornerRadius(radius - bevel / 2), style = androidx.compose.ui.graphics.drawscope.Stroke(width = (1f + rim * 2.5f).dp.toPx()))
            val glow = 45.dp.toPx() + refraction * 45.dp.toPx()
            drawCircle(Brush.radialGradient(listOf(Color.White.copy(alpha = .38f * rim), Color(0xFF9CC7FF).copy(alpha = .14f * rim), Color.Transparent), center = Offset(size.width * touch.x, size.height * touch.y), radius = glow), radius = glow, center = Offset(size.width * touch.x, size.height * touch.y))
            if (dispersion > .01f) {
                val d = dispersion * 7.dp.toPx()
                drawRoundRect(Color(0xFFFF7DCE).copy(alpha = dispersion * .22f), Offset(d, d * .4f), Size(size.width - d * 2, size.height - d * .8f), androidx.compose.ui.geometry.CornerRadius(radius), style = androidx.compose.ui.graphics.drawscope.Stroke(width = 1.5.dp.toPx()))
                drawRoundRect(Color(0xFF7DE9FF).copy(alpha = dispersion * .25f), Offset(-d * .25f, d * .5f), Size(size.width - d, size.height - d), androidx.compose.ui.geometry.CornerRadius(radius), style = androidx.compose.ui.graphics.drawscope.Stroke(width = 1.dp.toPx()))
            }
            // Gloss streaks make the changing light direction easy to read.
            drawLine(Color.White.copy(alpha = .38f * rim), Offset(size.width * .12f, size.height * .07f), Offset(size.width * .36f, size.height * .07f), 2.dp.toPx(), cap = StrokeCap.Round)
        }
        Column(Modifier.align(Alignment.BottomStart).padding(22.dp)) {
            Text("GLASS / 01", color = Color.White.copy(alpha = .68f), fontSize = 10.sp, letterSpacing = 2.sp, fontWeight = FontWeight.Bold)
            Text(if (clear) "Clear crystal" else "Optical surface", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.SemiBold)
            Text("Drag to move the light", color = Color.White.copy(alpha = .68f), fontSize = 12.sp)
        }
        Surface(Modifier.align(Alignment.TopEnd).padding(16.dp), color = Color.White.copy(alpha = .13f), shape = CircleShape, border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = .24f))) {
            Text("n = 1.33", Modifier.padding(horizontal = 10.dp, vertical = 6.dp), color = Color.White, fontSize = 11.sp)
        }
    }
}
