package dev.app.quizhub.data

import android.content.ContentValues
import android.content.Context
import dev.app.quizhub.model.CollectionEntity


class CollectionDAO(private val context: Context) {

    private val dbHelper: DatabaseHelper by lazy {
        DatabaseHelper(context)
    }

    fun insert(collection: CollectionEntity): Long {
        val db = dbHelper.writableDatabase

        val values = ContentValues().apply {
            put(DatabaseHelper.COLUMN_NAME, collection.name)
            put(DatabaseHelper.COLUMN_DESCRIPTION, collection.description)
        }

        return db.insert(DatabaseHelper.TABLE_COLLECTIONS, null, values)
    }

    fun getById(id: Long): CollectionEntity? {
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            DatabaseHelper.TABLE_COLLECTIONS,
            arrayOf(
                DatabaseHelper.COLUMN_ID,
                DatabaseHelper.COLUMN_NAME,
                DatabaseHelper.COLUMN_DESCRIPTION,
                DatabaseHelper.COLUMN_CREATED_AT,
                DatabaseHelper.COLUMN_UPDATED_AT
            ),
            "\${DatabaseHelper.COLUMN_ID} = ?",
            arrayOf(id.toString()),
            null,
            null,
            null
        )

        return if (cursor.moveToFirst()) {
            val collection = CollectionEntity(
                id = cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID)),
                name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_NAME)),
                description = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DESCRIPTION)),
                createdAt = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CREATED_AT)),
                updatedAt = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_UPDATED_AT))
            )
            cursor.close()
            collection
        } else {
            cursor.close()
            null
        }
    }

    fun getAll(): List<CollectionEntity> {
        val collections = mutableListOf<CollectionEntity>()
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            DatabaseHelper.TABLE_COLLECTIONS,
            arrayOf(
                DatabaseHelper.COLUMN_ID,
                DatabaseHelper.COLUMN_NAME,
                DatabaseHelper.COLUMN_DESCRIPTION,
                DatabaseHelper.COLUMN_CREATED_AT,
                DatabaseHelper.COLUMN_UPDATED_AT
            ),
            null,
            null,
            null,
            null,
            "${DatabaseHelper.COLUMN_NAME} ASC"
        )

        while (cursor.moveToNext()) {
            collections.add(
                CollectionEntity(
                    id = cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID)),
                    name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_NAME)),
                    description = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DESCRIPTION)),
                    createdAt = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CREATED_AT)),
                    updatedAt = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_UPDATED_AT))
                )
            )
        }

        cursor.close()
        return collections
    }

    fun update(collection: CollectionEntity): Int {
        val db = dbHelper.writableDatabase

        val values = ContentValues().apply {
            put(DatabaseHelper.COLUMN_NAME, collection.name)
            put(DatabaseHelper.COLUMN_DESCRIPTION, collection.description)
        }

        return db.update(
            DatabaseHelper.TABLE_COLLECTIONS,
            values,
            "\${DatabaseHelper.COLUMN_ID} = ?",
            arrayOf(collection.id.toString())
        )
    }

    fun delete(id: Long): Int {
        val db = dbHelper.writableDatabase
        return db.delete(
            DatabaseHelper.TABLE_COLLECTIONS,
            "\${DatabaseHelper.COLUMN_ID} = ?",
            arrayOf(id.toString())
        )
    }
}