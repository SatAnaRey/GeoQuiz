package com.example.geoquiz

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.geoquiz.ui.theme.GeoQuizTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GeoQuizTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    QuizScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun QuizScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    var currentIndex by remember { mutableIntStateOf(0) }
    var correctCount by remember { mutableIntStateOf(0) }
    var answered by remember { mutableStateOf(false) }
    var finished by remember { mutableStateOf(false) }

    val isLast = currentIndex == questionBank.lastIndex
    val question = questionBank[currentIndex]

    fun checkAnswer(userAnswer: Boolean) {
        if (userAnswer == question.answer) correctCount++
        answered = true
        if (isLast) {
            finished = true
            Toast.makeText(
                context,
                "Правильных ответов: $correctCount из ${questionBank.size}",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = question.text,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(vertical = 24.dp)
        )

        if (!answered) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(onClick = { checkAnswer(true) }) { Text("TRUE") }
                Button(onClick = { checkAnswer(false) }) { Text("FALSE") }
            }
        }

        if (!finished) {
            Button(
                onClick = {
                    if (answered && !isLast) {
                        currentIndex++
                        answered = false
                    }
                },
                enabled = answered && !isLast,
                modifier = Modifier.padding(top = 24.dp)
            ) {
                Text("NEXT ›")
            }
        } else {
            Text(
                text = "Тест завершён. Правильных ответов: $correctCount из ${questionBank.size}",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(top = 24.dp)
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun QuizScreenPreview() {
    GeoQuizTheme {
        QuizScreen()
    }
}