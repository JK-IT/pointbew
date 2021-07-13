package desoft.studio.webpoint

import android.os.Debug
import android.util.Log
import androidx.recyclerview.selection.ItemKeyProvider

private const val tag = "KUST ITEM KEY TELLER";
class KusItemKeyTeller(val kusdapter: KusAdapter): ItemKeyProvider<String>(SCOPE_CACHED)
{

   val tag : String = "Kus_ItemTeller";
   override fun getKey(position: Int): String?
   {
      Log.d(tag, "GetKey of $position that is long pressed");
      // the key will be the name cuz, we need the name to find the url in database
      return kusdapter.GetAdapterData()[position].Name;
   }

   override fun getPosition(key: String): Int
   {
      Log.d(tag, "GetPosition of $key that is long pressed");
      return kusdapter.GetAdapterData().indexOfFirst { it.Name == key };
   }
}
/*
getKey(pos: Int): Long?
   return recyview.adapter?.getItemId(posi)

getPosition(key:Long): Int
   val viewholder = recyview.findViewHolderForItemId(key);
   return viewholder?.layoutPosition ? : RecyclerView.NO_POSITION
 */