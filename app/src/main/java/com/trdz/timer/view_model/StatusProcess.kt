package com.trdz.timer.view_model

import com.trdz.timer.model.DataTimer

sealed class StatusProcess {
	//Базовые команды
	object Loading: StatusProcess()
	data class Success(val data: List<DataTimer?>): StatusProcess()
	data class Error(val code: Int, val error: Throwable): StatusProcess()

	//Специфические команды
	data class State(val states: List<Boolean>): StatusProcess()

}