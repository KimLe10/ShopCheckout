package com.kimle.shopcheckout.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight

/**
 * Full-width primary action button (e.g. "Add to Cart", "Proceed to Checkout").
 * Centralizing this keeps button copy/weight consistent across screens instead
 * of every screen defining its own ad-hoc [Button].
 */
@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier.fillMaxWidth()
    ) {
        Text(text, fontWeight = FontWeight.SemiBold, style = MaterialTheme.typography.labelLarge)
    }
}

/**
 * Compact action button for repeated per-item actions (e.g. "Add to Cart" on a product row).
 * Same text weight as [PrimaryButton] so copy reads consistently, just not full-width.
 */
@Composable
fun CompactButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
    ) {
        Text(text, fontWeight = FontWeight.SemiBold, style = MaterialTheme.typography.labelLarge)
    }
}
