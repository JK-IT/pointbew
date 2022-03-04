package desoft.studio.webpocket

import android.view.MotionEvent
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.widget.RecyclerView

class KusItemLookup (val recyview: RecyclerView) : ItemDetailsLookup<String> ()
{
   val tag : String = "KusItem LookUP";
   override fun getItemDetails(e: MotionEvent): KusItemDetails<String>?
   {
      val v = recyview.findChildViewUnder(e.x, e.y);
      if(v != null)
      {
         return (recyview.getChildViewHolder(v) as KusAdapter.ViewHolder).GetItemDetails();
      }
      return null;
   }
}