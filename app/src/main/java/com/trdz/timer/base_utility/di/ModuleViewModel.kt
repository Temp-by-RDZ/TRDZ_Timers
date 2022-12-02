package com.trdz.timer.base_utility.di

import androidx.lifecycle.MutableLiveData
import com.trdz.timer.model.Repository
import com.trdz.timer.model.RepositoryExecutor
import com.trdz.timer.view_model.StatusProcess
import com.trdz.timer.view_model.ViewModelFactory
import org.koin.dsl.module

val moduleViewModelK = module {
	single<Repository>() { RepositoryExecutor() }
	single<MutableLiveData<StatusProcess>>() { MutableLiveData() }
	single<ViewModelFactory>() { ViewModelFactory(repository = get(), dataLive = get()) }
}


