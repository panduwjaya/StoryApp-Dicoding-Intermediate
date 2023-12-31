package com.example.storyapp.data.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.storyapp.data.network.ApiService
import com.example.storyapp.data.response.detail.DetailListResponse
import com.example.storyapp.data.response.detail.DetailResponse
import com.example.storyapp.data.response.list.StoryListReponses
import com.example.storyapp.data.response.login.LoginResponse
import com.example.storyapp.data.response.register.RegisterResponse
import com.example.storyapp.data.response.upload.FileUploadResponse
import com.example.storyapp.data.token.TokenPreference
import com.example.storyapp.utils.Result
import com.google.gson.Gson
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.HttpException
import java.net.SocketTimeoutException

class StoryRepository(
    private val apiService: ApiService,
    private val tokenPreference: TokenPreference
) {
    companion object {
        @Volatile
        private var instance: StoryRepository? = null
        fun getInstance(
            apiService: ApiService,
            tokenPreference: TokenPreference
        ): StoryRepository =
            instance ?: synchronized(this) {
                instance ?: StoryRepository(apiService,tokenPreference)
            }.also { instance = it }
    }

    fun postRegister(name: String, email: String, password: String): LiveData<Result<RegisterResponse>> = liveData{
        emit(Result.Loading)
        try {
            //get success message
            val responseMessage = apiService.postRegister(name,email,password)
            emit(Result.Success(responseMessage))
        } catch (e: SocketTimeoutException){
            val errorMessage = "Koneksi ke server Gagal"
            emit(Result.Error(errorMessage))
        } catch (e: HttpException) {
            //get error message
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, LoginResponse::class.java)
            val errorMessage = errorBody.message
            emit(Result.Error(errorMessage))
        }
    }

    fun postLogin(email: String, password: String): LiveData<Result<LoginResponse>> = liveData{
        emit(Result.Loading)
        try {
            //get success message
            val responseMessage = apiService.postLogin(email,password)
            emit(Result.Success(responseMessage))
        } catch (e: SocketTimeoutException){
            val errorMessage = "Koneksi ke server Gagal"
            emit(Result.Error(errorMessage))
        } catch (e: HttpException) {
            //get error message
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, LoginResponse::class.java)
            val errorMessage = errorBody.message
            emit(Result.Error(errorMessage))
        }
    }

    fun postStory(file: MultipartBody.Part, description: RequestBody): LiveData<Result<FileUploadResponse>> = liveData {
        emit(Result.Loading)
        try {
            //get success message
            val responseMessage = apiService.postStory(file,description)
            emit(Result.Success(responseMessage))
        }catch (e: SocketTimeoutException){
            val errorMessage = "Koneksi ke server Gagal"
            emit(Result.Error(errorMessage))
        } catch (e: HttpException){
            //get error message
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, FileUploadResponse::class.java)
            val errorMessage = errorBody.message
            emit(Result.Error(errorMessage))
        }
    }

    fun getStory(): LiveData<Result<StoryListReponses>> = liveData{
        emit(Result.Loading)
        try {
            //get success message
            val responseMessage = apiService.getListStory()
            emit(Result.Success(responseMessage))
        } catch (e: SocketTimeoutException){
            val errorMessage = "Koneksi ke server Gagal"
            emit(Result.Error(errorMessage))
        } catch (e: HttpException){
            //get error message
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, StoryListReponses::class.java)
            val errorMessage = errorBody.message
            emit(Result.Error(errorMessage))
        }
    }


    fun getDetailStory(id: String): LiveData<Result<DetailResponse>> = liveData {
        emit(Result.Loading)
        try {
            //get success message
            val responseMessage = apiService.getStoryDetail(id)
            emit(Result.Success(responseMessage))
        } catch (e: SocketTimeoutException){
            val errorMessage = "Koneksi ke server Gagal"
            emit(Result.Error(errorMessage))
        } catch (e: HttpException){
            //get error message
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, DetailResponse::class.java)
            val errorMessage = errorBody.message
            emit(Result.Error(errorMessage))
        }
    }
}