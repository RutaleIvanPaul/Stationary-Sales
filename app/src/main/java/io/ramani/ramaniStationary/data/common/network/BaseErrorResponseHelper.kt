package io.ramani.ramaniStationary.data.common.network

import com.google.gson.Gson
import io.ramani.ramaniStationary.domain.entities.BaseErrorResponse
import okhttp3.MediaType
import okhttp3.ResponseBody
import retrofit2.HttpException
import retrofit2.Response

/**
 * Created by Amr on 10/5/17.
 */
inline fun <reified T : BaseErrorResponse<*>> ResponseBody?.toErrorResponseModel(): T? {
    val gson = Gson()
    return gson.fromJson(this?.string(), T::class.java)
}

inline fun <reified T : BaseErrorResponse<*>> HttpException.toErrorResponseModel(): T? =
    response().errorBody().toErrorResponseModel()

fun <T> BaseErrorResponse.Companion.createHttpException(
    errorCode: Int,
    errorMessage: String,
    data: T? = null
) =
    HttpException(
        Response.error<BaseErrorResponse<Any>>(
            errorCode,
            ResponseBody.create(
                MediaType.parse("text"),
                BaseErrorResponse(500, errorMessage, data).toString()
            )
        )
    )
