package com.example.fakestore.data.core

import com.example.fakestore.domain.utils.Either
import com.example.fakestore.domain.utils.NetworkError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Response
import java.net.SocketTimeoutException
import java.net.UnknownHostException

abstract class RemoteRepository {
    protected fun <T, S> doNetworkRequest(
        request: suspend () -> Response<T>,
        mapper: (T) -> S
    ) = flow {
        request().let {
            when {
                it.isSuccessful -> {
                    val body = it.body()
                    if (body != null) {
                        emit(Either.Right(mapper(body)))
                    } else {
                        emit(Either.Left(NetworkError.Unexpected("Body is null")))
                    }
                }
                it.code() in 400..499 -> {
                    val message = "Ошибка клиента (${it.code()})"
                    emit(Either.Left(NetworkError.Api(message, it.code())))
                }
                it.code() in 500..599 -> {
                    emit(Either.Left(NetworkError.ServerIsNotAvailable))
                }
                else -> {
                    emit(Either.Left(NetworkError.Unexpected("Unknown Code: ${it.code()}")))
                }
            }
        }
    }.flowOn(Dispatchers.IO).catch { exception ->
        val message = exception.localizedMessage ?: "Error Occurred!"
        when (exception) {
            is UnknownHostException -> emit(Either.Left(NetworkError.NoInternetConnection))
            is SocketTimeoutException -> emit(Either.Left(NetworkError.ServerIsNotAvailable))
            else -> emit(Either.Left(NetworkError.Unexpected(message)))
        }
    }
}