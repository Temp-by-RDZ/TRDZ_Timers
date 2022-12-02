package com.trdz.timer.model

import kotlinx.coroutines.flow.Flow

interface Repository {
	fun getData(timersState: List<Boolean>): Flow<List<DataTimer?>>
	fun state(id: Int, state: Boolean)
	fun refresh(id: Int)
}