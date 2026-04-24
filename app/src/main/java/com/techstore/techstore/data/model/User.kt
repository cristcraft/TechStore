package com.techstore.techstore.data.model

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

enum class UserRole { BUYER, SELLER, ADMIN }

data class User(
    @DocumentId val uid: String = "",
    val name: String = "",
    val email: String = "",
    val phone: String = "",
    val photoUrl: String = "",
    val role: String = UserRole.BUYER.name,
    val address: String = "",
    val isActive: Boolean = true,
    val authProvider: String = "email",
    @ServerTimestamp val createdAt: Date? = null
)