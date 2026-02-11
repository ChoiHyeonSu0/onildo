package com.likelion.onildo.ui.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.likelion.onildo.data.local.TilEntity
import com.likelion.onildo.data.repository.TilRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface TilListState {
    data object Loading: TilListState
    data class Success(val tilList: Flow<List<TilEntity>>): TilListState
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
                .onSuccess { tils ->
                    _tilListState.value = TilListState.Success(tils)
                }
                .onFailure {
                    _tilListState.value = TilListState.Error("데이터 로딩 중 오류 발생")
                }
        }
    }

    fun deleteTil(til: TilEntity) {
        viewModelScope.launch {
            repository.deleteTil(til)
        }
    }
}