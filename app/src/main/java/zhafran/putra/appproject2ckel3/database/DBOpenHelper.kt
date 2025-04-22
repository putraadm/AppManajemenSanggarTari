package zhafran.putra.appproject2ckel3.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBOpenHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object{
        private const val DATABASE_NAME = "db_sanggar_tari"
        private const val DATABASE_VERSION = 1
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val sqlUser = "CREATE TABLE user(id_user INTEGER PRIMARY KEY AUTOINCREMENT, nama_lengkap TEXT, username TEXT, email TEXT, password TEXT, id_jadwal_kelas INTEGER, role TEXT CHECK(role IN ('admin', 'user')) DEFAULT 'user', FOREIGN KEY(id_jadwal_kelas) REFERENCES jadwal_kelas(id_jadwal))"
        val sqlAbsensi = "CREATE TABLE absensi(id_absensi INTEGER PRIMARY KEY AUTOINCREMENT, id_user INTEGER, tanggal TEXT, status TEXT, FOREIGN KEY(id_user) REFERENCES user(id_user))"
        val sqlReport = "CREATE TABLE report(id_report INTEGER PRIMARY KEY AUTOINCREMENT, id_user INTEGER, tanggal TEXT, warama TEXT, wirasa TEXT, wiraga TEXT, catatan TEXT, FOREIGN KEY(id_user) REFERENCES user(id_user))"
        val sqlJadwal = "CREATE TABLE jadwal_kelas(id_jadwal INTEGER PRIMARY KEY AUTOINCREMENT, kelas TEXT, hari TEXT, jam TEXT)"
        val sqlLomba = "CREATE TABLE lomba(id_lomba INTEGER PRIMARY KEY AUTOINCREMENT, poster BLOB,judul TEXT, deskripsi TEXT)"
        val insertJadwalKelas = "INSERT INTO jadwal_kelas(kelas, hari, jam) VALUES('A', 'Sabtu', '09:00'), ('B', 'Sabtu', '15:00'), ('C', 'Sabtu', '09:00'), ('D', 'Sabtu', '15:00')"
        val insertUser ="INSERT INTO user(nama_lengkap, username, email, password, id_jadwal_kelas, role) VALUES('admin', 'admin', 'admin@test.com', 'admin', ?, 'admin')"
        db?.execSQL(sqlUser)
        db?.execSQL(sqlAbsensi)
        db?.execSQL(sqlReport)
        db?.execSQL(sqlJadwal)
        db?.execSQL(sqlLomba)
        db?.execSQL(insertJadwalKelas)
        db?.execSQL(insertUser)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }

    // CRUD for User
    fun insertUser(namaLengkap: String, username: String, email: String, password: String, idJadwalKelas: Int, role: String = "user"): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("nama_lengkap", namaLengkap)
            put("username", username)
            put("email", email)
            put("password", password)
            put("id_jadwal_kelas", idJadwalKelas)
            put("role", role)
        }
        return db.insert("user", null, values)
    }

    fun updateUser(idUser: Int, namaLengkap: String, username: String, email: String, password: String, idJadwalKelas: Int, role: String): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("nama_lengkap", namaLengkap)
            put("username", username)
            put("email", email)
            put("password", password)
            put("id_jadwal_kelas", idJadwalKelas)
            put("role", role)
        }
        return db.update("user", values, "id_user = ?", arrayOf(idUser.toString()))
    }

    fun deleteUser(idUser: Int): Int {
        val db = writableDatabase
        return db.delete("user", "id_user = ?", arrayOf(idUser.toString()))
    }

    fun getAllUsers(): Cursor {
        val db = readableDatabase
        return db.rawQuery("SELECT * FROM user", null)
    }

    // CRUD for Lomba
    fun insertLomba(poster: ByteArray?, judul: String, deskripsi: String): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("poster", poster)
            put("judul", judul)
            put("deskripsi", deskripsi)
        }
        return db.insert("lomba", null, values)
    }

    fun updateLomba(idLomba: Int, poster: ByteArray?, judul: String, deskripsi: String): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("poster", poster)
            put("judul", judul)
            put("deskripsi", deskripsi)
        }
        return db.update("lomba", values, "id_lomba = ?", arrayOf(idLomba.toString()))
    }

    fun deleteLomba(idLomba: Int): Int {
        val db = writableDatabase
        return db.delete("lomba", "id_lomba = ?", arrayOf(idLomba.toString()))
    }

    fun getAllLomba(): Cursor {
        val db = readableDatabase
        return db.rawQuery("SELECT * FROM lomba", null)
    }

    // CRUD for Absensi
    fun insertAbsensi(idUser: Int, tanggal: String, status: String): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("id_user", idUser)
            put("tanggal", tanggal)
            put("status", status)
        }
        return db.insert("absensi", null, values)
    }

    fun updateAbsensi(idAbsensi: Int, idUser: Int, tanggal: String, status: String): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("id_user", idUser)
            put("tanggal", tanggal)
            put("status", status)
        }
        return db.update("absensi", values, "id_absensi = ?", arrayOf(idAbsensi.toString()))
    }

    fun deleteAbsensi(idAbsensi: Int): Int {
        val db = writableDatabase
        return db.delete("absensi", "id_absensi = ?", arrayOf(idAbsensi.toString()))
    }

    fun getAllAbsensi(): Cursor {
        val db = readableDatabase
        return db.rawQuery("SELECT * FROM absensi", null)
    }

    // CRUD for Report
    fun insertReport(idUser: Int, tanggal: String, warama: String, wirasa: String, wiraga: String, catatan: String): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("id_user", idUser)
            put("tanggal", tanggal)
            put("warama", warama)
            put("wirasa", wirasa)
            put("wiraga", wiraga)
            put("catatan", catatan)
        }
        return db.insert("report", null, values)
    }

    fun updateReport(idReport: Int, idUser: Int, tanggal: String, warama: String, wirasa: String, wiraga: String, catatan: String): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("id_user", idUser)
            put("tanggal", tanggal)
            put("warama", warama)
            put("wirasa", wirasa)
            put("wiraga", wiraga)
            put("catatan", catatan)
        }
        return db.update("report", values, "id_report = ?", arrayOf(idReport.toString()))
    }

    fun deleteReport(idReport: Int): Int {
        val db = writableDatabase
        return db.delete("report", "id_report = ?", arrayOf(idReport.toString()))
    }

    fun getAllReport(): Cursor {
        val db = readableDatabase
        return db.rawQuery("SELECT * FROM report", null)
    }

    // CRUD for Jadwal Kelas
    fun insertJadwal(kelas: String, hari: String, jam: String): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("kelas", kelas)
            put("hari", hari)
            put("jam", jam)
        }
        return db.insert("jadwal_kelas", null, values)
    }

    fun updateJadwal(idJadwal: Int, kelas: String, hari: String, jam: String): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("kelas", kelas)
            put("hari", hari)
            put("jam", jam)
        }
        return db.update("jadwal_kelas", values, "id_jadwal = ?", arrayOf(idJadwal.toString()))
    }

    fun deleteJadwal(idJadwal: Int): Int {
        val db = writableDatabase
        return db.delete("jadwal_kelas", "id_jadwal = ?", arrayOf(idJadwal.toString()))
    }

    fun getAllJadwal(): Cursor {
        val db = readableDatabase
        return db.rawQuery("SELECT * FROM jadwal_kelas", null)
    }
}