package com.likelion.onildo.ui.screen.write

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.likelion.onildo.data.local.TilEntity
import com.likelion.onildo.data.repository.TilRepository
import com.likelion.onildo.ui.screen.detail.TilDetailState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TilWriteViewModel @Inject constructor(
    private val repository: TilRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _tilDetailState = MutableStateFlow<TilDetailState>(TilDetailState.Loading)
    val tilDetailState: StateFlow<TilDetailState> = _tilDetailState

    private val _uiState = MutableStateFlow(WriteUiState())
    val uiState: StateFlow<WriteUiState> = _uiState.asStateFlow()

    var title by mutableStateOf("")
        private set
    var learned by mutableStateOf("")
        private set
    var difficulty by mutableStateOf("")
        private set
    var tomorrow by mutableStateOf("")
        private set
    var createdAt by mutableStateOf(0L)
        private set

   //  val tilId: Long? = savedStateHandle.get<Long>("tilId")
    private val _tilId: Long = savedStateHandle.get<Long>("tilId") ?: -1L
    val tilId: Long? = if (_tilId == -1L) null else _tilId

    fun onTitleChange(value: String) {
        title = value
    }

    fun onLearnedChange(value: String) {
        learned = value
    }

    fun onDifficultyChange(value: String) {
        difficulty = value
    }

    fun onTomorrowChange(value: String) {
        tomorrow = value
        Log.d("til", tomorrow)
    }

    fun saveOrUpdateTil(
        title: String,
        learned: String,
        difficulty: String,
        tomorrow: String
    ) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            try {
                // AI 분석 수행
                val analysis = repository.analyzeTil(
                    title = title,
                    learned = learned,
                    difficulty = difficulty.ifBlank { null },
                    tomorrow = tomorrow.ifBlank { null }
                ).getOrThrow()

                // Update Til
                if(_tilDetailState.value is TilDetailState.Success) {
                    val til = TilEntity(
                        id = _tilId,
                        title = title,
                        learned = learned,
                        difficulty = difficulty.ifBlank { null },
                        tomorrow = tomorrow.ifBlank { null },
                        emotion = analysis.emotion,
                        emotionScore = analysis.emotionScore,
                        difficultyLevel = analysis.difficultyLevel,
                        aiComment = analysis.comment,
                        createdAt = createdAt,
                        updatedAt = System.currentTimeMillis()
                    )
                    repository.updateTil(til)
                }

                // Insert Til
                else {
                    // TIL 저장
                    val til = TilEntity(
                        title = title,
                        learned = learned,
                        difficulty = difficulty.ifBlank { null },
                        tomorrow = tomorrow.ifBlank { null },
                        emotion = analysis.emotion,
                        emotionScore = analysis.emotionScore,
                        difficultyLevel = analysis.difficultyLevel,
                        aiComment = analysis.comment,
                    )
                    repository.insertTil(til)
                }

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isSaved = true
                )

            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "분석 중 오류가 발생했습니다: ${e.message}"
                )
            }
        }
    }

    fun getTilById(id: Long) {
        viewModelScope.launch {
            _tilDetailState.value = TilDetailState.Loading
            repository.getTilById(id)
                .onSuccess { til ->
                    if(til != null) {
                        title = til.title
                        learned = til.learned
                        difficulty = til.difficulty ?: ""
                        tomorrow = til.tomorrow ?: ""
                        createdAt = til.createdAt
                        _tilDetailState.value = TilDetailState.Success(til)
                    }
                    else _tilDetailState.value = TilDetailState.Error("잘못된 TIL 형식입니다.")
                }
                .onFailure {
                    _tilDetailState.value = TilDetailState.Error("데이터 로딩 중 오류 발생")
                }
        }
    }
}

data class WriteUiState(
    val isLoading: Boolean = false,
    val isSaved: Boolean = false,
    val error: String? = null
)