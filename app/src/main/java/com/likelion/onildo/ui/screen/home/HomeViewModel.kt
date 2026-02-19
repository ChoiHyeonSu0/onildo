package com.likelion.onildo.ui.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.likelion.onildo.data.local.TilEntity
import com.likelion.onildo.data.repository.TilRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import javax.inject.Inject


sealed interface TilListState {
    data object Loading: TilListState
    data class Success(val tilList: StateFlow<Map<String, List<TilEntity>>>): TilListState
    data class Error(val error: String): TilListState
}

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: TilRepository
): ViewModel() {
    private val _tilListState = MutableStateFlow<TilListState>(TilListState.Loading)
    val tilListState: StateFlow<TilListState> = _tilListState

    fun getTilList() {
        viewModelScope.launch {
            _tilListState.value = TilListState.Loading
            repository.getAllTils()
                .onSuccess { flowData ->
                    // 날짜 별
                    val groupedStateFlow = flowData
                        .map { list ->
                            list.groupBy { it.createdAt.setTilListByDate() }
                        }
                        .stateIn(
                            scope = viewModelScope,
                            started = SharingStarted.WhileSubscribed(5000),
                            initialValue = emptyMap()
                        )
                    _tilListState.value = TilListState.Success(groupedStateFlow)
                }
                .onFailure {
                    _tilListState.value = TilListState.Error("데이터 로딩 중 오류 발생")
                }
        }
    }

    fun Long.setTilListByDate(): String {
        // 작성 TIL 날짜 문자열 생성
        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val tilDay = formatter.format(Date(this))

        // 오늘, 어제 날짜 문자열 생성
        val today = formatter.format(Date(System.currentTimeMillis()))

        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, -1)
        val yesterday = formatter.format(calendar.time)

        if(tilDay == today) return "오늘"
        else if(tilDay == yesterday) return "어제"
        else return formatter.format(Date(this))
    }

    fun deleteTil(til: TilEntity) {
        viewModelScope.launch {
            repository.deleteTil(til)
        }
    }
}