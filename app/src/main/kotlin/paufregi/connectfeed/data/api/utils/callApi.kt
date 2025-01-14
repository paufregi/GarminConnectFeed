package paufregi.connectfeed.data.api.utils

import paufregi.connectfeed.core.models.Result
import retrofit2.Response

suspend fun <T, R>callApi(block: suspend () -> Response<T>, transform: (Response<T>) -> R): Result<R> {
    return runCatching { block() }
        .map{ res ->
            when(res.isSuccessful) {
                true -> Result.Success(transform(res))
                false -> Result.Failure(res.errorBody()?.string() ?: "no errorBody")
            }
        }
        .getOrElse { err ->
            Result.Failure(err.message ?: "Unknown error")
        }
}