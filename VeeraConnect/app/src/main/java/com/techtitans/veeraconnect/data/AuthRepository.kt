package com.techtitans.veeraconnect.data

import com.techtitans.veeraconnect.util.Resource

/** Repository pattern: the UI depends on this interface, not on Firebase. */
interface AuthRepository {
    fun isLoggedIn(): Boolean
    fun currentEmail(): String?
    /** Returns true in Success if the user has the admin custom claim. */
    suspend fun login(email: String, password: String): Resource<Boolean>
    suspend fun register(name: String, email: String, password: String): Resource<Unit>
    fun logout()
}
