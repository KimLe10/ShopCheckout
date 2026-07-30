package com.kimle.shopcheckout

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.kimle.shopcheckout.navigation.ShopNavGraph
import com.kimle.shopcheckout.ui.theme.ShopCheckoutTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ShopCheckoutTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    ShopNavGraph()
                }
            }
        }
    }
}
