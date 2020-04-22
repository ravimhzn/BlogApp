package com.ravimhzn.blogapplication.util


data class Result<T>(
    var message: Event<String>? = null,
    var loading: Boolean = false,
    var data: Event<T>? = null
) {
    companion object {
        fun <T> error(message: String): Result<T> {
            return Result(message = Event(message), loading = false, data = null)
        }

        fun <T> loading(isLoading: Boolean): Result<T> {
            return Result(message = null, loading = isLoading, data = null)
        }

        fun <T> success(message: String? = null, data: T? = null): Result<T> {
            return Result(message = Event.messageEvent(message), loading = false, data = Event.dataEvent(data))
        }
    }

    override fun toString(): String {
        return "DataState(message=$message, loading=$loading, data=$data)"
    }
}
