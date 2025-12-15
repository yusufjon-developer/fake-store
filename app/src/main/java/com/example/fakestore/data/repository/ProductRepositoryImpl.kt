package com.example.fakestore.data.repository

import com.example.fakestore.data.api.ProductsApi
import com.example.fakestore.data.core.RemoteRepository
import com.example.fakestore.data.model.toDomain
import com.example.fakestore.domain.model.Product
import com.example.fakestore.domain.repository.ProductRepository
import com.example.fakestore.domain.utils.Either
import com.example.fakestore.domain.utils.NetworkError
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val api: ProductsApi
) : RemoteRepository(), ProductRepository {

    override fun getProducts(): Flow<Either<NetworkError, List<Product>>> {
        return doNetworkRequest(
            request = { api.getProducts() },
            mapper = { productDtos -> productDtos.map { item -> item.toDomain() } }
        )
    }
}