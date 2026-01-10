package domain.result

typealias RootError = Error

sealed interface Result<out D, out E : RootError> {
   data class Success<out D , out E : RootError>(val data: D) : Result<D, E>
   data class Error<out D , out E : RootError>(val error: E) : Result<D, E>

   suspend fun onSuccess(block: suspend (D) -> Unit): Result<D, E> {
      if (this is Success) block(data)
      return this
   }

   suspend fun onError(block: suspend (E) -> Unit): Result<D, E> {
      if (this is Error) block(error)
      return this
   }

   fun onFinally(block: () -> Unit): Result<D, E> {
      // Always call the block, regardless of success or error
      block()
      return this
   }

}

