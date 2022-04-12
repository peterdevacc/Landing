package com.peter.landing

import android.app.Application

/**
 * We use a separate App for tests to prevent initializing dependency injection.
 *
 * See [com.peter.landing.LandingTestRunner].
 */
class LandingTestApp : Application()