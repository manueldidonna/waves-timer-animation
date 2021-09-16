package com.manueldidonna.wavestimeranimation

import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.insets.statusBarsPadding

@Composable
fun WavesTimerAnimation() {
    Column(modifier = Modifier.fillMaxSize()) {
        WavesTimerAppBar()
        Box(modifier = Modifier.weight(1f, fill = true)) {
            var timerDurationInMillis by rememberSaveable { mutableStateOf(0) }
            var timerState by remember { mutableStateOf(TimerState.Stopped) }

            val timerProgress by timerProgressAsState(
                timerState = timerState,
                timerDurationInMillis = timerDurationInMillis
            )

            LaunchedEffect(timerProgress == 0f) {
                if (timerProgress == 0f) {
                    timerState = TimerState.Stopped
                }
            }

            WavesLoadingIndicator(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colors.primary,
                progress = timerProgress
            )

            Timer(
                modifier = Modifier
                    .navigationBarsPadding()
                    .padding(bottom = 56.dp + 24.dp + 12.dp)
                    .align(Alignment.Center),
                timerDurationInMillis = timerDurationInMillis,
                onTimerDurationInMillisChange = { timerDurationInMillis = it },
                isTimerStarted = timerState != TimerState.Stopped,
                onTimerIsStartedChange = { isTimerStarted ->
                    timerState = if (isTimerStarted) TimerState.Started else TimerState.Stopped
                },
                timerProgress = timerProgress
            )

            PauseTimerFloatingButton(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, bottom = 24.dp),
                isVisible = timerState != TimerState.Stopped,
                isTimerPaused = timerState == TimerState.Paused,
                onClick = {
                    timerState =
                        if (timerState == TimerState.Paused) TimerState.Started
                        else TimerState.Paused
                }
            )
        }
    }
}

@Composable
private fun WavesTimerAppBar() {
    Surface(
        elevation = AppBarDefaults.TopAppBarElevation,
        color = MaterialTheme.colors.surface,
        contentColor = MaterialTheme.colors.onSurface
    ) {
        TopAppBar(
            title = { Text(text = "Waves Timer Animation") },
            elevation = 0.dp,
            backgroundColor = Color.Transparent,
            modifier = Modifier.statusBarsPadding()
        )
    }
}

private enum class TimerState {
    Started,
    Stopped,
    Paused
}

@Composable
private fun timerProgressAsState(
    timerState: TimerState,
    timerDurationInMillis: Int
): State<Float> {
    val animatable = remember { Animatable(initialValue = 0f) }

    LaunchedEffect(timerState) {
        val animateToStartOrStopState = timerState == TimerState.Stopped ||
                (timerState == TimerState.Started && animatable.value == 0f)

        if (animateToStartOrStopState) {
            animatable.animateTo(
                targetValue = if (timerState == TimerState.Started) 1f else 0f,
                animationSpec = spring(stiffness = 100f)
            )
        }

        if (timerState == TimerState.Started) {
            animatable.animateTo(
                targetValue = 0f,
                animationSpec = tween(
                    durationMillis = timerDurationInMillis,
                    easing = LinearEasing
                ),
            )
        }
    }

    return remember(animatable) {
        derivedStateOf { animatable.value }
    }
}

@Composable
private fun PauseTimerFloatingButton(
    modifier: Modifier,
    isVisible: Boolean,
    isTimerPaused: Boolean,
    onClick: () -> Unit
) {
    val transition = updateTransition(targetState = isVisible, label = "pause timer button")

    val enterTransitionSpec = remember {
        spring<Float>(
            dampingRatio = SpringDefaultDampingRatio,
            stiffness = SpringDefaultStiffness
        )
    }

    val exitTransitionSpec = remember {
        tween<Float>(
            durationMillis = 220,
            easing = FastOutSlowInEasing
        )
    }

    val alpha by transition.animateFloat(
        transitionSpec = { if (targetState) enterTransitionSpec else exitTransitionSpec },
        targetValueByState = { state -> if (state) 1f else 0f },
        label = "Alpha Animation"
    )

    val translationY by transition.animateFloat(
        transitionSpec = { if (targetState) enterTransitionSpec else exitTransitionSpec },
        targetValueByState = { state ->
            if (state) 0f else with(LocalDensity.current) { 40.dp.toPx() }
        },
        label = "Translation Y Animation"
    )

    Box(
        modifier = modifier.graphicsLayer {
            this.clip = true
            this.alpha = alpha
            this.translationY = translationY
        }
    ) {
        ExtendedFloatingActionButton(
            modifier = modifier,
            text = { Text(text = if (isTimerPaused) "RESUME" else "PAUSE") },
            elevation = FloatingActionButtonDefaults.elevation(
                defaultElevation = 2.dp,
                pressedElevation = 6.dp
            ),
            onClick = onClick
        )
    }
}
