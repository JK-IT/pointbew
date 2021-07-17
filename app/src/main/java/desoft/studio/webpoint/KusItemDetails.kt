package desoft.studio.webpoint

import androidx.recyclerview.selection.ItemDetailsLookup
import android.util.Log

class KusItemDetails<T>(val adapPosition : Int, val itemId: T) : ItemDetailsLookup.ItemDetails<T>()
{
   val tag: String = "Kus_ItemDetails";
   override fun getPosition(): Int
   {
      return adapPosition;
   }

   override fun getSelectionKey(): T?
   {
      return itemId as? T;
   }
}