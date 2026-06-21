package com.qamarq.jellymusic.data.playback

import android.content.Context
import com.google.android.gms.cast.framework.CastOptions
import com.google.android.gms.cast.framework.OptionsProvider
import com.google.android.gms.cast.framework.SessionProvider

class CastOptionsProvider : OptionsProvider {
    override fun getCastOptions(context: Context): CastOptions {
        return CastOptions.Builder()
            .setReceiverApplicationId(JELLYMUSIC_CAST_RECEIVER_APP_ID)
            .build()
    }

    override fun getAdditionalSessionProviders(context: Context): MutableList<SessionProvider>? {
        return null
    }
}

/** App ID of the JellyMusic custom Cast receiver, registered in the Google Cast SDK Developer Console. */
const val JELLYMUSIC_CAST_RECEIVER_APP_ID = "0909CD3C"
