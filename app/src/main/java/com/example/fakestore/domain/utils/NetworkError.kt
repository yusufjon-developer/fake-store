package com.example.fakestore.domain.utils

sealed class NetworkError {
    class Unexpected(val reason: String) : NetworkError()
    class Api(val reason: String, val code: Int) : NetworkError()
    object NoInternetConnection : NetworkError()
    object ServerIsNotAvailable : NetworkError()
    class ResponseDeserialization(val message: String) : NetworkError()

    override fun toString(): String {
        return when (this) {
            is NoInternetConnection -> "Нет подключения к интернету"
            is ServerIsNotAvailable -> "Сервер недоступен"
            is Api -> this.reason
            is Unexpected -> this.reason
            is ResponseDeserialization -> this.message
        }
    }
}