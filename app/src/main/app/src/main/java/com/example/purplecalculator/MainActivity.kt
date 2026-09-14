package com.example.purplecalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PurpleCalculatorApp()
        }
    }
}

@Composable
fun PurpleCalculatorApp() {
    var display by remember { mutableStateOf("0") }
    var operand1 by remember { mutableStateOf<Double?>(null) }
    var pendingOperation by remember { mutableStateOf<String?>(null) }
    var isNewInput by remember { mutableStateOf(true) }

    val darkBackground = Color(0xFF121212)
    val buttonBackground = Color(0xFF1E1E2E)
    val purpleAccent = Color(0xFFBB86FC)
    val textWhite = Color(0xFFEEEEEE)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(darkBackground)
            .padding(16.dp),
        verticalArrangement = Arrangement.Bottom
    ) {
        // Дисплей
        Text(
            text = display,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 32.dp, horizontal = 16.dp),
            color = textWhite,
            fontSize = 48.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.End,
            maxLines = 1
        )

        // Кнопки
        val buttons = listOf(
            listOf("C", "(", ")", "/"),
            listOf("7", "8", "9", "*"),
            listOf("4", "5", "6", "-"),
            listOf("1", "2", "3", "+"),
            listOf("0", ".", "⌫", "=")
        )

        for (row in buttons) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                for (symbol in row) {
                    val isAccent = symbol in listOf("/", "*", "-", "+", "=")
                    val bgColor = if (isAccent) purpleAccent else buttonBackground
                    val textColor = if (isAccent) Color.Black else textWhite

                    Button(
                        onClick = {
                            when (symbol) {
                                in "0".."9", "." -> {
                                    if (isNewInput || display == "0") {
                                        display = symbol
                                        isNewInput = false
                                    } else {
                                        display += symbol
                                    }
                                }
                                "C" -> {
                                    display = "0"
                                    operand1 = null
                                    pendingOperation = null
                                    isNewInput = true
                                }
                                "⌫" -> {
                                    if (display.length > 1) {
                                        display = display.dropLast(1)
                                    } else {
                                        display = "0"
                                        isNewInput = true
                                    }
                                }
                                "+", "-", "*", "/" -> {
                                    operand1 = display.toDoubleOrNull()
                                    pendingOperation = symbol
                                    isNewInput = true
                                }
                                "=" -> {
                                    val op1 = operand1
                                    val op2 = display.toDoubleOrNull()
                                    if (op1 != null && op2 != null && pendingOperation != null) {
                                        val result = when (pendingOperation) {
                                            "+" -> op1 + op2
                                            "-" -> op1 - op2
                                            "*" -> op1 * op2
                                            "/" -> if (op2 != 0.0) op1 / op2 else "Error"
                                            else -> op2
                                        }
                                        display = if (result is Double && result % 1.0 == 0.0) {
                                            result.toLong().toString()
                                        } else {
                                            result.toString()
                                        }
                                        operand1 = null
                                        pendingOperation = null
                                        isNewInput = true
                                    }
                                }
                            }
                        },
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = bgColor)
                    ) {
                        Text(
                            text = symbol,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = textColor
                        )
                    }
                }
            }
        }
    }
}
