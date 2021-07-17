package desoft.studio.webpoint

import android.content.Context
import android.util.Log
import androidx.appcompat.view.ActionMode
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Switch
import androidx.annotation.MenuRes
import androidx.recyclerview.selection.SelectionTracker
import desoft.studio.webpoint.data.Wpoint
import desoft.studio.webpoint.data.WpointVM

private const val tagg = "Kus Contextual Callback";

class KustextualCb(val ctx : Context, @MenuRes val menuId: Int, val viadapter: KusAdapter) : ActionMode.Callback
{
   
   override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean
   {
      mode?.menuInflater?.inflate(menuId, menu);
      return true;
   }
   
   override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean
   {
      return false;
   }
   
   override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean
   {
      return when(item?.itemId){
         R.id.menu_delete ->{
            if(viadapter.selectedSet.isNotEmpty())
            {
               (ctx as MainActivity).DeleteMultiWpoin(viadapter.selectedSet.toList())
            }
            (ctx as MainActivity).StopTextualMode();
            false;
         }
         else -> false;
      }
   }
   
   override fun onDestroyActionMode(mode: ActionMode?)
   {
      viadapter.GetTracker().clearSelection();
      viadapter.selectedSet.clear();
   }
}