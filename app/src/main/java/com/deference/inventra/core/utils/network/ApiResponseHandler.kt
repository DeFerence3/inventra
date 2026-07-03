package com.deference.inventra.core.utils.network

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import retrofit2.Response

object ApiResponseHandler {

    private val json = Json {
        ignoreUnknownKeys = true
    }

    /*fun <T> handleErrorResponse(retrofitResponse: Response<CommonResponse<T>>): RequestState.Error {
        val errorBody = retrofitResponse.errorBody()?.string()
        val errorMessage = if (errorBody != null){
            try {
                val responseJson = Json.parseToJsonElement(errorBody).jsonObject
                when (retrofitResponse.code()) {
                    401 -> if (responseJson["message"] == null) "Unauthorized Access" else responseJson["message"]!!.jsonPrimitive.content
                    500 -> json.decodeFromString<ErrorResponse>(errorBody).message ?: ""
                    700 -> json.decodeFromString<Response700>(errorBody).statusDescription
                    else -> "Request Failed with code:${retrofitResponse.code()}"
                }
            } catch (e: Exception) {
                "Request Failed with code:${retrofitResponse.code()}"
            }
        }else "Request Failed with code:${retrofitResponse.code()}"

        return RequestState.Error(errorMessage)
    }

    fun <T> handlePagedErrorResponse(retrofitResponse: Response<PagedCommonResponse<T>>): RequestState.Error {
        val errorBody = retrofitResponse.errorBody()?.string().orEmpty()

        var errorResponse: PagedCommonResponse<*>? = retrofitResponse.body()

        try {
            errorResponse = json.decodeFromString<PagedCommonResponse<*>>(errorBody)
        } catch (e: Exception) {
            e.printStackTrace()
        }


        val errorMessage = try {
            when (retrofitResponse.code()) {
                401 -> if (errorResponse?.status == "") "Unauthorized Access" else errorResponse?.status
                429 -> if (errorResponse?.status == "") "Too Many Requests" else errorResponse?.status
                700 -> json.decodeFromString<Response700>(errorBody).statusDescription
                else -> "HTTP Request Failed:${retrofitResponse.message()}"
            } ?: "HTTP Request Failed:${retrofitResponse.message()}"
        } catch (e: Exception) {
            "HTTP Request Failed:${retrofitResponse.message()}"
        }

        return RequestState.Error(errorMessage)
    }*/

    fun getApiErrors(response: Response<*>): RequestState.Error {
        val message = if (!response.isSuccessful) {
            val errorBody = response.errorBody()?.string()
            val statusCode = response.code()
            val statusMessage = response.message()
            if (!errorBody.isNullOrEmpty()) {
                try {
                    val json = Json.parseToJsonElement(errorBody).jsonObject
                    if (json["errors"] != null) {
                        val errorsObject = json["errors"]!!.jsonObject
                        val errorMessages = mutableListOf<String>()
                        errorsObject.forEach { (field, value) ->
                            value.jsonArray.forEach { error ->
                                errorMessages.add(
                                    "$field: ${error.jsonPrimitive.content}"
                                )
                            }
                        }
                        errorMessages.joinToString("\n")
                    }
                    else if (json["error"] != null) {
                        val errorsObject = json["error"]?.jsonPrimitive
                        errorsObject?.content ?: ""
                    }

                    else if (json["title"] != null) {

                        json["title"]!!.jsonPrimitive.content
                    }

                    else if (json["message"] != null) {
                        val message = json["message"]!!.jsonPrimitive.content
                        val statusDescription =
                            json["statusDescription"]
                                ?.jsonPrimitive
                                ?.content
                        if (statusDescription != null) {
                            "$message: $statusDescription"
                        } else {
                            message
                        }
                    } else {
                        "Error: HTTP $statusCode: $statusMessage"
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    "Unable to parse error response: HTTP $statusCode: $statusMessage"
                }
            } else {
                "HTTP $statusCode: $statusMessage"
            }
        } else {
            "Unexpected error: successful response with no content."
        }
        return RequestState.Error(message)
    }
}