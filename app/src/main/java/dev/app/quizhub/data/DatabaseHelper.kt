package dev.app.quizhub.data

import android.app.Application
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest

class DatabaseHelper {

    val supabase = createSupabaseClient(
        supabaseUrl = "https://tixzzvwqwqemkenictcp.supabase.co",
        supabaseKey  = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InRpeHp6dndxd3FlbWtlbmljdGNwIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NDA3NjQ3OTAsImV4cCI6MjA1NjM0MDc5MH0.tqE92QYoQGVe1U7Qrbkb3yH-o345xRFBaao_246PBQA"
    ){
        install(Postgrest)
    }
}