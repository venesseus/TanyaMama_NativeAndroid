package com.example.mystoryappdicoding.data.repo

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.tanyamama_native.data.auth.AuthenticationManager
import com.example.tanyamama_native.data.auth.User
import com.example.tanyamama_native.utils.Event
import com.example.tanyamama_native.data.response.LoginResponse
import com.example.tanyamama_native.data.response.RegisterResponse
import com.example.tanyamama_native.ui.login.LoginResult
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuthRepo {

    private val login = MutableLiveData<LoginResponse>()

    private val _registerUser = MutableLiveData<RegisterResponse>()
    val registerUser: LiveData<RegisterResponse> = _registerUser

    private val _loginUser = MutableLiveData<LoginResult>()
    val loginUser: LiveData<LoginResult> = _loginUser

    private val _isEnabled = MutableLiveData<Boolean>()
    val isEnabled: LiveData<Boolean> = _isEnabled

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _regMessage = MutableLiveData<Event<String>>()
    val regMessage: LiveData<Event<String>>
        get() = _regMessage

    private val _logMessage = MutableLiveData<Event<String>>()
    val logMessage: LiveData<Event<String>>
        get() = _logMessage

    fun register(
        name: String,
        email: String,
        password: String,
    ): LiveData<RegisterResponse> {
        _isEnabled.value = false
        _isLoading.value = true



        AuthenticationManager.register(name, email, password)
            .enqueue(object : Callback<RegisterResponse> {
                override fun onResponse(
                    call: Call<RegisterResponse>,
                    response: Response<RegisterResponse>,
                ) {
                    _isEnabled.value = true
                    _isLoading.value = false
                    if (response.isSuccessful) {
                        _registerUser.postValue(response.body())
                    } else {
                        Log.e(TAG, "onFailure: ${response.message()}")
                        _regMessage.value = Event("")
                    }
                }

                override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                    Log.e(TAG, "onFailure: ${t.message}")
                }
            })
        return _registerUser
    }

    fun login(
        email: String,
        password: String,
    ): LiveData<LoginResponse> {
        _isEnabled.value = false
        _isLoading.value = true

        Log.e(TAG, "Hasil adalah $email")

        AuthenticationManager.login(email, password)
            .enqueue(object : Callback<LoginResponse> {

                override fun onResponse(
                    call: Call<LoginResponse>,
                    response: Response<LoginResponse>,
                ) {
                    _isEnabled.value = true
                    _isLoading.value = false
                    if (response.isSuccessful) {
                        response.body().let { login ->
                            login?.loginResult?.let {
                                _loginUser.value = LoginResult(it.name, it.userId, it.token)
                            }
                        }
                    } else {
                        Log.e(TAG, "onFailure: ${response.message()}")
                        _logMessage.value = Event("")
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    Log.e(TAG, "onFailure: ${t.message}")
                }
            })
        return login
    }

    companion object {
        private const val TAG = "AuthRepository"
    }
}