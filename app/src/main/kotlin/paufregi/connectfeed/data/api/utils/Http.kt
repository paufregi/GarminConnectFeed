package paufregi.connectfeed.data.api.utils

import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody

fun authRequest(request: Request, accessToken: String?): Request =
    request.newBuilder()
        .header("Authorization", "Bearer $accessToken")
        .build()

fun failedAuthResponse(request: Request, reason: String): Response =
    Response.Builder()
        .request(request)
        .protocol(Protocol.HTTP_1_1)
        .code(401)
        .message("Auth failed")
        .body(reason.toResponseBody())
        .build()