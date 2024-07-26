package com.example.apitestcompose

import android.content.Context
import android.graphics.Bitmap
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import java.io.ByteArrayInputStream
import java.io.InputStream


data class UserName(
    var username: String,
    val password: String,
)

data class TaskSchema(
    var username:String,
    var description:String,
    var title:String ,
    var karma:Int,
)

data class UpdateSchema(
    var username:String,
    var description:String,
    var title:String ,
    var karma:Int,
    var taskid: Int
)
data class TaskResponse(
    var taskid:Int,
    var username:String,
    var description:String,
    var title:String ,
    var isreserved:Boolean,
    var underinspection:Boolean ,
    var karma:Int,
    var reservename:String,
    var isedited:Boolean
)

data class HallResponse(
    var fameid:Int,
    var owner:String,
    var description:String,
    var title:String ,
    var karma:Int,
    var volunteer:String,
    )

data class UserResponse(
    var userId: Int,
    var username: String,
    val password: String,
    val karma: Int,
)
data class ReserveBody(
    var taskid: Int,
    var reservename: String
)

interface ChaseDeuxInterface {

    @POST("/adduser")
    suspend fun getUser(@Body userBody: UserName) : Response<UserResponse>

    @POST("/login")
    suspend fun loginCheck(@Body username:UserName) : Response<UserResponse>

    @POST("/reserve")
    suspend fun reserveTask(@Body reserveBody: ReserveBody) : Response<TaskResponse>

    @POST("/complete")
    suspend fun completeTask(@Body taskResponse: TaskResponse) : Response<String>

    @POST("/submit")
    suspend fun submitTask(@Body reserveBody: ReserveBody) : Response<String>

    @GET("/karma/{username}")
    suspend fun getKarma(@Path("username") username: String) : Response<Int>

    @GET("/karmaadd/{username}")
    suspend fun addKarma(@Path("username") username: String) : Response<Int>


    @GET("/othertask/{username}")
    suspend fun getOtherTasks(@Path("username") username: String) : Response<List<TaskResponse>>

    @GET("/yourtask/{username}")
    suspend fun getYourTasks(@Path("username") username: String) : Response<List<TaskResponse>>


    @GET("/task2/{username}")
    suspend fun getTasks(@Path("username") username: String) : Response<List<TaskResponse>>

    @GET("/HOF/{username}")
    suspend fun hallOfFame(@Path("username") username: String) : Response<List<HallResponse>>

    @POST("/addtask")
    suspend fun addTask(@Body task: TaskSchema) : Response<TaskResponse>
    @POST("/updatetask")
    suspend fun updateTask(@Body task: UpdateSchema) : Response<String>
}
object RetrofitInstance {

    var urlText:String = "http://localhost:8000/"
    var gson: Gson = GsonBuilder()
        .setLenient()
        .create()

    val api:ChaseDeuxInterface by lazy {
        Retrofit.Builder().baseUrl(urlText)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ChaseDeuxInterface::class.java)

    }
}
