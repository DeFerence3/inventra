package com.deference.inventra.presentation.core.ocr

import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.TextRecognizer
import com.google.mlkit.vision.text.latin.TextRecognizerOptions

class MLkitImageAnalyzer(
    private val onTextDetected: ((String?) -> (Unit))?,
) : ImageAnalysis.Analyzer {

    private val textRecognizer: TextRecognizer by lazy {
        TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
    }

    @OptIn(ExperimentalGetImage::class)
    override fun analyze(imageProxy: ImageProxy) {
        if (!detectWhitePaper(imageProxy)) {
            imageProxy.close()
            return
        }

        imageProxy.image?.let { mediaImage ->
            val image = InputImage.fromMediaImage(
                mediaImage,
                imageProxy.imageInfo.rotationDegrees
            )
            textRecognizer.process(image)
                .addOnSuccessListener { visionText ->
                    onTextDetected?.invoke(visionText.text)
                }
                .addOnCompleteListener {
                    imageProxy.close()
                }
        } ?: imageProxy.close()
    }

    private fun detectWhitePaper(imageProxy: ImageProxy): Boolean {
        val image = imageProxy.image ?: return false
        val planes = image.planes
        if (planes.isEmpty()) return false
        val buffer = planes[0].buffer
        val rowStride = planes[0].rowStride
        val pixelStride = planes[0].pixelStride
        val width = imageProxy.width
        val height = imageProxy.height

        val startX = (width * 0.3).toInt()
        val endX = (width * 0.7).toInt()
        val startY = (height * 0.3).toInt()
        val endY = (height * 0.7).toInt()

        var centerSum = 0L
        var centerCount = 0

        val step = 16
        for (y in startY until endY step step) {
            for (x in startX until endX step step) {
                val index = y * rowStride + x * pixelStride
                if (index < buffer.remaining()) {
                    val value = buffer.get(index).toInt() and 0xFF
                    centerSum += value
                    centerCount++
                }
            }
        }

        if (centerCount == 0) return false
        val avgCenter = centerSum / centerCount

        var borderSum = 0L
        var borderCount = 0
        for (x in 0 until width step step) {
            val yTop = (height * 0.05).toInt()
            val indexTop = yTop * rowStride + x * pixelStride
            if (indexTop < buffer.remaining()) {
                borderSum += buffer.get(indexTop).toInt() and 0xFF
                borderCount++
            }
            val yBottom = (height * 0.95).toInt()
            val indexBottom = yBottom * rowStride + x * pixelStride
            if (indexBottom < buffer.remaining()) {
                borderSum += buffer.get(indexBottom).toInt() and 0xFF
                borderCount++
            }
        }

        if (borderCount == 0) return false
        val avgBorder = borderSum / borderCount

        return avgCenter > 150 && (avgCenter - avgBorder) > 20
    }
}