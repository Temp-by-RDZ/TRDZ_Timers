package com.trdz.timer.base_utility.di

import com.trdz.timer.R
import com.trdz.timer.view.Navigation
import org.koin.dsl.module

val moduleMainK = module {
	single<Navigation>() { Navigation(R.id.container_fragment_base) }
}


