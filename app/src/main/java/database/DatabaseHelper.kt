package database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import database.DatabaseContract.FavoriteColumns.Companion.AVATAR
import database.DatabaseContract.FavoriteColumns.Companion.TABLE_NAME
import database.DatabaseContract.FavoriteColumns.Companion.USERNAME

internal class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION)
{
    companion object {
        private const val DATABASE_NAME = "fav_db"
        private const val DATABASE_VERSION = 1

        private const val SQL_CREATE_TABLE_FAVORITE = "CREATE TABLE $TABLE_NAME" +
                "($USERNAME  TEXT NOT NULL PRIMARY KEY," +
                "$AVATAR TEXT NOT NULL)"
    }

    override fun onCreate(database: SQLiteDatabase) {
        database.execSQL(SQL_CREATE_TABLE_FAVORITE)
    }

    override fun onUpgrade(database: SQLiteDatabase,oldVersion: Int, newVersion: Int) {
        database.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(database)
    }
}