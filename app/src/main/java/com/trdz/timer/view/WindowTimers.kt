package com.trdz.timer.view

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.trdz.timer.base_utility.TIMER_F
import com.trdz.timer.base_utility.TIMER_S
import com.trdz.timer.databinding.FragmentNavigationBinding
import com.trdz.timer.model.DataTimer
import com.trdz.timer.view_model.MainViewModel
import com.trdz.timer.view_model.StatusProcess
import com.trdz.timer.view_model.ViewModelFactory
import org.koin.android.ext.android.inject
import java.lang.StringBuilder


class WindowTimers: Fragment() {

	//region Elements

	private var _binding: FragmentNavigationBinding? = null
	private val binding get() = _binding!!

	//endregion

	//region Injected

	private val factory: ViewModelFactory by inject()

	private val viewModel: MainViewModel by viewModels {
		factory
	}

	//endregion

	//region Base realization

	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
		_binding = FragmentNavigationBinding.inflate(inflater, container, false)
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		vmSetup()
		bindings()
		restore()
	}

	private fun vmSetup() {
		val observer = Observer<StatusProcess> { renderData(it) }
		viewModel.getData().observe(viewLifecycleOwner, observer)
	}

	private var justForFun = 0
	private fun bindings() {
		with(binding) {
			countButt.setOnClickListener {
				justForFun++
				counts.text = justForFun.toString()
			}
			countButt.setOnLongClickListener {
				justForFun = 0
				binding.counts.text = ""
				true
			}
			timerUp.timer.setOnClickListener {
				viewModel.use(TIMER_F)
			}
			timerUp.timer.setOnLongClickListener {
				viewModel.refresh(TIMER_F)
				true
			}
			timerDown.timer.setOnClickListener {
				viewModel.use(TIMER_S)
			}
			timerDown.timer.setOnLongClickListener {
				viewModel.refresh(TIMER_S)
				true
			}
		}
	}
	//endregion

	//region Instance

	private fun restore() {
		viewModel.restore()
	}

	//endregion


	//region ViewModel command realization

	private fun renderData(material: StatusProcess) {
		when (material) {
			is StatusProcess.Loading -> { }
			is StatusProcess.Error -> { }
			is StatusProcess.Success -> update(material.data)
			is StatusProcess.State -> {
				if (material.states[TIMER_F]) firstTimerOn()
				else firstTimerOff()
				if (material.states[TIMER_S]) secondTimerOn()
				else secondTimerOff()
			}
		}
	}

	private fun update(data: List<DataTimer?>) {
		with(binding) {
			if (data[TIMER_F] != null) {
				timerUp.screenMin.text = data[TIMER_F]!!.mn
				timerUp.screenSec.text = data[TIMER_F]!!.sc
				timerUp.screenMls.text = data[TIMER_F]!!.ms
			}
			else if (data[TIMER_S] != null) {
				timerDown.screenMin.text = data[TIMER_S]!!.mn
				timerDown.screenSec.text = data[TIMER_S]!!.sc
				timerDown.screenMls.text = data[TIMER_S]!!.ms
			}
		}

	}

	private fun firstTimerOn() {
		with(binding.timerUp) {
			screenMin.setActive()
			screenSec.setActive()
			screenMls.setActive()
		}
	}

	private fun firstTimerOff() {
		with(binding.timerUp) {
			screenMin.setDisable()
			screenSec.setDisable()
			screenMls.setDisable()
		}
	}

	private fun secondTimerOn() {
		with(binding.timerDown) {
			screenMin.setActive()
			screenSec.setActive()
			screenMls.setActive()
		}
	}

	private fun secondTimerOff() {
		with(binding.timerDown) {
			screenMin.setDisable()
			screenSec.setDisable()
			screenMls.setDisable()
		}
	}

	private fun TextView.setActive() {
		this.setTextColor(Color.RED)
	}

	private fun TextView.setDisable() {
		this.setTextColor(Color.WHITE)
	}


	//endregion

}