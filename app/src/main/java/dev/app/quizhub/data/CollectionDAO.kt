package dev.app.quizhub.data

import android.util.Log
import android.widget.Toast
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import dev.app.quizhub.model.CollectionEntity
import kotlinx.coroutines.tasks.await

class CollectionDAO {

    private val db = FirebaseFirestore.getInstance()
    private val collectionRef = db.collection("collections")

    suspend fun getAll(): List<CollectionEntity> {
        return try {
            val snapshot = collectionRef.get().await()
            snapshot.toObjects(CollectionEntity::class.java)
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun insert(collection: CollectionEntity) {
        try {
            val data = mapOf(
                "name" to collection.name,
                "description" to collection.description,
                "owner" to collection.owner,
                "createdAt" to FieldValue.serverTimestamp(),
                "updatedAt" to FieldValue.serverTimestamp()
            )
            collectionRef.add(data).await()
        } catch (e: Exception) {
            Log.d("CollectionDAO", "Error inserting collection", e)
        }
    }
}