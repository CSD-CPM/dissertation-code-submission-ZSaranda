package com.example.gosti.session

import com.example.gosti.Model.UserModel

object UserSession {
    var user: UserModel? = null

    fun isLoggedIn(): Boolean = user != null

    fun logout() {
        user = null
    }
}
