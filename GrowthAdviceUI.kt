
import android.content.Context
import android.graphics.pdf.PdfDocument
import android.os.Environment
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.io.File
import java.io.FileOutputStream

data class Prescription(
    val medicine: String,
    val dose: String? = null,
    val note: String? = null
)

data class GrowthAdvice(
    val condition: String,
    val label: String,
    val adviceSummary: String,
    val nutritionAdvice: List<String>,
    val medicalActions: List<String>,
    val prescriptions: List<Prescription>
)

@Composable
fun GrowthAdviceCard(advice: GrowthAdvice) {
    val context = LocalContext.current
    Card(
        modifier = Modifier.padding(16.dp).fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = advice.label, fontSize = 20.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = advice.adviceSummary)

            Spacer(modifier = Modifier.height(12.dp))
            Text("Nutrition Advice")
            advice.nutritionAdvice.forEach { Text("• $it") }

            Spacer(modifier = Modifier.height(12.dp))
            Text("Medical Actions")
            advice.medicalActions.forEach { Text("• $it") }

            Spacer(modifier = Modifier.height(12.dp))
            if (advice.prescriptions.isNotEmpty()) {
                Text("Prescriptions")
                advice.prescriptions.forEach {
                    Text("• ${it.medicine} - ${it.dose ?: it.note ?: "As directed"}")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { generatePdf(context, advice) }) {
                Text("Export to PDF")
            }
        }
    }
}

fun generatePdf(context: Context, advice: GrowthAdvice) {
    val document = PdfDocument()
    val pageInfo = PdfDocument.PageInfo.Builder(300, 600, 1).create()
    val page = document.startPage(pageInfo)
    val canvas = page.canvas

    var y = 20
    fun drawLine(text: String) {
        canvas.drawText(text, 10f, y.toFloat(), android.graphics.Paint().apply {
            textSize = 12f
        })
        y += 20
    }

    drawLine("Child Growth Assessment Report")
    drawLine("Label: ${advice.label}")
    drawLine("Summary: ${advice.adviceSummary}")
    drawLine("--- Nutrition Advice ---")
    advice.nutritionAdvice.forEach { drawLine("• $it") }
    drawLine("--- Medical Actions ---")
    advice.medicalActions.forEach { drawLine("• $it") }
    drawLine("--- Prescriptions ---")
    advice.prescriptions.forEach {
        drawLine("• ${it.medicine} - ${it.dose ?: it.note ?: "As directed"}")
    }

    document.finishPage(page)
    val dir = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
    val file = File(dir, "GrowthAdviceReport.pdf")
    document.writeTo(FileOutputStream(file))
    document.close()
}
