package foo.bar.compose.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import co.early.fore.kt.core.delegate.Fore
import foo.bar.compose.OG
import foo.bar.compose.databinding.ActivitySecondBinding
import foo.bar.compose.feature.example.flow.FlowModel
import foo.bar.compose.feature.example.fore.ForeModel
import foo.bar.compose.feature.example.livedata.LiveDataModel

class SecondActivity : FragmentActivity() {

    //models we are interested in
    private val flowModel: FlowModel = OG[FlowModel::class.java]
    private val foreModel: ForeModel = OG[ForeModel::class.java]
    private val liveDataModel: LiveDataModel = OG[LiveDataModel::class.java]

    private lateinit var vb: ActivitySecondBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // looks like kotlin-android-extensions will finally need
        // to be abandoned when using compose with kotlin 1.6
        // https://issuetracker.google.com/issues/211475860
        vb = ActivitySecondBinding.inflate(layoutInflater)

        setupClickListeners()

        setContentView(vb.root)
    }

    private fun setupClickListeners() {

        vb.flowIncrease.setOnClickListener { flowModel.increase() }
        vb.flowDecrease.setOnClickListener { flowModel.decrease() }
        vb.flowShowHide.setOnClickListener { flowModel.toggleVisibility() }
        vb.flowLogObsCount.setOnClickListener { Fore.getLogger().e("flowModel has ${flowModel.logObserverCount()}") }

        vb.foreIncrease.setOnClickListener { foreModel.increase() }
        vb.foreDecrease.setOnClickListener { foreModel.decrease() }
        vb.foreShowHide.setOnClickListener { foreModel.toggleVisibility() }
        vb.foreLogObsCount.setOnClickListener { Fore.getLogger().e("foreModel has ${foreModel.logObserverCount()}") }

        vb.ldIncrease.setOnClickListener { liveDataModel.increase() }
        vb.ldDecrease.setOnClickListener { liveDataModel.decrease() }
        vb.ldShowHide.setOnClickListener { liveDataModel.toggleVisibility() }
        vb.ldLogObsCount.setOnClickListener { Fore.getLogger().e("liveDataModel has ${liveDataModel.logObserverCount()}") }

        vb.finishFirstActivity.setOnClickListener { foo.bar.compose.App.inst.firstActivity?.finish() }
    }

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, SecondActivity::class.java)
            context.startActivity(intent)
        }
    }
}
