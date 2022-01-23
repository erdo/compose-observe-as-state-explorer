package foo.bar.compose

import android.app.Application
import foo.bar.compose.ui.FirstActivity

/**
 * Copyright © 2015-2021 early.co. All rights reserved.
 */
class App : Application() {

    // obviously, don't do this, it's just for hacky investigations
    var firstActivity: FirstActivity? = null

    override fun onCreate() {
        super.onCreate()

        inst = this

        OG.setApplication(this)
        OG.init()
    }

    companion object {
        lateinit var inst: App private set
    }
}
