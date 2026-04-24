package com.techstore.techstore.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.techstore.techstore.data.model.Order
import com.techstore.techstore.data.model.OrderStatus
import com.techstore.techstore.util.Resource
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OrderRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    fun getBuyerOrders(buyerId: String): Flow<List<Order>> = callbackFlow {
        val listener = firestore.collection("orders")
            .whereEqualTo("buyerId", buyerId)
            .orderBy("createdAt", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .addSnapshotListener { snap, _ ->
                trySend(snap?.toObjects(Order::class.java) ?: emptyList())
            }
        awaitClose { listener.remove() }
    }

    fun getSellerOrders(sellerId: String): Flow<List<Order>> = callbackFlow {
        val listener = firestore.collection("orders")
            .whereEqualTo("sellerId", sellerId)
            .addSnapshotListener { snap, _ ->
                trySend(snap?.toObjects(Order::class.java) ?: emptyList())
            }
        awaitClose { listener.remove() }
    }

    suspend fun createOrder(order: Order): Resource<String> = try {
        val docRef = firestore.collection("orders").document()
        docRef.set(order).await()
        Resource.Success(docRef.id)
    } catch (e: Exception) { Resource.Error(e.message ?: "Error al crear pedido") }

    suspend fun updateOrderStatus(
        orderId: String, status: OrderStatus
    ): Resource<Unit> = try {
        firestore.collection("orders").document(orderId)
            .update("status", status.name).await()
        Resource.Success(Unit)
    } catch (e: Exception) { Resource.Error(e.message ?: "Error") }
}