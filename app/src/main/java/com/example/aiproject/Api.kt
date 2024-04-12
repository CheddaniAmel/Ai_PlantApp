package com.example.aiproject

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface Api {
@POST("getIssue/")
fun getIssue(@Body request:Request):Call<Responce>
}