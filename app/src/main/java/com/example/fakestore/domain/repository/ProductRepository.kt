package com.example.fakestore.domain.repository

import com.example.fakestore.domain.model.Product
import com.example.fakestore.domain.utils.Either
import com.example.fakestore.domain.utils.NetworkError
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    fun getProducts(): Flow<Either<NetworkError, List<Product>>>
}