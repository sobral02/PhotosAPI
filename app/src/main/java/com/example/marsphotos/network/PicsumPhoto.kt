package com.example.marsphotos.network
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class PicsumPhoto(
    val id: String,

    @SerialName(value = "download_url")
    val imgSrc: String
)