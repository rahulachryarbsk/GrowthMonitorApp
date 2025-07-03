
package com.example.growthmonitor

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                GrowthInputScreen()
            }
        }
    }
}

@Composable
fun GrowthInputScreen() {
    var age by remember { mutableStateOf(0) }
    var weight by remember { mutableStateOf(0.0) }
    var height by remember { mutableStateOf(0.0) }
    var showAdvice by remember { mutableStateOf(false) }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Enter Child Data", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = age.toString(),
            onValueChange = { age = it.toIntOrNull() ?: 0 },
            label = { Text("Age in Months") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = weight.toString(),
            onValueChange = { weight = it.toDoubleOrNull() ?: 0.0 },
            label = { Text("Weight (kg)") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = height.toString(),
            onValueChange = { height = it.toDoubleOrNull() ?: 0.0 },
            label = { Text("Height (cm)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { showAdvice = true }) {
            Text("Analyze Growth")
        }

        if (showAdvice) {
            val zScore = -3.2 // Placeholder for LMS logic
            val advice = getAdviceByZ(zScore)
            GrowthAdviceCard(advice)
        }
    }
}

fun getAdviceByZ(z: Double): GrowthAdvice {
    return if (z <= -3) {
        GrowthAdvice(
            condition = "Z <= -3",
            label = "Severe Undernutrition",
            adviceSummary = "Z-score indicates severe undernutrition.",
            nutritionAdvice = listOf("Energy-dense feeding", "Frequent meals", "Use local food"),
            medicalActions = listOf("Refer to pediatrician", "Deworming", "Check for deficiencies"),
            prescriptions = listOf(Prescription("Multivitamin", "5ml daily"))
        )
    } else {
        GrowthAdvice(
            condition = "Z Normal",
            label = "Normal Growth",
            adviceSummary = "Child's growth is within normal range.",
            nutritionAdvice = listOf("Continue healthy diet"),
            medicalActions = listOf("Routine monitoring"),
            prescriptions = emptyList()
        )
    }
}
