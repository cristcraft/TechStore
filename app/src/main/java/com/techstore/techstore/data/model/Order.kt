package com.techstore.techstore.data.model

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

enum class OrderStatus { PENDING, SHIPPED, DELIVERED, CANCELLED }
enum class PaymentMethod { CREDIT_CARD, PSE, NEQUI, DAVIPLATA, CASH_ON_DELIVERY }

data class Order(
    @DocumentId val id: String = "",
    val buyerId: String = "",
    val buyerName: String = "",
    val items: List<CartItem> = emptyList(),
    val subtotal: Double = 0.0,
    val tax: Double = 0.0,
    val shipping: Double = 0.0,
    val total: Double = 0.0,
    val status: String = OrderStatus.PENDING.name,
    val paymentMethod: String = PaymentMethod.PSE.name,
    val shippingAddress: String = "",
    val sellerId: String = "",
    val trackingNumber: String = "",
    @ServerTimestamp val createdAt: Date? = null
)