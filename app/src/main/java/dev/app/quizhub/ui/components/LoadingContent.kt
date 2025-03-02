package dev.app.quizhub.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun <T> LoadingContent(
    isLoading: Boolean,
    content: T?,
    onContent: @Composable (T) -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        when {
            isLoading || content == null -> CircularProgressIndicator(
                color = MaterialTheme.colorScheme.primary
            )
            else -> onContent(content)
        }
    }
}

@Composable
fun <T> LoadingContent(
    content: T?,
    onContent: @Composable (T) -> Unit
) {
    LoadingContent(
        isLoading = false,
        content = content,
        onContent = onContent
    )
} 