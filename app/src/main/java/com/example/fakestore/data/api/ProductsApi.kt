package com.example.fakestore.data.api

import com.example.fakestore.data.model.ProductDto
import retrofit2.Response
import retrofit2.http.GET

interface ProductsApi {
    @GET("products")
    suspend fun getProducts(): Response<List<ProductDto>>
}