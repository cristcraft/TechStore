package com.techstore.techstore.data.model

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

enum class ProductStatus { ACTIVE, PAUSED, DELETED }
enum class ProductCategory {
    SMARTPHONES, LAPTOPS, AUDIO, GAMING, WEARABLES, COMPONENTS, ACCESSORIES
}

data class Product(
    @DocumentId val id: String = "",
    val title: String = "",
    val description: String = "",
    val category: String = ProductCategory.ACCESSORIES.name,
    val price: Double = 0.0,
    val stock: Int = 0,
    val warranty: String = "",
    val images: List<String> = emptyList(),
    val sellerId: String = "",
    val sellerName: String = "",
    val status: String = ProductStatus.ACTIVE.name,
    val rating: Double = 0.0,
    val reviewCount: Int = 0,
    @ServerTimestamp val createdAt: Date? = null
)