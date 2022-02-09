package desoft.studio.webpoint.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "webpoint_table")//, primaryKeys = arrayOf("id", "Name"))
data class Wpoint
(
      @PrimaryKey
      val Name: String,
      val Url: String
      ) : Parcelable, Comparable<Wpoint>
{

      override fun compareTo(other: Wpoint): Int {
            return Name.compareTo(other.Name);
      }

}
   