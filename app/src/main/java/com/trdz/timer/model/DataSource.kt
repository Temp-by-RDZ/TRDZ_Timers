package com.trdz.timer.model

import com.trdz.timer.base_utility.timeIni
import com.trdz.timer.base_utility.timeSet
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

internal class DataSource(
	private val id: Int,
	private var state: Boolean = false,
	private var currentTime: Long = 0,
	private var milliseconds: Long = 0,
	private val refreshIntervalMs: Long = 20,
) {

	fun iniTime() {
		currentTime = timeIni()
	}

	fun setState(state: Boolean) {
		this.state = state
	}

	fun count(): Flow<Pair<Int, Long>> = flow {
		while (true) {
			if (state) {
				val t = currentTime.timeSet()
				milliseconds += t
				iniTime()
				emit(id to milliseconds)
			}
			else iniTime()
			delay(refreshIntervalMs)
		}
	}
		.flowOn(Dispatchers.Default)
		.catch { e ->
			println(e.message)//Error!
		}

	fun refresh() {
		milliseconds = 0
	}
}