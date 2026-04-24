package com.techstore.techstore.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import com.techstore.techstore.data.model.Product
import com.techstore.techstore.data.model.ProductStatus
import com.techstore.techstore.util.Resource
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import android.net.Uri
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage
) {
    // Observa productos en tiempo real
    fun getProducts(category: String? = null): Flow<List<Product>> = callbackFlow {
        var query: Query = firestore.collection("products")
            .whereEqualTo("status", ProductStatus.ACTIVE.name)
        if (category != null) query = query.whereEqualTo("category", category)
        val listener = query.addSnapshotListener { snap, _ ->
            val products = snap?.toObjects(Product::class.java) ?: emptyList()
            trySend(products)
        }
        awaitClose { listener.remove() }
    }

    // Obtiene productos de un vendedor específico
    fun getSellerProducts(sellerId: String): Flow<List<Product>> = callbackFlow {
        val listener = firestore.collection("products")
            .whereEqualTo("sellerId", sellerId)
            .addSnapshotListener { snap, _ ->
                trySend(snap?.toObjects(Product::class.java) ?: emptyList())
            }
        awaitClose { listener.remove() }
    }

    suspend fun createProduct(product: Product): Resource<String> = try {
        val docRef = firestore.collection("products").document()
        firestore.collection("products").document(docRef.id)
            .set(product.copy()).await()
        Resource.Success(docRef.id)
    } catch (e: Exception) { Resource.Error(e.message ?: "Error") }

    suspend fun updateProduct(product: Product): Resource<Unit> = try {
        firestore.collection("products").document(product.id).set(product).await()
        Resource.Success(Unit)
    } catch (e: Exception) { Resource.Error(e.message ?: "Error") }

    // Soft delete: cambia status a DELETED
    suspend fun deleteProduct(productId: String): Resource<Unit> = try {
        firestore.collection("products").document(productId)
            .update("status", ProductStatus.DELETED.name).await()
        Resource.Success(Unit)
    } catch (e: Exception) { Resource.Error(e.message ?: "Error") }

    // Sube imagen a Firebase Storage y retorna la URL
    suspend fun uploadProductImage(uri: Uri, productId: String, index: Int): Resource<String> = try {
        val ref = storage.reference.child("products/$productId/image_$index.jpg")
        ref.putFile(uri).await()
        val url = ref.downloadUrl.await().toString()
        Resource.Success(url)
    } catch (e: Exception) { Resource.Error(e.message ?: "Error al subir imagen") }
}

