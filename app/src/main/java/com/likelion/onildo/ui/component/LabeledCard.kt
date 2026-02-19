package com.likelion.onildo.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.toRect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun LabeledCard(
    title: String,
    backgroundColor: Color = Color.White,
    borderColor: Color = Color.Black,
    content: @Composable ColumnScope.() -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 15.dp)
    ) {
        // 1. 메인 카드 본체 (테두리를 직접 그리지 않고 내부에 배치)
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .innerShadow(
                blur = 1.dp,
                color = borderColor.copy(alpha = 0.2f),
                cornersRadius = 16.dp
            ),
            shape = RoundedCornerShape(16.dp),
            color = backgroundColor
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                content = content
            )
        }

        // 제목 레이블
        Box(
            modifier = Modifier
                .offset(x = 20.dp, y = (-12).dp) // 테두리 정중앙에 걸치도록
                .background(backgroundColor)   // 카드 배경색과 동일하게 맞춤
                .padding(horizontal = 6.dp) // 선을 가릴 만큼의 최소한의 여백

        ) {
            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.ExtraBold,
                color = borderColor
            )
        }
    }
}

fun Modifier.innerShadow(
    blur: Dp = 4.dp,
    color: Color = Color.Black.copy(alpha = 0.3f),
    cornersRadius: Dp = 16.dp,
    spread: Dp = 0.dp,
    offsetY: Dp = 2.dp,
    offsetX: Dp = 2.dp
) = drawWithContent {
    drawContent() // 실제 카드의 내용물을 먼저 그립니다.

    val rect = size.toRect()
    val paint = Paint().apply {
        this.color = color
        this.isAntiAlias = true
    }

    // 그림자가 바깥으로 나가지 않도록 카드 영역만 클리핑합니다.
    clipRect {
        val shadowOuterRect = rect.inflate(blur.toPx())
        val shadowInnerRect = rect.deflate(spread.toPx())

        // 그림자를 그리는 핵심 로직 (Path 연산 사용)
        val shadowPath = Path().apply {
            fillType = PathFillType.EvenOdd
            addRoundRect(RoundRect(shadowOuterRect, cornersRadius.toPx(), cornersRadius.toPx()))
            addRoundRect(RoundRect(shadowInnerRect, cornersRadius.toPx(), cornersRadius.toPx()))
        }

        // 그림자 위치 및 블러 적용
        paint.asFrameworkPaint().apply {
            maskFilter = android.graphics.BlurMaskFilter(blur.toPx(), android.graphics.BlurMaskFilter.Blur.NORMAL)
        }

        drawContext.canvas.translate(offsetX.toPx(), offsetY.toPx())
        drawContext.canvas.drawPath(shadowPath, paint)
        drawContext.canvas.translate(-offsetX.toPx(), -offsetY.toPx())
    }
}
