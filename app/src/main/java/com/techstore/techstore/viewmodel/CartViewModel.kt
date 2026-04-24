package com.techstore.techstore.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.techstore.techstore.data.model.CartItem
import com.techstore.techstore.data.model.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor() : ViewModel() {

    private val _cartItems = MutableLiveData<MutableList<CartItem>>(mutableListOf())
    val cartItems: LiveData<MutableList<CartItem>> = _cartItems

    val subtotal: Double get() = _cartItems.value?.sumOf { it.subtotal } ?: 0.0
    val tax: Double get() = subtotal * 0.19  // IVA 19%
    val total: Double get() = subtotal + tax
    val itemCount: Int get() = _cartItems.value?.sumOf { it.quantity } ?: 0

    fun addItem(product: Product) {
        val list = _cartItems.value ?: mutableListOf()
        val existing = list.find { it.productId == product.id }
        if (existing != null) {
            val idx = list.indexOf(existing)
            list[idx] = existing.copy(quantity = existing.quantity + 1)
        } else {
            list.add(CartItem(
                productId = product.id,
                productTitle = product.title,
                productImage = product.images.firstOrNull() ?: "",
                price = product.price,
                sellerId = product.sellerId
            ))
        }
        _cartItems.value = list
    }

    fun removeItem(productId: String) {
        _cartItems.value = _cartItems.value?.filter { it.productId != productId }?.toMutableList()
    }

    fun updateQuantity(productId: String, quantity: Int) {
        val list = _cartItems.value ?: return
        if (quantity <= 0) { removeItem(productId); return }
        val idx = list.indexOfFirst { it.productId == productId }
        if (idx >= 0) list[idx] = list[idx].copy(quantity = quantity)
        _cartItems.value = list
    }

    fun clearCart() { _cartItems.value = mutableListOf() }
}