package dev.app.quizhub.data

import android.util.Log
import dev.app.quizhub.model.Alternative
import dev.app.quizhub.model.Section
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Columns

class AlternativeDAO {
    val supabase = DatabaseHelper().supabase


    suspend fun getByQuestionId(questionId: String): List<Alternative> {
        return supabase.postgrest["alternatives"]
            .select (columns = Columns.list("id", "alternative_text")){
                filter {
                    eq("question_id", questionId)
                }
            }
            .decodeList<Alternative>()
    }

    suspend fun insert(alternative: Alternative) {
        try {
            val data = mapOf(
                "question_id" to alternative.questionId,
                "alternative_text" to alternative.alternativeText,
            )
            supabase.from("alternatives").insert(data)
            Log.d("success", "ATENÇÃO: DEU CERTO AQUI")
        } catch (e: Exception) {
            Log.d("error", "ATENÇÃO: DEU MERDA AQUI: ${e.message}")
        }
    }
}