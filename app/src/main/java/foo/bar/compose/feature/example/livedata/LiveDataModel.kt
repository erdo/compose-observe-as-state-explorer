package foo.bar.compose.feature.example.livedata

import androidx.arch.core.internal.SafeIterableMap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import co.early.fore.kt.core.delegate.Fore
import foo.bar.compose.feature.example.ObservableModel
import foo.bar.compose.feature.example.SomeState
import kotlin.reflect.jvm.isAccessible

class LiveDataModel: ObservableModel {

    private val _state = MutableLiveData(SomeState())
    var state: LiveData<SomeState> = _state

    init {
        Fore.getLogger().i("init() id:${Thread.currentThread().id}")
    }

    fun increase() {
        Fore.getLogger().i("increase() id:${Thread.currentThread().id} |${state.value}|")
        state.value?.let {
            if (it.canIncrease()) {
                _state.value = it.copy(magicNumber = it.magicNumber + 1)
            }
        }
    }

    fun decrease() {
        Fore.getLogger().i("decrease() id:${Thread.currentThread().id} |${state.value}|")
        state.value?.let {
            if (it.canDecrease()) {
                _state.value = it.copy(magicNumber = it.magicNumber - 1)
            }
        }
    }

    fun toggleVisibility() {
        Fore.getLogger().i("toggleDisplay() id:${Thread.currentThread().id} |${state.value}|")
        state.value?.let {
            _state.value = it.copy(showControls = !it.showControls)
        }
    }

    /**
     * Diagnostics below to find out how many observers we currently have
     */

    fun logObserverCount() : String {
        val activeCount = call(state, "mActiveCount") as Int
        val observers = declaredField(state as Object, "mObservers") as SafeIterableMap<*, *>
        return "observers:${call(observers, "size") as Int} active:$activeCount"
    }

    private fun call(clazz: Any, name: String): Any? {
        return clazz::class.members.find {
            it.name == name
        }?.let {
            it.isAccessible = true
            it.call(clazz)
        }
    }

    private fun declaredField(obj: Object, name: String): Any? {
        val field = obj.javaClass.superclass.getDeclaredField(name)
        field.isAccessible = true
        return field.get(obj)
    }
}
