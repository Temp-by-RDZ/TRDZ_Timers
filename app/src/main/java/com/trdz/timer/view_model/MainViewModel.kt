package com.trdz.timer.view_model

import androidx.lifecycle.*
import com.trdz.timer.model.DataTimer
import com.trdz.timer.model.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class MainViewModel(
	private val repository: Repository,
	private val dataLive: MutableLiveData<StatusProcess> = MutableLiveData(),
): ViewModel() {

	private var timersState = mutableListOf(false, false)

	//region Flow

	private var jobs: Job? = null

	fun getData(): LiveData<StatusProcess> = dataLive

	private fun active() {
		jobs = viewModelScope.launch {
			repository.getData(timersState).flowOn(Dispatchers.Main)
				.collect { data ->
					dataLive.value = StatusProcess.Success(data)
				}
		}
	}

	private fun stop() {
		jobs?.cancel()
	}

	override fun onCleared() {
		viewModelScope.cancel()
		super.onCleared()
	}

	//endregion

	fun use(id: Int) {
		timersState[id] = !timersState[id]
		dataLive.postValue(StatusProcess.State(timersState))
		if (timersState[id]) {
			if ((jobs?.isActive != true)) active()
			else repository.state(id, true)
		}
		else {
			if (!timersState.any { flag -> flag }) stop()
			else repository.state(id, false)
		}
	}


	fun refresh(id: Int) {
		val list = mutableListOf<DataTimer?>(null, null)
		list[id] = DataTimer()
		dataLive.value = StatusProcess.Success(list)
		repository.refresh(id)
	}


	fun restore() {
		dataLive.postValue(StatusProcess.State(timersState))
	}

}

class ViewModelFactory(
	private val repository: Repository,
	private val dataLive: MutableLiveData<StatusProcess>,
): ViewModelProvider.Factory {

	@Suppress("UNCHECKED_CAST")
	override fun <T: ViewModel> create(modelClass: Class<T>): T {
		return MainViewModel(repository, dataLive) as T
	}

}