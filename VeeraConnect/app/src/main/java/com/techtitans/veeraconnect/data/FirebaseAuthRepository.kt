package com.techtitans.veeraconnect.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.userProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.techtitans.veeraconnect.util.Resource
import kotlinx.coroutines.tasks.await

class FirebaseAuthRepository(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
) : AuthRepository {

    override fun isLoggedIn() = auth.currentUser != null
    override fun currentEmail() = auth.currentUser?.email

    override suspend fun login(email: String, password: String): Resource<Boolean> = try {
        auth.signInWithEmailAndPassword(email, password).await()
        // Admin role is set as a custom claim on the server (see security section of Task 1)
        val token = auth.currentUser?.getIdToken(true)?.await()
        Resource.Success(token?.claims?.get("admin") == true)
    } catch (e: Exception) {
        Resource.Error(friendly(e))
    }

    override suspend fun register(name: String, email: String, password: String): Resource<Unit> = try {
        val result = auth.createUserWithEmailAndPassword(email, password).await()
        val user = result.user!!
        user.updateProfile(userProfileChangeRequest { displayName = name }).await()
        db.collection("users").document(user.uid).set(
            mapOf(
                "name" to name,
                "email" to email,
                "role" to "CUSTOMER",
                "theme" to "LIGHT",
                "createdAt" to com.google.firebase.Timestamp.now()
            )
        ).await()
        Resource.Success(Unit)
    } catch (e: Exception) {
        Resource.Error(friendly(e))
    }

    override fun logout() = auth.signOut()

    private fun friendly(e: Exception): String = when ((e as? FirebaseAuthException)?.errorCode) {
        "ERROR_INVALID_CREDENTIAL", "ERROR_WRONG_PASSWORD", "ERROR_USER_NOT_FOUND" -> "Incorrect email or password"
        "ERROR_EMAIL_ALREADY_IN_USE" -> "An account with this email already exists"
        "ERROR_NETWORK_REQUEST_FAILED" -> "No internet connection"
        "ERROR_TOO_MANY_REQUESTS" -> "Too many attempts. Try again later"
        else -> e.localizedMessage ?: "Something went wrong. Please try again."
    }
}
