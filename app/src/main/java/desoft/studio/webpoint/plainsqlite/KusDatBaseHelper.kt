package desoft.studio.webpoint.plainsqlite

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

private const val tagg = "KusBASE HELPER";

class KusDatBaseHelper(ctx: Context): SQLiteOpenHelper(ctx, "webpoint.db", null, 1)
{
   private val TABNAME: String = "WEBPOINT_TABLE"
   private val NAMECOL: String = "NAME";
   private val URLCOL: String = "URL";
   
   
   override fun onCreate(db: SQLiteDatabase?)
   {
      val creStat : String = "CREATE TABLE $TABNAME ( ID INTEGER PRIMARY KEY AUTOINCREMENT ,NAME TEXT NOT NULL,  URL BLOB NOT NULL)";
      db?.execSQL(creStat);
   }
   
   override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int)
   {
      TODO("Not yet implemented")
   }
   
   public fun AddOne(model: WebPointModel): Boolean
   {
      val db:SQLiteDatabase = this.writableDatabase;
      val cv: ContentValues = ContentValues();
      
      cv.put(NAMECOL, model.name);
      cv.put(URLCOL, model.url);
      
      // return -1 = failure else return rowid of newly inserted row
      return db.insert(TABNAME, null, cv) >= 0;
   }
   
   public fun ReadAll() : List<WebPointModel>?
   {
      val weblist : MutableList<WebPointModel> = ArrayList<WebPointModel>();
      val que : String = "SELECT * FROM $TABNAME";
      val dab: SQLiteDatabase = readableDatabase;
      val cur: Cursor =  dab.rawQuery(que, null);
      
      if(cur.moveToFirst())
      {
         while( ! cur.isAfterLast)
         {
            val model: WebPointModel = WebPointModel(cur.getString(cur.getColumnIndex(NAMECOL)), cur.getString(cur.getColumnIndex(URLCOL)));
            weblist.add(model);
            cur.moveToNext();
            
         }
         cur.close();
         dab.close();
      }else
      {
         cur.close();
         dab.close();
         return null;
         
      }
      
      return weblist;
   }
   
   public fun ReadOne() : WebPointModel?
   {
      val datba: SQLiteDatabase = readableDatabase;
      val str: String = "SELECT * FROM $TABNAME ORDER BY RANDOM() LIMIT 1";
      val cur: Cursor=  datba.rawQuery(str, null);
   
      var model: WebPointModel? = null;
      if(cur.moveToFirst())
      {
         model= WebPointModel(cur.getString(cur.getColumnIndex(NAMECOL)),
               cur.getString(cur.getColumnIndex(URLCOL)));
      }
      else return null;
      cur.close();
      datba.close();
      //Log.d(tagg, "this is model i got ${model!!.GetString()}")
      return model;
   }
}