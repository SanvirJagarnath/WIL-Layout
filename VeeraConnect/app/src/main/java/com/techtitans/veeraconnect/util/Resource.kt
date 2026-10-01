package com.techtitans.veeraconnect.util

/** Wraps a UI state so screens can show loading, success and error feedback. */
sealed class Resource<out T> {
    object Loading : Resource<Nothing>()
    data class Success<T>(val data: T) : Resource<T>()
    data class Error(val message: String) : Resource<Nothing>()
}
