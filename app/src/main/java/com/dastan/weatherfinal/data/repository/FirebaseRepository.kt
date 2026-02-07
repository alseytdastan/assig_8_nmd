package com.dastan.weatherfinal.data.repository

import com.dastan.weatherfinal.data.firebase.Favorite
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class FirebaseRepository {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseDatabase.getInstance().getReference("favorites")

    suspend fun ensureAuthenticated(): String {
        val user = auth.currentUser ?: auth.signInAnonymously().await().user
        return user?.uid ?: throw Exception("Auth failed")
    }

    fun getFavorites(uid: String): Flow<List<Favorite>> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = snapshot.children.mapNotNull { it.getValue(Favorite::class.java) }
                trySend(list.sortedByDescending { it.createdAt })
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }
        db.child(uid).addValueEventListener(listener)
        awaitClose { db.child(uid).removeEventListener(listener) }
    }

    suspend fun addFavorite(uid: String, city: String, note: String) {
        val pushRef = db.child(uid).push()
        val key = pushRef.key ?: return
        val item = Favorite(key, city, note, System.currentTimeMillis(), uid)
        pushRef.setValue(item).await()
    }

    suspend fun updateFavorite(uid: String, id: String, city: String, note: String) {
        db.child(uid).child(id).updateChildren(mapOf("city" to city, "note" to note)).await()
    }

    suspend fun deleteFavorite(uid: String, id: String) {
        db.child(uid).child(id).removeValue().await()
    }
}
