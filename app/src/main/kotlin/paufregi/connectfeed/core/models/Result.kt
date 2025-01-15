package paufregi.connectfeed.core.models

sealed interface Result<T> {
    val isSuccessful: Boolean

    fun <R>fold(
        onSuccess: (data: T) -> R,
        onFailure: (reason: String) -> String
    ) = when(this) {
        is Success -> Success(onSuccess(data))
        is Failure -> Failure(onFailure(reason))
    }

    fun <R>map(transform: (data: T) -> R) = fold(transform) { it }

    suspend fun onSuccess(action: suspend (data: T) -> Unit): Result<T> {
        if (this is Success) action(data)
        return this
    }

    suspend fun onFailure(action: suspend (reason: String) -> Unit): Result<T> {
        if (this is Failure) action(reason)
        return this
    }


    class Success<T>(val data: T) : Result<T> {
        override val isSuccessful: Boolean
            get() = true
    }
    class Failure<T>(val reason: String) : Result<T>{
        override val isSuccessful: Boolean
            get() = false
    }
}