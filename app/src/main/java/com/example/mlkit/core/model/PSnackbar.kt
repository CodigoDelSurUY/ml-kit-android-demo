package com.example.mlkit.core.model

import androidx.annotation.StringRes

sealed class PSnackbar {

    data class Resource(@StringRes val resId: Int) : PSnackbar()
    data class Text(val message: String?) : PSnackbar()
}
