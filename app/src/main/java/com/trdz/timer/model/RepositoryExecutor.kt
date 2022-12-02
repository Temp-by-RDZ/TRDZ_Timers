package com.trdz.timer.model

import com.trdz.timer.base_utility.TIMER_F
import com.trdz.timer.base_utility.TIMER_S
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge

class RepositoryExecutor(): Repository {

	private val dataSource: List<DataSource> = listOf(DataSource(TIMER_F), DataSource(TIMER_S))

	//region Data update

	@ExperimentalCoroutinesApi
	override fun getData(timersState: List<Boolean>): Flow<List<DataTimer?>> {
		dataSource[TIMER_F].setState(timersState[TIMER_F])
		dataSource[TIMER_S].setState(timersState[TIMER_S])
		dataSource[TIMER_F].iniTime()
		dataSource[TIMER_S].iniTime()
		return merge(
			dataSource[TIMER_F].count().map { data -> responseMaker(data.first, data.second) },
			dataSource[TIMER_S].count().map { data -> responseMaker(data.first, data.second) })
	}

	override fun state(id: Int, state: Boolean) {
		dataSource[id].setState(state)
	}

	override fun refresh(id: Int) {
		dataSource[id].refresh()
	}

	private fun responseMaker(id: Int, data: Long): List<DataTimer?> {
		val list = mutableListOf<DataTimer?>(null, null)
		val result: List<StringBuilder> = listOf(StringBuilder(), StringBuilder(), StringBuilder())
		val ml = (data % 1000)
		if (ml < 10) result[2].append("00")
		else if (ml < 100) result[2].append("0")
		result[2].append(ml)
		var sc = data / 1000
		val mn = sc / 60
		sc %= 60
		if (sc < 10) result[1].append("0")
		result[1].append(sc)
		if (mn < 10) result[0].append("0")
		result[0].append(mn)
		list[id] = DataTimer(result[0].toString(), result[1].toString(), result[2].toString())
		return list
	}

	//endregion


}
