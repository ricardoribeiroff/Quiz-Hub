package dev.app.quizhub.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

public class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_NAME = "quizhub.db"
        private const val DATABASE_VERSION = 1

        // Nomes das tabelas
        const val TABLE_COLLECTIONS = "collections"
        const val TABLE_SECTIONS = "sections"
        const val TABLE_QUESTION_SETS = "question_sets"
        const val TABLE_QUESTIONS = "questions"
        const val TABLE_ALTERNATIVES = "alternatives"

        // Colunas comuns
        const val COLUMN_ID = "id"
        const val COLUMN_NAME = "name"
        const val COLUMN_DESCRIPTION = "description"
        const val COLUMN_ORDER_INDEX = "order_index"
        const val COLUMN_CREATED_AT = "created_at"
        const val COLUMN_UPDATED_AT = "updated_at"

        // Colunas específicas
        const val COLUMN_COLLECTION_ID = "collection_id"
        const val COLUMN_SECTION_ID = "section_id"
        const val COLUMN_SET_ID = "set_id"
        const val COLUMN_QUESTION_ID = "question_id"
        const val COLUMN_QUESTION_TEXT = "question_text"
        const val COLUMN_ALTERNATIVE_TEXT = "alternative_text"
        const val COLUMN_IS_CORRECT = "is_correct"
        const val COLUMN_EXPLANATION = "explanation"
    }

    override fun onCreate(db: SQLiteDatabase) {
        // Tabela Collections
        db.execSQL("""
            CREATE TABLE $TABLE_COLLECTIONS (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_NAME TEXT NOT NULL,
                $COLUMN_DESCRIPTION TEXT,
                $COLUMN_CREATED_AT TIMESTAMP DEFAULT (datetime('now', 'localtime')),
                $COLUMN_UPDATED_AT TIMESTAMP DEFAULT (datetime('now', 'localtime'))
            )
        """.trimIndent())

        // Tabela Sections
        db.execSQL("""
            CREATE TABLE $TABLE_SECTIONS (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_COLLECTION_ID INTEGER NOT NULL,
                $COLUMN_NAME TEXT NOT NULL,
                $COLUMN_DESCRIPTION TEXT,
                $COLUMN_ORDER_INDEX INTEGER DEFAULT 0,
                $COLUMN_CREATED_AT TIMESTAMP DEFAULT (datetime('now', 'localtime')),
                $COLUMN_UPDATED_AT TIMESTAMP DEFAULT (datetime('now', 'localtime')),
                FOREIGN KEY ($COLUMN_COLLECTION_ID) REFERENCES $TABLE_COLLECTIONS($COLUMN_ID) ON DELETE CASCADE
            )
        """.trimIndent())

        // Tabela Question Sets
        db.execSQL("""
            CREATE TABLE $TABLE_QUESTION_SETS (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_SECTION_ID INTEGER NOT NULL,
                $COLUMN_NAME TEXT NOT NULL,
                $COLUMN_DESCRIPTION TEXT,
                $COLUMN_ORDER_INDEX INTEGER DEFAULT 0,
                $COLUMN_CREATED_AT TIMESTAMP DEFAULT (datetime('now', 'localtime')),
                $COLUMN_UPDATED_AT TIMESTAMP DEFAULT (datetime('now', 'localtime')),
                FOREIGN KEY ($COLUMN_SECTION_ID) REFERENCES $TABLE_SECTIONS($COLUMN_ID) ON DELETE CASCADE
            )
        """.trimIndent())

        // Tabela Questions
        db.execSQL("""
            CREATE TABLE $TABLE_QUESTIONS (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_SET_ID INTEGER NOT NULL,
                $COLUMN_QUESTION_TEXT TEXT NOT NULL,
                $COLUMN_EXPLANATION TEXT,
                $COLUMN_ORDER_INDEX INTEGER DEFAULT 0,
                $COLUMN_CREATED_AT TIMESTAMP DEFAULT (datetime('now', 'localtime')),
                $COLUMN_UPDATED_AT TIMESTAMP DEFAULT (datetime('now', 'localtime')),
                FOREIGN KEY ($COLUMN_SET_ID) REFERENCES $TABLE_QUESTION_SETS($COLUMN_ID) ON DELETE CASCADE
            )
        """.trimIndent())

        // Tabela Alternatives
        db.execSQL("""
            CREATE TABLE $TABLE_ALTERNATIVES (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_QUESTION_ID INTEGER NOT NULL,
                $COLUMN_ALTERNATIVE_TEXT TEXT NOT NULL,
                $COLUMN_IS_CORRECT INTEGER DEFAULT 0,
                $COLUMN_ORDER_INDEX INTEGER DEFAULT 0,
                $COLUMN_CREATED_AT TIMESTAMP DEFAULT (datetime('now', 'localtime')),
                $COLUMN_UPDATED_AT TIMESTAMP DEFAULT (datetime('now', 'localtime')),
                FOREIGN KEY ($COLUMN_QUESTION_ID) REFERENCES $TABLE_QUESTIONS($COLUMN_ID) ON DELETE CASCADE
            )
        """.trimIndent())

        // Trigger para collections
        db.execSQL("""
            CREATE TRIGGER update_collections_timestamp
            AFTER UPDATE ON $TABLE_COLLECTIONS
            BEGIN
                UPDATE $TABLE_COLLECTIONS SET $COLUMN_UPDATED_AT = datetime('now', 'localtime') WHERE $COLUMN_ID = NEW.$COLUMN_ID;
            END
        """.trimIndent())

        // Trigger para sections
        db.execSQL("""
            CREATE TRIGGER update_sections_timestamp
            AFTER UPDATE ON $TABLE_SECTIONS
            BEGIN
                UPDATE $TABLE_SECTIONS SET $COLUMN_UPDATED_AT = datetime('now', 'localtime') WHERE $COLUMN_ID = NEW.$COLUMN_ID;
            END
        """.trimIndent())

        // Trigger para question_sets
        db.execSQL("""
            CREATE TRIGGER update_question_sets_timestamp
            AFTER UPDATE ON $TABLE_QUESTION_SETS
            BEGIN
                UPDATE $TABLE_QUESTION_SETS SET $COLUMN_UPDATED_AT = datetime('now', 'localtime') WHERE $COLUMN_ID = NEW.$COLUMN_ID;
            END
        """.trimIndent())

        // Trigger para questions
        db.execSQL("""
            CREATE TRIGGER update_questions_timestamp
            AFTER UPDATE ON $TABLE_QUESTIONS
            BEGIN
                UPDATE $TABLE_QUESTIONS SET $COLUMN_UPDATED_AT = datetime('now', 'localtime') WHERE $COLUMN_ID = NEW.$COLUMN_ID;
            END
        """.trimIndent())

        // Trigger para alternatives
        db.execSQL("""
            CREATE TRIGGER update_alternatives_timestamp
            AFTER UPDATE ON $TABLE_ALTERNATIVES
            BEGIN
                UPDATE $TABLE_ALTERNATIVES SET $COLUMN_UPDATED_AT = datetime('now', 'localtime') WHERE $COLUMN_ID = NEW.$COLUMN_ID;
            END
        """.trimIndent())
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }
}