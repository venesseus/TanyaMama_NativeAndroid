package com.example.tanyamama_native.data.auth

data class User(
    val username: String,
    val password: String
)

class AuthenticationManager{
    private val registeredUsers: MutableList<User> = mutableListOf()

    fun register(username: String, password: String) {
        val user = User(username, password)
        registeredUsers.add(user)
        println("User registered successfully!")
    }

    fun login(username: String, password: String): Boolean {
        val user = registeredUsers.find { it.username == username }
        if (user != null && user.password == password) {
            println("Login successful!")
            return true
        }
        println("Invalid credentials. Please try again.")
        return false
    }
}