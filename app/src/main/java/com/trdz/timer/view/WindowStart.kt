package com.trdz.timer.view

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.*
import androidx.fragment.app.Fragment
import com.trdz.timer.base_utility.EFFECT_RISE
import com.trdz.timer.databinding.FragmentWindowStartBinding
import org.koin.android.ext.android.inject

class WindowStart: Fragment() {

	//region Elements

	private var _binding: FragmentWindowStartBinding? = null
	private val binding get() = _binding!!

	//endregion

	//region Injected

	private val navigation: Navigation by inject()

	//endregion

	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
		_binding = FragmentWindowStartBinding.inflate(inflater, container, false)
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		binding.firstView.animate().apply {
			alpha(0.0f)
			duration = 900L
			withEndAction {
				if (this@WindowStart.isAdded) {
					requireActivity().supportFragmentManager.beginTransaction().detach(this@WindowStart).commit()
				}
			}
			start()
		}
		createMainWindow()
	}

	private fun createMainWindow() {
		Handler(Looper.getMainLooper()).postDelayed({
			navigation.replace(requireActivity().supportFragmentManager, WindowTimers(), false, effect = EFFECT_RISE)
		}, 100L)
	}

	companion object {
		fun newInstance() = WindowStart()
	}
}

