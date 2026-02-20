package com.likelion.onildo.ui.screen.stats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.likelion.onildo.data.repository.TilRepository
import com.patrykandpatrick.vico.core.entry.ChartEntryModel
import com.patrykandpatrick.vico.core.entry.entryModelOf
import com.patrykandpatrick.vico.core.entry.entryOf
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface StatsState {
    data object Loading: StatsState
    data class Success(val chartEntryModel: ChartEntryModel): StatsState
    data class Error(val error: String): StatsState
}


@HiltViewModel
class StatsViewModel @Inject constructor(
    private val repository: TilRepository
): ViewModel() {
    private val _statsState = MutableStateFlow<StatsState>(StatsState.Loading)
    val statsState: StateFlow<StatsState> = _statsState


    fun analyzeChart() {
        viewModelScope.launch {
            repository.getAllTils()
                .onSuccess { flowData ->
                    flowData.collect { value ->
                        // 차트 데이터 생성
                        val chartEntryModel = entryModelOf(
                            value.mapIndexed { index, til ->
                                entryOf(index.toFloat(), til.emotionScore?.toFloat() ?: 3f)
                            }
                        )
                        _statsState.value = StatsState.Success(chartEntryModel)
                    }

                }
                .onFailure { e ->
                    _statsState.value = StatsState.Error("차트 생성 중 오류 발생: $e")
                }
        }
    }
}