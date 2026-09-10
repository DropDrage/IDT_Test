package com.dropdrage.idt_test.common.ui.utils

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.platform.LocalContext

sealed interface TextResource {

    @Composable
    @ReadOnlyComposable
    fun getText(): String


    private data class StringResource(private val text: String) : TextResource {
        @Composable
        @ReadOnlyComposable
        override fun getText(): String = text
    }

    private data class IdResource(@StringRes private val textId: Int) : TextResource {
        @Composable
        @ReadOnlyComposable
        override fun getText(): String = LocalContext.current.getString(textId)
    }

    private data class IdWithArgsResource(
        @StringRes private val textId: Int,
        private val args: Array<out Any>,
    ) : TextResource {
        @Composable
        @ReadOnlyComposable
        override fun getText(): String = LocalContext.current.getString(textId, *args)
    }


    companion object {
        fun string(text: String): TextResource = StringResource(text)
        fun id(@StringRes textId: Int): TextResource = IdResource(textId)
        fun idWithArgs(@StringRes textId: Int, vararg args: Any): TextResource = IdWithArgsResource(textId, args)
    }
}
