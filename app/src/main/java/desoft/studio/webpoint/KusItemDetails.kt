package desoft.studio.webpoint

import androidx.recyclerview.selection.ItemDetailsLookup
import android.util.Log

class KusItemDetails<T>(val adapPosition : Int, val itemId: T) : ItemDetailsLookup.ItemDetails<T>()
{
   val tag: String = "Kus_ItemDetails";
   override fun getPosition(): Int
   {
      //Log.d(tag, "GetPosition $adapPosition is touched, called from ViewHolder.getitemdetails");
      return adapPosition;
   }

   override fun getSelectionKey(): T?
   {
      //Log.d(tag, "GetSelectionKey $itemId is touched, called from ViewHolder.getitemdetails");
      return itemId as? T;
   }
}