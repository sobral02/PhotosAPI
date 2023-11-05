package com.example.marsphotos.firebase

data class FirebasePhotoData(
    val marsPhotoName: String,
    val marsPhotoURL: String,
    val picsumPhotoName: String,
    val picsumPhotoURL: String
){
    constructor() : this("", "", "", "")
}

