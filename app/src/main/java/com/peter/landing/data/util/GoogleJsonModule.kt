package com.peter.landing.data.util

import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object GoogleJsonModule {

    @Provides
    @Singleton
    fun provideGoogleJson(): Gson {
        return Gson()
    }

}