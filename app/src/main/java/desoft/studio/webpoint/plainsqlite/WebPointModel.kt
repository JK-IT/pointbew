package desoft.studio.webpoint.plainsqlite

import android.util.Log

class WebPointModel()
{
   
   var name:String? = null
      get() = field;
      set(value) {field = value};
   
   var url:String? = null
      get() = field;
      set(value) {field = value};
   
   
   constructor(namepar:String , urlpar: String): this()
   {
      name = namepar; url = urlpar;
   }
   
   public fun GetString() : String{
      return ("name: $name, url: $url");
   }
}