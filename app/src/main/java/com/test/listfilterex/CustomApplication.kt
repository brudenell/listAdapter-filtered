package com.test.listfilterex

import android.app.Application
import com.unsplash.pickerandroid.photopicker.UnsplashPhotoPicker

class CustomApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        UnsplashPhotoPicker.init(
            this,
            "sJwBIjoHrpQw4ZsyeTQW1ILJwCu7EwfHHiyBC2yJ9_Y",
            "Iy6sFQOYODsLqx52sqjWYqBaKAmkTXLsxgx7zpUstLY",
            1
        )
    }
}