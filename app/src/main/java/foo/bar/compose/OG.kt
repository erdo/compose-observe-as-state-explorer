package foo.bar.compose

import android.app.Application
import co.early.fore.kt.core.delegate.DebugDelegateDefault
import co.early.fore.kt.core.delegate.Fore
import foo.bar.compose.feature.example.flow.FlowModel
import foo.bar.compose.feature.example.fore.ForeModel
import foo.bar.compose.feature.example.livedata.LiveDataModel
import java.util.*

/**
 * Copyright © 2015-2022 early.co. All rights reserved.
 */
@Suppress("UNUSED_PARAMETER")
object OG {

    private var initialized = false
    private val dependencies = HashMap<Class<*>, Any>()

    fun setApplication(application: Application) {

        // create dependency graph
        if (BuildConfig.DEBUG) {
            Fore.setDelegate(DebugDelegateDefault("foo_"))
        }
        val flowModel = FlowModel()
        val foreModel = ForeModel()
        val liveDataModel = LiveDataModel()

        // add models to the dependencies map for later
        dependencies[FlowModel::class.java] = flowModel
        dependencies[ForeModel::class.java] = foreModel
        dependencies[LiveDataModel::class.java] = liveDataModel
    }

    fun init() {
        if (!initialized) {
            initialized = true

            // run any necessary initialization code once object graph has been created here

        }
    }

    /**
     * This is how dependencies get injected, typically an Activity/Fragment/View will call this
     * during the onCreate()/onCreateView()/onFinishInflate() method respectively for each of the
     * dependencies it needs.
     *
     * Can use a DI library for similar behaviour using annotations
     *
     * Will return mocks if they have been set previously in putMock()
     *
     *
     * Call it like this:
     *
     * <code>
     *     yourModel = OG[YourModel::class.java]
     * </code>
     *
     * If you want to more tightly scoped object, one way is to pass a factory class here and create
     * an instance where you need it
     *
     */
    @Suppress("UNCHECKED_CAST")
    operator fun <T> get(model: Class<T>): T = dependencies[model] as T

    fun <T> putMock(clazz: Class<T>, instance: T) {
        dependencies[clazz] = instance as Any
    }
}
