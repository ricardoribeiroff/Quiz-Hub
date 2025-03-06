package dev.app.quizhub.data

import android.util.Log
import dev.app.quizhub.model.Question
import dev.app.quizhub.model.QuestionSet
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Columns

class QuestionDAO {
    private val supabase = DatabaseHelper().supabase

    suspend fun getAll(): List<Question> {
        return supabase.postgrest["questions"]
            .select(columns = Columns.list("id", "set_id", "question_text", "explanation"))
            .decodeList<Question>()
    }

    suspend fun getBySetId(setId: String): List<Question> {
        return supabase.postgrest["questions"]
            .select (columns = Columns.list("id", "set_id", "question_text", "explanation")){
                filter {
                    eq("set_id", setId.toLong())
                }
            }
            .decodeList<Question>()
    }

    suspend fun insert(question: Question) {
        try {
            val data = mapOf(
                "set_id" to question.setId,
                "question_text" to question.questionText,
                "explanation" to question.explanation
            )
            supabase.from("question").insert(data)
            Log.d("DEBUGLOG", "ATENÇÃO: DEU CERTO AQUI")
        } catch (e: Exception) {
            Log.d("DEBUGLOG", "ATENÇÃO: DEU MERDA AQUI: ${e.message}")
        }
    }
}