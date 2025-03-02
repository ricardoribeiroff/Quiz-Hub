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

    suspend fun getBySectionId(sectionId: Int): List<QuestionSet> {
        return supabase.postgrest["question_sets"]
            .select (columns = Columns.list("id", "name", "description", "is_finished")){
                filter {
                    eq("section_id", sectionId)
                }
            }
            .decodeList<QuestionSet>()
    }

    suspend fun insert(questionSet: QuestionSet) {
        try {
            supabase.postgrest["question_sets"]
                .insert(questionSet)
            Log.d("success", "ATENÇÃO: DEU CERTO AQUI")
        } catch (e: Exception) {
            Log.d("error", "ATENÇÃO: DEU MERDA AQUI: ${e.message}")
            e.printStackTrace()
        }
    }

    suspend fun updateIsFinished(setId: String, isFinished: Boolean) {
        try {
            supabase.postgrest["question_sets"]
                .update(
                    {
                        set("is_finished", isFinished)
                    }
                ) {
                    filter {
                        eq("id", setId)
                    }
                }
            Log.d("success", "Status is_finished atualizado com sucesso")
        } catch (e: Exception) {
            Log.e("error", "Erro ao atualizar is_finished: ${e.message}")
        }
    }
}