package com.hae.haessentials.backend

import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface APICalls {
    @POST("login")
    fun sendOtpToLogin(@Body otpRequest: OtpRequest): Observable<Response<OtpResponse>>
}