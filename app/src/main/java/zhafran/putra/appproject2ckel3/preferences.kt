package zhafran.putra.appproject2ckel3

import android.content.Context

class preferences(context: Context) {
    private val TAG_STATUS = "status"
    private val TAG_LEVEL = "level"
    private val TAG_USER_ID = "user_id"
    private val TAG_USER_FULLNAME = "user_fullname"
    private val TAG_USER_JADWAL_KELAS = "user_jadwal_kelas"
    private val TAG_APP = "app"

    private val sharedPreferences = context.getSharedPreferences(TAG_APP, Context.MODE_PRIVATE)

    var prefStatus: Boolean
        get() = sharedPreferences.getBoolean(TAG_STATUS, false)
        set(value) = sharedPreferences.edit().putBoolean(TAG_STATUS, value).apply()

    var prefLevel: String?
        get() = sharedPreferences.getString(TAG_LEVEL, "")
        set(value) = sharedPreferences.edit().putString(TAG_LEVEL, value).apply()

    var prefUserId: Int
        get() = sharedPreferences.getInt(TAG_USER_ID, -1)
        set(value) = sharedPreferences.edit().putInt(TAG_USER_ID, value).apply()

    var prefUserFullName: String?
        get() = sharedPreferences.getString(TAG_USER_FULLNAME, "")
        set(value) = sharedPreferences.edit().putString(TAG_USER_FULLNAME, value).apply()

    var prefUserKelasId: Int
        get() = sharedPreferences.getInt(TAG_USER_JADWAL_KELAS, -1)
        set(value) = sharedPreferences.edit().putInt(TAG_USER_JADWAL_KELAS, value).apply()

    fun clear() {
        sharedPreferences.edit().remove(TAG_STATUS).apply()
        sharedPreferences.edit().remove(TAG_LEVEL).apply()
        sharedPreferences.edit().remove(TAG_USER_ID).apply()
        sharedPreferences.edit().remove(TAG_USER_FULLNAME).apply()
        sharedPreferences.edit().remove(TAG_USER_JADWAL_KELAS).apply()
    }

}
