package com.deference.inventra.presentation.core.components.selectors

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.activity.result.contract.ActivityResultContract
import java.io.Serializable

class SelectionContract<T : Serializable>(
    private val selectionConst: String,
    private val head: String
) : ActivityResultContract<Unit, T?>() {
    override fun createIntent(context: Context, input: Unit): Intent {
        return Intent(context, SingleSelectionActivity::class.java).apply {
            putExtra("type", selectionConst)
            putExtra("head", head)
        }
    }

    override fun parseResult(resultCode: Int, intent: Intent?): T? {
        return if (resultCode == Activity.RESULT_OK) {
            @Suppress("UNCHECKED_CAST")
            intent?.serializable("item") as T?
        } else null
    }
}

private fun Intent.serializable(key: String): Serializable? {
    return if (Build.VERSION.SDK_INT >= 33) {
        getSerializableExtra(key, Serializable::class.java)
    } else {
        @Suppress("DEPRECATION")
        getSerializableExtra(key)
    }
}
