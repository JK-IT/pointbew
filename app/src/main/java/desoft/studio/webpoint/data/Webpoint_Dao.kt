package desoft.studio.webpoint.data

import androidx.lifecycle.LiveData
import androidx.room.*

/**
 *
@Insert - This method will add records. This means a query of type "Insert" will be executed.
@Update - This method will update records.
@Delete - This method to delete a record
@Query - Define the database operations to be performed directly using SQL commands.
 */


@Dao
interface Webpoint_Dao
{
   @Insert(onConflict = OnConflictStrategy.REPLACE)
   suspend fun AddPoint(point : Wpoint); // suspend -> to use coroutine
   
   
   
   @Query("SELECT * FROM webpoint_table ORDER BY Name COLLATE NOCASE ASC")
   fun ReadAll() : LiveData<List<Wpoint>>
   
   @Query("SELECT * FROM webpoint_table WHERE Name LIKE :inname")
   fun ReadItem(inname: String) : Wpoint
   
   @Delete
   fun DeleteItem(point :  Wpoint);
   
   @Delete
   fun DeleteMulti(polist : List<Wpoint>) // must be collections/array to work, attempt to use set collection ,but failed
   
}