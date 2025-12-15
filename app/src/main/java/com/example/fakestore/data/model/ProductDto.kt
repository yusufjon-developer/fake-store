package com.example.fakestore.data.model

import com.example.fakestore.domain.model.Product

data class ProductDto(
    val id: Int,
    val title: String,
    val price: Double,
    val description: String,
    val category: String,
    val image: String
)

fun ProductDto.toDomain() =
    Product(
        id = id,
        title = title,
        price = price,
        description = description,
        category = category,
        image = image
    )