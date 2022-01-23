package foo.bar.compose.feature.example.fore

import co.early.fore.core.observer.Observable
import co.early.fore.core.observer.Observer
import co.early.fore.kt.core.delegate.Fore
import co.early.fore.kt.core.observer.ObservableImp
import foo.bar.compose.feature.example.ObservableModel
import foo.bar.compose.feature.example.SomeState

class ForeModel : Observable, ObservableModel {

    // for diagnostics
    private val observable = ObservableImp()
    private var observerCount = 0

    var state: SomeState = SomeState()
        private set

    init {
        Fore.getLogger().i("init() id:${Thread.currentThread().id}")
    }

    fun increase() {
        Fore.getLogger().i("increase() id:${Thread.currentThread().id} |${state}|")
        if (state.canIncrease()) {
            state = state.copy(magicNumber = state.magicNumber + 1)
            notifyObservers()
        }
    }

    fun decrease() {
        Fore.getLogger().i("decrease() id:${Thread.currentThread().id} |${state}|")
        if (state.canDecrease()) {
            state = state.copy(magicNumber = state.magicNumber - 1)
            notifyObservers()
        }
    }

    fun toggleVisibility() {
        Fore.getLogger().i("toggleDisplay() id:${Thread.currentThread().id} |${state}|")
        state = state.copy(showControls = !state.showControls)
        notifyObservers()
    }

    /**
     * Diagnostics below to find out how many observers we currently have
     */

    fun logObserverCount() : String {
        return "$observerCount observers"
    }

    override fun addObserver(observer: Observer) {
        observable.addObserver(observer)
        observerCount++
    }

    override fun removeObserver(observer: Observer) {
        observable.removeObserver(observer)
        observerCount--
    }

    override fun notifyObservers() {
        observable.notifyObservers()
    }

    override fun hasObservers(): Boolean {
        return observable.hasObservers()
    }
}
