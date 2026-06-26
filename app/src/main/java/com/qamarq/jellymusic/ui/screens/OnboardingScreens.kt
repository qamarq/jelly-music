package com.qamarq.jellymusic.ui.screens

import androidx.annotation.DrawableRes
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.qamarq.jellymusic.R
import com.qamarq.jellymusic.ui.theme.ElovaireRadii
import com.qamarq.jellymusic.ui.theme.RoseAccent
import com.qamarq.jellymusic.ui.theme.elovaireScaledSp
import kotlinx.coroutines.launch

internal data class OnboardingStep(
    @DrawableRes val iconResId: Int,
    val title: String,
    val description: String,
    val primaryLabel: String,
    val skippable: Boolean = true,
    // Permission-request steps launch an async system dialog - the result comes back later
    // and removes the now-granted step from the list (the carousel naturally lands on the next
    // remaining step at the same index). Auto-advancing immediately on tap, before that result
    // arrives, races ahead of the actual system dialog and ends up skipping/misattributing steps.
    val autoAdvanceOnClick: Boolean = true,
    val onPrimaryAction: () -> Unit,
)

@Composable
internal fun OnboardingCarousel(
    steps: List<OnboardingStep>,
    onFinished: () -> Unit,
) {
    val pagerState = rememberPagerState(pageCount = { steps.size })
    val scope = rememberCoroutineScope()
    // Keyed on steps - without this, the derivedStateOf is created once on first composition
    // and keeps comparing against that original (longer) list's lastIndex forever, even after
    // granted permissions shrink the list. isLastPage then never becomes true again, so the
    // final "Connect" step's click silently tries to animate to a page index that no longer
    // exists instead of finishing onboarding.
    val isLastPage by remember(steps) { derivedStateOf { pagerState.currentPage == steps.lastIndex } }
    val canGoBack by remember { derivedStateOf { pagerState.currentPage > 0 } }

    // A granted permission removes its step from the list (see autoAdvanceOnClick below), which
    // can leave currentPage pointing past the end of the now-shorter list.
    LaunchedEffect(steps.size) {
        if (pagerState.currentPage > steps.lastIndex) {
            pagerState.scrollToPage(steps.lastIndex.coerceAtLeast(0))
        }
    }

    fun advance() {
        if (isLastPage) {
            onFinished()
        } else {
            scope.launch {
                pagerState.animateScrollToPage(
                    page = pagerState.currentPage + 1,
                    animationSpec = tween(durationMillis = 320),
                )
            }
        }
    }

    fun goBack() {
        if (canGoBack) {
            scope.launch {
                pagerState.animateScrollToPage(
                    page = pagerState.currentPage - 1,
                    animationSpec = tween(durationMillis = 320),
                )
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding(),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .padding(start = 8.dp),
                contentAlignment = Alignment.CenterStart,
            ) {
                if (canGoBack) {
                    TextButton(onClick = { goBack() }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_lucide_chevron_left),
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(20.dp),
                        )
                    }
                }
            }
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                userScrollEnabled = false,
            ) { page ->
                OnboardingStepContent(step = steps[page])
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.Center,
            ) {
                steps.forEachIndexed { index, _ ->
                    val selected = index == pagerState.currentPage
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .size(width = if (selected) 22.dp else 7.dp, height = 7.dp)
                            .clip(RoundedCornerShape(50))
                            .background(
                                if (selected) {
                                    RoseAccent
                                } else {
                                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.18f)
                                },
                            ),
                    )
                }
            }

            val currentStep = steps[pagerState.currentPage.coerceIn(0, steps.lastIndex)]
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Button(
                    onClick = {
                        currentStep.onPrimaryAction()
                        // Permission steps launch an async system dialog - the actual result
                        // (delivered later) removes the now-granted step from the list, which
                        // naturally lands the carousel on the next remaining step. Advancing here
                        // too would race ahead of that dialog and skip/misattribute steps.
                        if (currentStep.autoAdvanceOnClick) {
                            advance()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(ElovaireRadii.pill),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = RoseAccent,
                        contentColor = Color.White,
                    ),
                ) {
                    Text(
                        text = currentStep.primaryLabel,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                    )
                }
                if (currentStep.skippable) {
                    TextButton(
                        onClick = { advance() },
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text(
                            text = "Skip",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                } else {
                    Spacer(modifier = Modifier.height(48.dp))
                }
            }
        }
    }
}

@Composable
private fun OnboardingStepContent(step: OnboardingStep) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Surface(
            shape = CircleShape,
            color = RoseAccent.copy(alpha = 0.14f),
            modifier = Modifier.size(96.dp),
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    painter = painterResource(id = step.iconResId),
                    contentDescription = null,
                    tint = RoseAccent,
                    modifier = Modifier.size(40.dp),
                )
            }
        }
        Spacer(modifier = Modifier.height(28.dp))
        Text(
            text = step.title,
            style = MaterialTheme.typography.displayLarge.copy(fontSize = elovaireScaledSp(26f)),
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface,
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = step.description,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}
