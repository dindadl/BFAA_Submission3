package provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import database.DatabaseContract.AUTHORITY
import database.DatabaseContract.FavoriteColumns.Companion.CONTENT_URI
import database.DatabaseContract.FavoriteColumns.Companion.TABLE_NAME
import database.FavoriteHelper

class FavoriteProvider : ContentProvider() {

    companion object {
        private const val USER_FAVORITE = 1
        private const val USER_ID = 2
        private lateinit var favHelper: FavoriteHelper
        private val sUriMatcher = UriMatcher(UriMatcher.NO_MATCH)

        init {
            sUriMatcher.addURI(AUTHORITY, TABLE_NAME, USER_FAVORITE)
            sUriMatcher.addURI(AUTHORITY, "$TABLE_NAME/#", USER_ID)
        }
    }

    override fun onCreate(): Boolean {
        favHelper = FavoriteHelper.getInstance(context as Context)
        favHelper.open()
        return true    }

    override fun query(uri: Uri, projection: Array<String>?, selection: String?,
                       selectionArgs: Array<String>?, sortOrder: String?): Cursor? {
        return when (sUriMatcher.match(uri)) {
            USER_FAVORITE -> favHelper.queryAll()
            USER_ID -> favHelper.queryById(uri.lastPathSegment.toString())
            else -> null
        }
    }

    override fun getType(uri: Uri): String? {
        return null
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val added: Long = when (USER_FAVORITE) {
            sUriMatcher.match(uri) -> favHelper.insert(values)
            else -> 0
        }

        context?.contentResolver?.notifyChange(CONTENT_URI, null)
        return Uri.parse("$CONTENT_URI/$added")
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        val deleted: Int = when (USER_ID) {
            sUriMatcher.match(uri) -> favHelper.delete(uri.lastPathSegment.toString())
            else -> 0
        }
        context?.contentResolver?.notifyChange(CONTENT_URI, null)
        return deleted
    }


    override fun update(uri: Uri, values: ContentValues?, selection: String?,
                        selectionArgs: Array<String>?): Int {
        val updated: Int = when (USER_ID) {
            sUriMatcher.match(uri) -> favHelper.update(uri.lastPathSegment.toString(), values)
            else -> 0
        }
        context?.contentResolver?.notifyChange(CONTENT_URI, null)
        return updated
        }
}