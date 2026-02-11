package com.likelion.onildo.ui.screen.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.likelion.onildo.data.local.TilEntity
import com.likelion.onildo.data.repository.TilRepository
import com.likelion.onildo.ui.screen.home.TilListState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface TilDetailState {
    data object Loading: TilDetailState
    data class Success(val til: TilEntity): TilDetailState
    data class Error(val error: String): TilDetailState
}

sealed class DetailUiEvent {
    object NavigateBack : DetailUiEvent()
}

@HiltViewModel
class TilDetailViewModel @Inject constructor(
    private val repository: TilRepository,
    savedStateHandle: SavedStateHandle
): ViewModel() {
    private val _tilDetailState = MutableStateFlow<TilDetailState>(TilDetailState.Loading)
    val tilDetailState: StateFlow<TilDetailState> = _tilDetailState
    private val _uiEvent = MutableSharedFlow<DetailUiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    val tilId: Long = checkNotNull(savedStateHandle["tilId"])

    fun getTilById(id: Long) {
        viewModelScope.launch {
            _tilDetailState.value = TilDetailState.Loading
            repository.getTilById(id)
                .onSuccess { til ->
                    if(til != null) _tilDetailState.value = TilDetailState.Success(til)
                    else _tilDetailState.value = TilDetailState.Error("잘못된 TIL 형식입니다.")
                }
                .onFailure {
                    _tilDetailState.value = TilDetailState.Error("데이터 로딩 중 오류 발생")
                }
        }
    }

    fun deleteTil(til: TilEntity) {
        viewModelScope.launch {
            repository.deleteTil(til)
                .onSuccess {
                    _uiEvent.emit(DetailUiEvent.NavigateBack)
                }
                .onFailure {
                    _tilDetailState.value = TilDetailState.Error("TIL 삭제 중 오류 발생")
                }
        }
    }
}