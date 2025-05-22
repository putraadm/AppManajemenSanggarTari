package zhafran.putra.appproject2ckel3.model

import android.os.Parcel
import android.os.Parcelable

data class User(
    val idUser: Int?,
    val username: String,
    val email: String,
    val password: String,
    val role: String,
    val namaLengkap: String?,
    val tanggalLahir: String?,
    val namaOrtu: String?,
    val nomorHp: String?,
    val kelasId: Int?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(idUser)
        parcel.writeString(username)
        parcel.writeString(email)
        parcel.writeString(password)
        parcel.writeString(role)
        parcel.writeString(namaLengkap)
        parcel.writeString(tanggalLahir)
        parcel.writeString(namaOrtu)
        parcel.writeString(nomorHp)
        parcel.writeValue(kelasId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<User> {
        override fun createFromParcel(parcel: Parcel): User {
            return User(parcel)
        }

        override fun newArray(size: Int): Array<User?> {
            return arrayOfNulls(size)
        }

        fun fromAuthAndProfile(userAuth: UserAuth, userProfile: UserProfile): User {
            return User(
                idUser = userAuth.idUser,
                username = userAuth.username,
                email = userAuth.email,
                password = userAuth.password,
                role = userAuth.role,
                namaLengkap = userProfile.namaLengkap,
                tanggalLahir = userProfile.tanggalLahir,
                namaOrtu = userProfile.namaOrtu,
                nomorHp = userProfile.nomorHp,
                kelasId = userProfile.idJadwal
            )
        }
    }
}
