package com.techstore.techstore.data.model

data class CartItem(
    val productId: String = "",
    val productTitle: String = "",
    val productImage: String = "",
    val price: Double = 0.0,
    val quantity: Int = 1,
    val sellerId: String = ""
) {
    val subtotal: Double get() = price * quantity
}