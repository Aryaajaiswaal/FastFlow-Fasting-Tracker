package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.ui.MainAppScaffold
import com.example.ui.ads.AdMobHelper
import com.example.ui.theme.MyApplicationTheme
import com.google.android.gms.ads.AdView

class MainActivity : ComponentActivity() {

    private lateinit var adMobHelper: AdMobHelper
    private var bannerAdView: AdView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // AdMob initialization helper initializes SDK in background and prepares banner & interstitial ads
        adMobHelper = AdMobHelper.getInstance(this)
        bannerAdView = adMobHelper.initializeAndLoadBanner(activity = this)

        setContent {
            MyApplicationTheme {
                MainAppScaffold(
                    preloadedBannerAdView = bannerAdView,
                    adMobHelper = adMobHelper
                )
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        adMobHelper.destroyBanner()
    }
}
