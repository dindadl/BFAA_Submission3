package Helper

import Model.UserData
import android.database.Cursor
import database.DatabaseContract
import database.DatabaseContract.FavoriteColumns.Companion.AVATAR
import database.DatabaseContract.FavoriteColumns.Companion.USERNAME

object MappingHelper {

    fun mapCursorToArrayList(favCursor: Cursor?): ArrayList<UserData> {
        val favList = ArrayList<UserData>()
        favCursor?.apply {
            while (moveToNext()) {
                // val id = getInt(getColumnIndexOrThrow(ID))
                val username = getString(getColumnIndexOrThrow(USERNAME))
                val avatar = getString(getColumnIndexOrThrow(AVATAR))
                favList.add(UserData(username = username, avatar = avatar))
            }
        }
        return favList
    }

    fun mapCursorToObject(favCursor: Cursor): UserData {
        var favorite: UserData
        favCursor.apply {
            moveToFirst()
            val username = getString(getColumnIndexOrThrow(USERNAME))
           // val name = getString(getColumnIndexOrThrow(DatabaseContract.FavoColumns.NAME))
            val avatar = getString(getColumnIndexOrThrow(AVATAR))
           // val company = getString(getColumnIndexOrThrow(DatabaseContract.FavoColumns.COMPANY))
           // val location = getString(getColumnIndexOrThrow(DatabaseContract.FavoColumns.LOCATION))
            // val repository =
               // getString(getColumnIndexOrThrow(DatabaseContract.FavoColumns.REPOSITORY))
           // val isfav = getString(getColumnIndexOrThrow(DatabaseContract.FavoColumns.ISFAV))
            favorite = UserData(username, avatar,)

        }
        return favorite
    }
}
