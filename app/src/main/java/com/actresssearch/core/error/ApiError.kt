package com.actresssearch.core.error

/**
 * Domain-level error representation to standardize error handling across the app.
 */
sealed class ApiError : Exception() {
    object NetworkError : ApiError()
    object TimeoutError : ApiError()
    data class HttpError(val code: Int, override val message: String) : ApiError()
    object AuthenticationError : ApiError()
    object UnknownError : ApiError()

    fun getDisplayMessage(): String = when (this) {
        NetworkError -> "ネットワーク接続を確認してください"
        TimeoutError -> "通信がタイムアウトしました"
        is HttpError -> "サーバーエラーが発生しました（エラーコード: $code）"
        AuthenticationError -> "認証に失敗しました"
        UnknownError -> "予期しないエラーが発生しました"
    }
}
