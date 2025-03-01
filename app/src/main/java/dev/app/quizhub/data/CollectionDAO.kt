package dev.app.quizhub.data

import dev.app.quizhub.model.CollectionEntity
import dev.app.quizhub.model.LoginState
import dev.app.quizhub.model.Section
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Columns


class CollectionDAO {

    val supabase = DatabaseHelper().supabase
    suspend fun getAll(): List<CollectionEntity> {
        return supabase.postgrest["collections"]
            .select()
            .decodeList<CollectionEntity>()
    }
    suspend fun getByUserUid(uidUser: String): List<CollectionEntity> {
        return supabase.postgrest["collections"]
            .select (columns = Columns.list("id", "name", "description", "owner")){
                filter {
                    eq("uid_user", uidUser)
                }
            }
            .decodeList<CollectionEntity>()
    }
    suspend fun insert(collection: CollectionEntity) {
        try {
            val data = mapOf(
                "name" to collection.name,
                "description" to collection.description,
                "owner" to collection.owner,
                "uid_user" to collection.uidUser
            )
            supabase.from("collections").insert(data)
        } catch (e: Exception) {
            // Handle exception
        }
    }
}