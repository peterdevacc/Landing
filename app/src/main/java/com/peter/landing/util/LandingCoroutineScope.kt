package com.peter.landing.util

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.CoroutineContext

@Singleton
class LandingCoroutineScope @Inject constructor() : CoroutineScope {

    private val exceptionHandler = CoroutineExceptionHandler { _, _ -> }

    override val coroutineContext: CoroutineContext
        get() = SupervisorJob() + Dispatchers.Default + exceptionHandler

}