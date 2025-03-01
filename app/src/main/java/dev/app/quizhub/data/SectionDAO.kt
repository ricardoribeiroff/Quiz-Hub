package dev.app.quizhub.data

import android.util.Log
import dev.app.quizhub.model.Section
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Columns

class SectionDAO {
    val supabase = DatabaseHelper().supabase

    suspend fun getAll(): List<Section> {
        return supabase.postgrest["sections"]
            .select()
            .decodeList<Section>()
    }

    suspend fun getByCollectionId(collectionId: String): List<Section> {
        return supabase.postgrest["sections"]
            .select (columns = Columns.list("id", "name", "description")){
                filter {
                    eq("collection_id", collectionId)
                }
            }
            .decodeList<Section>()
    }

    suspend fun insert(section: Section) {
        try {
            val data = mapOf(
                "collection_id" to section.collectionId,
                "name" to section.name,
                "description" to section.description
            )
            supabase.from("sections").insert(data)
            Log.d("success", "ATENÇÃO: DEU CERTO AQUI")
        } catch (e: Exception) {
            Log.d("error", "ATENÇÃO: DEU MERDA AQUI: ${e.message}")
        }
    }
}