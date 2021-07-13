package desoft.studio.webpoint.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "webpoint_table")//, primaryKeys = arrayOf("id", "Name"))
data class Wpoint
(
      //@PrimaryKey(autoGenerate = true)
      //val id: Int,
      @PrimaryKey
      val Name: String,
      val Url: String
      ) : Parcelable
   