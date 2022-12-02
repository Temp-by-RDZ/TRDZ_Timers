package com.trdz.timer

import android.app.Application
import com.trdz.timer.base_utility.di.moduleMainK
import com.trdz.timer.base_utility.di.moduleViewModelK
import org.koin.core.context.startKoin

class MyApp: Application() {

	companion object {
		lateinit var instance: MyApp
	}

	override fun onCreate() {
		super.onCreate()
		instance = this
		startKoin {
			modules(listOf(moduleMainK, moduleViewModelK))
		}
	}

}