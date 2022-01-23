package foo.bar.compose.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.arch.core.internal.FastSafeIterableMap
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.Lifecycle
import co.early.fore.kt.core.delegate.Fore
import co.early.fore.kt.core.ui.observeAsState
import foo.bar.compose.App
import foo.bar.compose.OG
import foo.bar.compose.R
import foo.bar.compose.feature.example.ObservableModel
import foo.bar.compose.feature.example.SomeState
import foo.bar.compose.feature.example.flow.FlowModel
import foo.bar.compose.feature.example.fore.ForeModel
import foo.bar.compose.feature.example.livedata.LiveDataModel
import kotlin.reflect.jvm.isAccessible

class FirstActivity : ComponentActivity() {

    //models we are interested in
    private val flowModel: FlowModel = OG[FlowModel::class.java]
    private val foreModel: ForeModel = OG[ForeModel::class.java]
    private val liveDataModel: LiveDataModel = OG[LiveDataModel::class.java]

    fun startNextActivity() {
        SecondActivity.start(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Fore.getLogger().v("CREATE")

        App.inst.firstActivity = this

        setContent {

            Fore.getLogger().v("SET CONTENT")

            val flowViewState by flowModel.state.collectAsState(SomeState())
            val foreViewState by foreModel.observeAsState { foreModel.state }
            val liveDataViewState by liveDataModel.state.observeAsState(SomeState())

            MaterialTheme {

                Fore.getLogger().v("THEME")

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .background(colorResource(id = R.color.colorBackground)),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Fore.getLogger().v("MAIN CONTENT")

                    Button(onClick = { startNextActivity() }) {
                        Text(text = stringResource(id = R.string.nav_to_non_compose_activity))
                    }

                    Section(
                        label = stringResource(id = R.string.flow_obs),
                        viewState = flowViewState,
                        additionalObservableModel = flowModel,
                        toggleVisibility = { flowModel.toggleVisibility() },
                        increase = { flowModel.increase() },
                        decrease = { flowModel.decrease() },
                        logObservers = {
                            Fore.getLogger().e("flowModel has ${flowModel.logObserverCount()}")
                        },
                        modifier = Modifier.background(colorResource(id = R.color.colorPastel01)),
                    )

                    Section(
                        label = stringResource(id = R.string.fore_obs),
                        viewState = foreViewState,
                        additionalObservableModel = foreModel,
                        toggleVisibility = { foreModel.toggleVisibility() },
                        increase = { foreModel.increase() },
                        decrease = { foreModel.decrease() },
                        logObservers = {
                            Fore.getLogger().e("foreModel has ${foreModel.logObserverCount()}")
                        },
                        modifier = Modifier.background(colorResource(id = R.color.colorPastel02)),
                    )

                    Section(
                        label = stringResource(id = R.string.ld_obs),
                        viewState = liveDataViewState,
                        additionalObservableModel = liveDataModel,
                        toggleVisibility = { liveDataModel.toggleVisibility() },
                        increase = { liveDataModel.increase() },
                        decrease = { liveDataModel.decrease() },
                        logObservers = {
                            Fore.getLogger()
                                .e("liveDataModel has ${liveDataModel.logObserverCount()}")
                        },
                        modifier = Modifier.background(colorResource(id = R.color.colorPastel04)),
                    )
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        Fore.getLogger().v("START")
    }

    override fun onResume() {
        super.onResume()
        Fore.getLogger().v("RESUME")
    }

    override fun onPause() {
        super.onPause()
        Fore.getLogger().v("PAUSE")
    }

    override fun onStop() {
        super.onStop()
        Fore.getLogger().v("STOP")
    }

    override fun onDestroy() {
        super.onDestroy()
        Fore.getLogger().v("DESTROY")
    }
}

@Composable
fun Section(
    label: String,
    viewState: SomeState,
    additionalObservableModel: ObservableModel,
    toggleVisibility: () -> Unit,
    increase: () -> Unit,
    decrease: () -> Unit,
    logObservers: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .then(modifier),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {

            Fore.getLogger().i("SECTION ($label)")

            Text("$label ")

            Text(
                modifier = Modifier
                    .wrapContentSize()
                    .clickable { toggleVisibility() },
                fontWeight = FontWeight.ExtraBold,
                text = "${stringResource(id = if (viewState.showControls) R.string.hide else R.string.show)} "
            )

            Button(
                modifier = Modifier
                    .wrapContentSize(),
                onClick = { logObservers() }
            ) {
                Text(stringResource(id = R.string.log_obs_count))
            }
        }

        if (viewState.showControls) {
            Controls(
                label,
                additionalObservableModel,
                increase,
                decrease
            )
        }
    }
}

@Composable
fun Controls(
    label: String,
    additionalObservableModel: ObservableModel,
    increase: () -> Unit,
    decrease: () -> Unit,
    modifier: Modifier = Modifier
) {

    Fore.getLogger().i("CONTROLS ($label)")

    val observableState: State<SomeState>? = when (additionalObservableModel) {
        is FlowModel -> {
            additionalObservableModel.state.collectAsState(SomeState())
        }
        is ForeModel -> {
            additionalObservableModel.observeAsState("tracking") { additionalObservableModel.state }
        }
        is LiveDataModel -> {
            additionalObservableModel.state.observeAsState(SomeState())
        }
        else -> null
    }

    logLifeCycleObservers(LocalLifecycleOwner.current.lifecycle)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .then(modifier),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {

            Button(
                modifier = Modifier
                    .wrapContentSize()
                    .padding(dimensionResource(id = R.dimen.common_space_small)),
                onClick = { increase() }
            ) {
                Text(stringResource(id = R.string.increase))
            }

            Text(
                modifier = Modifier
                    .wrapContentSize()
                    .padding(dimensionResource(id = R.dimen.common_space_small)),
                text = observableState?.value?.magicNumber.toString()
            )

            Button(
                modifier = Modifier
                    .wrapContentSize()
                    .padding(dimensionResource(id = R.dimen.common_space_small)),
                onClick = { decrease() }
            ) {
                Text(stringResource(id = R.string.decrease))
            }
        }
    }
}

fun logLifeCycleObservers(obj: Lifecycle){
    val field = obj.javaClass.getDeclaredField("mObserverMap")
    field.isAccessible = true
    val mObservers = field.get(obj) as FastSafeIterableMap<*, *>
    val size = mObservers::class.members.find {
        it.name == "size"
    }?.let {
        it.isAccessible = true
        it.call(mObservers)
    }
    Fore.getLogger().e("*** lifecycleObservers: $size")
}
