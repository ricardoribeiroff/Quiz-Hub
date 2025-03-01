package dev.app.quizhub.data

import dev.app.quizhub.model.CollectionEntity
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.postgrest


class CollectionDAO {

    val supabase = DatabaseHelper().supabase
    suspend fun getAll(): List<CollectionEntity> {
        return supabase.postgrest["collections"]
            .select()
            .decodeList<CollectionEntity>()
    }

    suspend fun insert(collection: CollectionEntity) {
        try {
            val data = mapOf(
                "name" to collection.name,
                "description" to collection.description,
                "owner" to collection.owner
            )
            supabase.from("collections").insert(data)
        } catch (e: Exception) {
            // Handle exception
        }
    }
}