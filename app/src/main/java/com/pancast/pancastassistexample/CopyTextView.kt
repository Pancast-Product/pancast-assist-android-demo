package com.pancast.pancastassistexample

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.util.AttributeSet
import android.widget.Toast


class CopyTextView : androidx.appcompat.widget.AppCompatTextView {
    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        setOnLongClickListener {
            val cManager: ClipboardManager =
                context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val cData = ClipData.newPlainText("text", text)
            cManager.setPrimaryClip(cData)
            Toast.makeText(context, "Copied to clipboard", Toast.LENGTH_SHORT).show()
            true
        }
    }

    constructor(context: Context, attrs: AttributeSet) : this(context, attrs, 0) {}
}