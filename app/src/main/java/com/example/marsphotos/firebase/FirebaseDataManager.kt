package com.example.marsphotos.firebase

import com.google.firebase.Firebase
import com.google.firebase.database.DatabaseReference

import com.google.firebase.database.*
import kotlin.system.exitProcess


private val databaseReference = Firebase.database("https://marsphotos-b69f9-default-rtdb.europe-west1.firebasedatabase.app/").reference

//  Add an option to Save the current displayed photos
fun saveCurrentPhotos(
    marsPhotoName: String,
    marsPhotoURL: String,
    picsumPhotoName: String,
    picsumPhotoURL: String
) {

    databaseReference.child("photos").push().setValue(
        FirebasePhotoData(
            marsPhotoName = marsPhotoName,
            marsPhotoURL = marsPhotoURL,
            picsumPhotoName = picsumPhotoName,
            picsumPhotoURL = picsumPhotoURL
        )
    )
}

//  Add the option to Load last images saved
fun loadLastSavedPhotos(callback: (FirebasePhotoData) -> Unit) {
    val photos = mutableListOf<FirebasePhotoData>()
    databaseReference.child("photos")
        .limitToLast(5)
        .addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (childSnapshot in snapshot.children) {
                    val firebasePhoto = childSnapshot.getValue(FirebasePhotoData::class.java)
                    firebasePhoto?.let { photos.add(it) }
                }
                callback(photos.last())
            }

            override fun onCancelled(error: DatabaseError) {
                exitProcess(-1)
            }
        })
}

/// Função para incrementar a contagem de "Roll"
fun incrementRollCount(callback: (Long) -> Unit) {
    databaseReference.child("roll_count").addListenerForSingleValueEvent(object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            val currentCount = snapshot.value as? Long ?: 0
            databaseReference.child("roll_count").setValue(currentCount + 1)
                .addOnCompleteListener {
                    callback(currentCount+1)
                }
        }

        override fun onCancelled(error: DatabaseError) {
            exitProcess(-1)
        }
    })
}

fun resetRollValue(){
    databaseReference.child("roll_count").setValue(null)
}




