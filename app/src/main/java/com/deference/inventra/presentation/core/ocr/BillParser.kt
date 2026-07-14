package com.deference.inventra.presentation.core.ocr

import com.deference.inventra.domain.model.ocr.ScannedBill
import com.deference.inventra.domain.model.ocr.ScannedBillItem

object BillParser {
    fun parse(text: String): ScannedBill {
        val lines = text.split("\n").map { it.trim() }.filter { it.isNotEmpty() }
        
        var supplierName = "Unknown Supplier"
        var invoiceNo = "TFG"
        var date = ""
        val items = mutableListOf<ScannedBillItem>()
        var totalAmount = 0.0

        val addressKeywords = listOf("street", "road", "ave", "building", "tel", "phone", "email", "web", "site", "tax", "vat", "trn", "invoice", "bill")
        for (line in lines) {
            val lower = line.lowercase()
            if (addressKeywords.any { lower.contains(it) }) continue
            if (line.matches(Regex(".*\\d{5,}.*"))) continue
            if (line.length < 3) continue
            supplierName = line
            break
        }

        val invRegex = Regex("(?i)(?:invoice\\s*(?:no|num|number)?|inv\\s*(?:no|num|number)?|bill\\s*(?:no|num|number)?)\\s*[:#\\s-]*\\s*([A-Za-z0-9-]+)")
        for (line in lines) {
            invRegex.find(line)?.let { match ->
                invoiceNo = match.groupValues[1]
            }
        }

        val dateRegex = Regex("(?i)(?:date)[:#\\s-]*\\s*(\\d{2}[-/.]\\d{2}[-/.]\\d{4}|\\d{4}[-/.]\\d{2}[-/.]\\d{2})")
        val simpleDateRegex = Regex("(\\d{2}[-/.]\\d{2}[-/.]\\d{4}|\\d{4}[-/.]\\d{2}[-/.]\\d{2})")
        for (line in lines) {
            dateRegex.find(line)?.let { match ->
                date = match.groupValues[1]
            } ?: simpleDateRegex.find(line)?.let { match ->
                if (date.isEmpty()) date = match.groupValues[1]
            }
        }

        val totalRegex = Regex("(?i)(?:total|grand\\s*total|net\\s*amount|gross\\s*amount|payable)[:#\\s-]*\\s*([0-9,]+\\.\\d{2})")
        for (line in lines) {
            totalRegex.find(line)?.let { match ->
                totalAmount = match.groupValues[1].replace(",", "").toDoubleOrNull() ?: totalAmount
            }
        }

        val numberRegex = Regex("([0-9,]+\\.\\d{2}|[0-9]+)")
        for (line in lines) {
            val lower = line.lowercase()
            if (lower.contains("total") || lower.contains("subtotal") || lower.contains("invoice") || lower.contains("tax") || lower.contains("trn") || lower.contains("date")) {
                continue
            }
            val matches = numberRegex.findAll(line).toList()
            if (matches.size >= 2) {
                val firstMatchIndex = matches.first().range.first
                val namePart = line.substring(0, firstMatchIndex).trim().trimEnd { it == '-' || it == ':' || it == '.' || it.isWhitespace() }
                if (namePart.length >= 3) {
                    val numbers = matches.map { it.value.replace(",", "").toDoubleOrNull() ?: 0.0 }
                    val qty = numbers.getOrNull(0) ?: 1.0
                    val rate = numbers.getOrNull(1) ?: 0.0
                    val tax = if (numbers.size >= 4) numbers.getOrNull(2) ?: 0.0 else (qty * rate * 0.05)
                    val gross = if (numbers.size >= 4) numbers.getOrNull(3) ?: (qty * rate + tax)
                                 else if (numbers.size == 3) numbers.getOrNull(2) ?: (qty * rate + tax)
                                 else (qty * rate + tax)
                    items.add(ScannedBillItem(name = namePart, qty = qty, rate = rate, tax = tax, gross = gross))
                }
            }
        }

        if (totalAmount == 0.0 && items.isNotEmpty()) {
            totalAmount = items.sumOf { it.gross }
        }

        return ScannedBill(
            supplierName = supplierName,
            invoiceNo = invoiceNo,
            date = date,
            items = items,
            totalAmount = totalAmount
        )
    }

    fun isBill(text: String): Boolean {
        val lower = text.lowercase()
        val keywords = listOf("invoice", "bill", "total", "qty", "amount", "rate", "subtotal", "tax invoice")
        val matchCount = keywords.count { lower.contains(it) }
        return matchCount >= 2
    }
}
