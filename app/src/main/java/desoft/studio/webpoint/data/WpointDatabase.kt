package desoft.studio.webpoint.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [Wpoint::class], version = 2, exportSchema = true)
abstract class WpointDatabase : RoomDatabase()
{
   
   abstract val WpointDao : Webpoint_Dao
   
   
   companion object
   {
      @Volatile
      private var _ins: WpointDatabase? = null;
      
      fun GetDatabase(ctx: Context): WpointDatabase
      {
         
         synchronized(this)
         {
            var instance = _ins;
            if(instance == null) {
               instance = Room.databaseBuilder(ctx.applicationContext, WpointDatabase::class.java,"webpoint_db").fallbackToDestructiveMigrationFrom(1, 2 , 3).build();
               
               _ins   = instance;
            }
            return instance;
         }
      }
   }
}