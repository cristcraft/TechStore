package com.techstore.techstore.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.techstore.techstore.data.model.User
import com.techstore.techstore.data.model.UserRole
import com.techstore.techstore.util.Resource
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) {
    val currentUser get() = auth.currentUser

    suspend fun register(
        name: String, email: String, password: String, role: UserRole
    ): Resource<User> = try {
        val result = auth.createUserWithEmailAndPassword(email, password).await()
        val uid = result.user!!.uid
        val user = User(
            uid = uid, name = name, email = email,
            role = role.name, authProvider = "email"
        )
        firestore.collection("users").document(uid).set(user).await()
        Resource.Success(user)
    } catch (e: Exception) {
        Resource.Error(e.message ?: "Error al registrar")
    }

    suspend fun login(email: String, password: String): Resource<User> = try {
        val result = auth.signInWithEmailAndPassword(email, password).await()
        val uid = result.user!!.uid
        val snapshot = firestore.collection("users").document(uid).get().await()
        val user = snapshot.toObject(User::class.java)!!
        Resource.Success(user)
    } catch (e: Exception) {
        Resource.Error(e.message ?: "Credenciales incorrectas")
    }

    suspend fun loginWithGoogle(idToken: String): Resource<User> = try {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        val result = auth.signInWithCredential(credential).await()
        val uid = result.user!!.uid
        val docRef = firestore.collection("users").document(uid)
        val snapshot = docRef.get().await()
        val user = if (snapshot.exists()) {
            snapshot.toObject(User::class.java)!!
        } else {
            val newUser = User(
                uid = uid,
                name = result.user!!.displayName ?: "",
                email = result.user!!.email ?: "",
                photoUrl = result.user!!.photoUrl?.toString() ?: "",
                authProvider = "google"
            )
            docRef.set(newUser).await()
            newUser
        }
        Resource.Success(user)
    } catch (e: Exception) {
        Resource.Error(e.message ?: "Error con Google Sign-In")
    }

    suspend fun resetPassword(email: String): Resource<Unit> = try {
        auth.sendPasswordResetEmail(email).await()
        Resource.Success(Unit)
    } catch (e: Exception) {
        Resource.Error(e.message ?: "Error al enviar email")
    }

    fun logout() { auth.signOut() }
}

