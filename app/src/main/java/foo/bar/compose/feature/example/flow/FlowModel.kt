package foo.bar.compose.feature.example.flow

import co.early.fore.kt.core.delegate.Fore
import foo.bar.compose.feature.example.ObservableModel
import foo.bar.compose.feature.example.SomeState
import kotlinx.coroutines.flow.*

class FlowModel: ObservableModel {

    private val _state: MutableStateFlow<SomeState> = MutableStateFlow(SomeState())
    val state = _state.asStateFlow()

    init {
        Fore.getLogger().i("init() id:${Thread.currentThread().id}")
    }

    fun increase() {
        Fore.getLogger().i("increase() id:${Thread.currentThread().id} |${state.value}|")
        if (_state.value.canIncrease()) {
            _state.value = _state.value.copy(magicNumber = _state.value.magicNumber + 1)
        }
    }

    fun decrease() {
        Fore.getLogger().i("decrease() id:${Thread.currentThread().id} |${state.value}|")
        if (_state.value.canDecrease()) {
            _state.value = _state.value.copy(magicNumber = _state.value.magicNumber - 1)
        }
    }

    fun toggleVisibility() {
        Fore.getLogger().i("toggleDisplay() id:${Thread.currentThread().id} |${state.value}|")
        _state.value = _state.value.copy(showControls = !_state.value.showControls)
    }

    /**
     * Diagnostics below to find out how many observers we currently have
     */
    fun logObserverCount() : String {

        //logReflection(_state.javaClass.superclass)

        val collectors = declaredField(_state, "nCollectors") as Int? // number of allocated (!free) slots

        return "allocatedSlots:$collectors subscriptionCount:${_state.subscriptionCount.value}"
    }

    private fun declaredField(obj: MutableStateFlow<SomeState>, name: String): Any? {
        val field = obj.javaClass.superclass.getDeclaredField(name)
        field.isAccessible = true
        return field.get(obj)
    }

    private fun logReflection(clazz: Class<*>){

        Fore.getLogger().e("logReflection() for ${clazz.canonicalName}")

        clazz.fields.iterator().forEach {
            Fore.getLogger().e("fields: ${it.name}")
        }

        clazz.declaredFields.iterator().forEach {
            Fore.getLogger().e("declaredFields: ${it.name}")
        }

        clazz.declaredMethods.iterator().forEach {
            Fore.getLogger().e("declaredMethods: ${it.name}")
        }

        clazz.methods.iterator().forEach {
            Fore.getLogger().e("methods: ${it.name}")
        }
    }
}
