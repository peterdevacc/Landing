package com.peter.landing.data.util

import android.content.Context
import android.content.res.AssetManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AssetManagerModule {

    @Singleton
    @Provides
    fun provideAssetManager(
        @ApplicationContext appContext: Context
    ): AssetManager {
        return appContext.assets
    }

}