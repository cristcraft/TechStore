package com.techstore.techstore.viewmodel

import android.net.Uri
import androidx.lifecycle.*
import com.techstore.techstore.data.model.Product
import com.techstore.techstore.data.repository.ProductRepository
import com.techstore.techstore.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val productRepo: ProductRepository
) : ViewModel() {

    private val _products = MutableLiveData<List<Product>>()
    val products: LiveData<List<Product>> = _products

    private val _operationResult = MutableLiveData<Resource<Unit>>()
    val operationResult: LiveData<Resource<Unit>> = _operationResult

    private val _selectedCategory = MutableLiveData<String?>(null)

    private val _searchQuery = MutableLiveData<String>("")

    val filteredProducts: LiveData<List<Product>> = MediatorLiveData<List<Product>>().apply {
        fun update() {
            val all = _products.value ?: emptyList()
            val query = _searchQuery.value ?: ""
            val cat = _selectedCategory.value
            value = all.filter { p ->
                (query.isBlank() || p.title.contains(query, ignoreCase = true)) &&
                        (cat == null || p.category == cat)
            }
        }
        addSource(_products) { update() }
        addSource(_searchQuery) { update() }
        addSource(_selectedCategory) { update() }
    }

    fun loadProducts(category: String? = null) {
        viewModelScope.launch {
            productRepo.getProducts(category).collectLatest {
                _products.value = it
            }
        }
    }

    fun setSearch(query: String) { _searchQuery.value = query }
    fun setCategory(cat: String?) { _selectedCategory.value = cat }

    fun createProduct(product: Product) {
        _operationResult.value = Resource.Loading()
        viewModelScope.launch {
            val result = productRepo.createProduct(product)
            _operationResult.value = when (result) {
                is Resource.Success -> Resource.Success(Unit)
                is Resource.Error -> Resource.Error(result.message)
                else -> Resource.Error("Error desconocido")
            }
        }
    }

    fun uploadImage(uri: Uri, productId: String, index: Int) {
        viewModelScope.launch {
            productRepo.uploadProductImage(uri, productId, index)
        }
    }
}

