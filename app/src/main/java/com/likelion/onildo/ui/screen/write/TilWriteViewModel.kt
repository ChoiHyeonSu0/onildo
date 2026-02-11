package com.likelion.onildo.ui.screen.write

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.likelion.onildo.data.local.TilEntity
import com.likelion.onildo.data.remote.OpenAIService
import com.likelion.onildo.data.repository.TilRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TilWriteViewModel @Inject constructor(
    private val repository: TilRepository,
    private val openAIService: OpenAIService
) : ViewModel() {

    private val _uiState = MutableStateFlow(WriteUiState())
    val uiState: StateFlow<WriteUiState> = _uiState.asStateFlow()

    fun saveTil(
        title: String,
        learned: String,
        difficulty: String,
        tomorrow: String
    ) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            try {
                // AI 분석 수행
                val analysis = openAIService.analyzeTil(
                    title = title,
                    learned = learned,
                    difficulty = difficulty.ifBlank { null },
                    tomorrow = tomorrow.ifBlank { null }
                )

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
}

data class WriteUiState(
    val isLoading: Boolean = false,
    val isSaved: Boolean = false,
    val error: String? = null
)