package com.example.battlecompare.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.battlecompare.R
import kotlinx.coroutines.delay

/**
 * Splash screen with optimized animations.
 * Uses LaunchedEffect for timing instead of a full activity.
 */
@Composable
fun SplashScreen(
    onSplashComplete: () -> Unit,
    splashDuration: Long = 2000 // 2 seconds
) {
    // State to track animation progress
    var startAnimation by remember { mutableStateOf(false) }

    // Track if animation has completed to avoid redundant navigation
    var isNavigated by remember { mutableStateOf(false) }

    // LaunchedEffect to handle timing
    LaunchedEffect(key1 = true) {
        startAnimation = true
        delay(splashDuration)
        if (!isNavigated) {
            isNavigated = true
            onSplashComplete()
        }
    }

    // Scale animation for logo
    val scaleAnimation = animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0.5f,
        animationSpec = tween(
            durationMillis = 1000,
            easing = FastOutSlowInEasing
        ),
        label = "scale"
    )

    // Alpha animation for logo
    val alphaAnimation = animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(
            durationMillis = 1000,
            easing = LinearEasing
        ),
        label = "alpha"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primaryContainer),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Logo
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = "Logo",
                modifier = Modifier
                    .size(120.dp)
                    .scale(scaleAnimation.value)
                    .alpha(alphaAnimation.value)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // App name with fade in animation
            AnimatedVisibility(
                visible = startAnimation,
                enter = fadeIn(animationSpec = tween(1000)),
                exit = fadeOut()
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "BATTLE COMPARE",
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Loading indicator with animation
                    LoadingDots()
                }
            }
        }
    }
}

/**
 * Animated loading dots for the splash screen.
 */
@Composable
fun LoadingDots(
    dotCount: Int = 3,
    dotColor: Color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
) {
    // Infinite transition for dot animation
    val infiniteTransition = rememberInfiniteTransition(label = "loading")

    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(8.dp)
    ) {
        repeat(dotCount) { index ->
            // Create staggered animation for each dot
            val scale by infiniteTransition.animateFloat(
                initialValue = 0.6f,
                targetValue = 1.0f,
                animationSpec = infiniteRepeatable(
                    animation = tween(500, easing = FastOutSlowInEasing),
                    repeatMode = RepeatMode.Reverse,
                    initialStartOffset = StartOffset(index * 150)
                ),
                label = "dot_$index"
            )

            Box(
                modifier = Modifier
                    .padding(horizontal = 4.dp)
                    .size(8.dp * scale)
                    .background(
                        color = dotColor,
                        shape = androidx.compose.foundation.shape.CircleShape
                    )
            )
        }
    }
}