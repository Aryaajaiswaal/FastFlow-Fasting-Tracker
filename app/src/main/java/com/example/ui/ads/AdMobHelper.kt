package com.example.ui.ads

import android.content.Context
import android.util.Log
import android.view.ViewGroup
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds

/**
 * AdMob initialization helper for FastFlow.
 * Handles SDK initialization, banner ad loading, and life-cycle management
 * using the configured AdMob IDs.
 */
class AdMobHelper(private val context: Context) {

    companion object {
        const val APP_ID = "ca-app-pub-3488309381496555~5578541915"
        const val BANNER_AD_UNIT_ID = "ca-app-pub-3488309381496555/9238585320"
        private const val TAG = "AdMobHelper"

        @Volatile
        private var instance: AdMobHelper? = null

        fun getInstance(context: Context): AdMobHelper {
            return instance ?: synchronized(this) {
                instance ?: AdMobHelper(context.applicationContext).also { instance = it }
            }
        }
    }

    private var bannerAdView: AdView? = null
    var isInitialized: Boolean = false
        private set

    /**
     * Initializes Google Mobile Ads SDK and immediately loads a banner ad.
     * Call this in MainActivity.onCreate.
     */
    fun initializeAndLoadBanner(
        onInitialized: (() -> Unit)? = null,
        onBannerLoaded: ((AdView) -> Unit)? = null,
        onBannerFailedToLoad: ((LoadAdError) -> Unit)? = null
    ): AdView {
        val adView = AdView(context).apply {
            setAdSize(AdSize.BANNER)
            adUnitId = BANNER_AD_UNIT_ID
        }
        bannerAdView = adView

        try {
            MobileAds.initialize(context) { status ->
                isInitialized = true
                Log.d(TAG, "AdMob SDK Initialized successfully. Status: $status")
                onInitialized?.invoke()

                adView.adListener = object : AdListener() {
                    override fun onAdLoaded() {
                        Log.d(TAG, "Banner ad loaded successfully with unit: $BANNER_AD_UNIT_ID")
                        onBannerLoaded?.invoke(adView)
                    }

                    override fun onAdFailedToLoad(error: LoadAdError) {
                        Log.w(TAG, "Banner ad failed to load: ${error.message} (Code: ${error.code})")
                        onBannerFailedToLoad?.invoke(error)
                    }
                }

                val adRequest = AdRequest.Builder().build()
                adView.loadAd(adRequest)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error initializing MobileAds or loading banner", e)
        }

        return adView
    }

    fun getBannerAdView(): AdView? = bannerAdView

    fun detachBannerFromParent() {
        bannerAdView?.let { adView ->
            (adView.parent as? ViewGroup)?.removeView(adView)
        }
    }

    fun destroyBanner() {
        bannerAdView?.destroy()
        bannerAdView = null
    }
}
