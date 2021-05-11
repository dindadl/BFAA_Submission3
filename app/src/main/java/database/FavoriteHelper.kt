package database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import database.DatabaseContract.FavoriteColumns.Companion.TABLE_NAME
import database.DatabaseContract.FavoriteColumns.Companion.USERNAME
import java.sql.SQLException

class FavoriteHelper (context: Context) {

    private var databaseHelper: DatabaseHelper = DatabaseHelper(context)
    private lateinit var  database: SQLiteDatabase

    companion object {
        private const val DATABASE_TABLE = TABLE_NAME
        private var INSTANCE: FavoriteHelper? = null
        fun getInstance (context: Context): FavoriteHelper
        = INSTANCE ?: synchronized(this) {
            INSTANCE ?: FavoriteHelper(context)
        }
    }

    @Throws (SQLException::class)
    fun open() {
        database = databaseHelper.writableDatabase
    }
    fun close() {
        databaseHelper.close()

        if (database.isOpen)
           database.close()
    }



    fun queryAll(): Cursor {
        return database.query(
                DATABASE_TABLE,
            null,
            null,
            null,
            null,
            null,
            "$USERNAME ASC"
        )
    }

    fun queryById(id: String): Cursor {
        return database.query(
                DATABASE_TABLE,
                null,
                "$USERNAME = ?",
                arrayOf(id),
                null,
                null,
                null,
                null,
        )
    }

    fun insert(values: ContentValues?): Long {
        return this.database.insert(DATABASE_TABLE, null, values)
    }

    fun delete(username: String): Int {
        return database.delete(DATABASE_TABLE, "$USERNAME = '$username'", null)
    }

    fun update(id: String, values: ContentValues?): Int {
        return database.update(DATABASE_TABLE, values, "$USERNAME = ?", arrayOf(id))
    }
}
