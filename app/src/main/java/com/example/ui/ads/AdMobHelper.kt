package com.example.ui.ads

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.ViewGroup
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Production AdMob initialization and management helper for FastFlow.
 * Handles background SDK initialization, banner loading, and revenue-maximizing
 * interstitial ad triggers upon fasting session completion.
 */
class AdMobHelper private constructor(private val appContext: Context) {

    companion object {
        const val APP_ID = "ca-app-pub-3488309381496555~5578541915"
        // User provided Ad Unit ID used for both banner and high-yield interstitial placement
        const val AD_UNIT_ID = "ca-app-pub-3488309381496555/9238585320"
        const val BANNER_AD_UNIT_ID = AD_UNIT_ID
        const val INTERSTITIAL_AD_UNIT_ID = AD_UNIT_ID

        // Fallback Google Test Unit IDs if live ads are temporarily unavailable
        const val TEST_INTERSTITIAL_AD_UNIT_ID = "ca-app-pub-3940256099942544/1033173712"
        const val TEST_BANNER_AD_UNIT_ID = "ca-app-pub-3940256099942544/6300978111"

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
    private var interstitialAd: InterstitialAd? = null
    private var isInterstitialLoading: Boolean = false

    var isInitialized: Boolean = false
        private set

    /**
     * Initializes Google Mobile Ads SDK on a background thread to prevent
     * main thread binder stalls and measurement service connection errors.
     */
    fun initialize(
        activity: Activity,
        onInitialized: (() -> Unit)? = null
    ) {
        if (isInitialized) {
            onInitialized?.invoke()
            return
        }

        CoroutineScope(Dispatchers.IO).launch {
            try {
                MobileAds.initialize(activity) { status ->
                    isInitialized = true
                    Log.d(TAG, "AdMob SDK Initialized successfully. Status: $status")

                    // Preload high-converting interstitial ad immediately so it's ready
                    // the moment the user finishes their fast
                    loadInterstitialAd(activity)

                    activity.runOnUiThread {
                        onInitialized?.invoke()
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Exception during MobileAds background initialization", e)
            }
        }
    }

    /**
     * Initializes and loads a banner ad using the provided Activity context.
     * Creating AdView with Activity context ensures proper window token binding
     * and avoids MESA rendernode errors.
     */
    fun initializeAndLoadBanner(
        activity: Activity,
        onBannerLoaded: ((AdView) -> Unit)? = null
    ): AdView {
        val adView = AdView(activity).apply {
            setAdSize(AdSize.BANNER)
            adUnitId = BANNER_AD_UNIT_ID
        }
        bannerAdView = adView

        initialize(activity) {
            val adRequest = AdRequest.Builder().build()
            adView.adListener = object : AdListener() {
                override fun onAdLoaded() {
                    Log.d(TAG, "Banner ad loaded successfully with unit: $BANNER_AD_UNIT_ID")
                    onBannerLoaded?.invoke(adView)
                }

                override fun onAdFailedToLoad(error: LoadAdError) {
                    Log.w(TAG, "Banner ad failed to load: ${error.message} (Code: ${error.code})")
                }
            }
            adView.loadAd(adRequest)
        }

        return adView
    }

    /**
     * Preloads an interstitial ad using the configured unit ID.
     */
    fun loadInterstitialAd(activity: Activity) {
        if (interstitialAd != null || isInterstitialLoading) return

        isInterstitialLoading = true
        val adRequest = AdRequest.Builder().build()

        InterstitialAd.load(
            activity,
            INTERSTITIAL_AD_UNIT_ID,
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(ad: InterstitialAd) {
                    interstitialAd = ad
                    isInterstitialLoading = false
                    Log.d(TAG, "Interstitial ad loaded and primed for session completion")
                }

                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    interstitialAd = null
                    isInterstitialLoading = false
                    Log.w(TAG, "Interstitial ad failed with production unit: ${loadAdError.message}. Trying test unit fallback.")
                    
                    // Fallback to test unit so developer/test sessions still render interstitial
                    loadFallbackInterstitialAd(activity)
                }
            }
        )
    }

    private fun loadFallbackInterstitialAd(activity: Activity) {
        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(
            activity,
            TEST_INTERSTITIAL_AD_UNIT_ID,
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(ad: InterstitialAd) {
                    interstitialAd = ad
                    Log.d(TAG, "Fallback Interstitial ad loaded successfully")
                }
                override fun onAdFailedToLoad(error: LoadAdError) {
                    interstitialAd = null
                    Log.w(TAG, "Fallback interstitial also failed: ${error.message}")
                }
            }
        )
    }

    /**
     * Triggers the full-screen interstitial ad after a fasting session completes.
     * Automatically reloads the next interstitial upon dismissal to maintain
     * continuous monetization across all fasting cycles.
     */
    fun showInterstitialAd(activity: Activity, onAdDismissed: () -> Unit = {}) {
        val ad = interstitialAd
        if (ad != null) {
            ad.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    Log.d(TAG, "Interstitial dismissed by user. Reloading for next fast.")
                    interstitialAd = null
                    loadInterstitialAd(activity)
                    onAdDismissed()
                }

                override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                    Log.e(TAG, "Interstitial failed to show: ${adError.message}")
                    interstitialAd = null
                    loadInterstitialAd(activity)
                    onAdDismissed()
                }

                override fun onAdShowedFullScreenContent() {
                    Log.d(TAG, "Interstitial impression logged successfully!")
                }
            }
            ad.show(activity)
        } else {
            Log.d(TAG, "Interstitial ad not ready yet. Preloading and proceeding.")
            loadInterstitialAd(activity)
            onAdDismissed()
        }
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
