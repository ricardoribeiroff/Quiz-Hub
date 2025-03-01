package dev.app.quizhub.data

import android.util.Log
import dev.app.quizhub.model.QuestionSet
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Columns

class QuestionSetDAO {
    private val supabase = DatabaseHelper().supabase

    suspend fun getAll(): List<QuestionSet> {
        return supabase.postgrest["question_sets"]
            .select()
            .decodeList<QuestionSet>()
    }

    suspend fun getBySectionId(sectionId: String): List<QuestionSet> {
        return supabase.postgrest["question_sets"]
            .select (columns = Columns.list("id", "name", "description")){
                filter {
                    eq("section_id", sectionId)
                }
            }
            .decodeList<QuestionSet>()
    }

    suspend fun insert(questionSet: QuestionSet) {
        try {
            val data = mapOf(
                "section_id" to questionSet.sectionId,
                "name" to questionSet.name,
                "description" to questionSet.description
            )
            supabase.from("question_sets").insert(data)
            Log.d("success", "ATENÇÃO: DEU CERTO AQUI")
        } catch (e: Exception) {
            Log.d("error", "ATENÇÃO: DEU MERDA AQUI: ${e.message}")
        }
    }
}