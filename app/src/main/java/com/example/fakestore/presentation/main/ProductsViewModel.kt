package com.example.fakestore.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fakestore.domain.model.Product
import com.example.fakestore.domain.repository.ProductRepository
import com.example.fakestore.domain.utils.Either
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class UiState {
    object Loading : UiState()
    data class Success(val products: List<Product>) : UiState()
    data class Error(val message: String) : UiState()
}

@HiltViewModel
class ProductsViewModel @Inject constructor(
    private val repository: ProductRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState

    private var allProducts: List<Product> = emptyList()

    init {
        loadProducts()
    }

    fun loadProducts() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            repository.getProducts().collect { result ->
                when (result) {
                    is Either.Right -> {
                        allProducts = result.value
                        _uiState.value = UiState.Success(result.value)
                    }
                    is Either.Left -> {
                        _uiState.value = UiState.Error(result.value.toString())
                    }
                }
            }
        }
    }

    fun searchProducts(query: String) {
        if (query.isBlank()) {
            _uiState.value = UiState.Success(allProducts)
            return
        }
        val filtered = allProducts.filter { it.title.contains(query, ignoreCase = true) }
        _uiState.value = UiState.Success(filtered)
    }
}