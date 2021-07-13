package desoft.studio.webpoint

import android.annotation.SuppressLint
import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.view.ActionMode
import androidx.fragment.app.*
import androidx.recyclerview.selection.SelectionPredicates
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StorageStrategy
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.lifecycle.Observer
import com.google.android.material.floatingactionbutton.FloatingActionButton
import desoft.studio.webpoint.data.Wpoint
import desoft.studio.webpoint.data.WpointVM
import kotlin.math.log

private const val tagg = "MAIN ACTIVITY";
private const val actiStack = "Main Activity Stack";

class MainActivity : AppCompatActivity() {
   
   var fragMan : FragmentManager? = null;
   var kusdapter: KusAdapter? = null;
   private val WpointFactory : WpointVM by viewModels();
   private var tracker: SelectionTracker<String>? = null;
   private var fragview: View? = null;
   private var actMode : ActionMode? = null;
   private lateinit var actModecb : KustextualCb;
   
   private val isActionModeWatcher = Observer<Boolean>{ value ->
      if(!value)
      {
         Log.d(tagg, "isActionmode Watcher is called");
         if (actMode != null) actMode = null;
      }
   }

   //----------------------- Activity Methods starting here
   
   override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)
      setContentView(R.layout.activity_main)
      // init setup
      fragMan = supportFragmentManager;
      fragview = findViewById(R.id.main_fragContainerView);
      setSupportActionBar(findViewById(R.id.main_toolbar));
      WpointFactory.isActionMode.observe(this, isActionModeWatcher);
      
      // view setup
      SetupView();
      if(savedInstanceState != null)
      {
         // restore selection here
         tracker?.onRestoreInstanceState(savedInstanceState);
      }
      SetupFloatButt()
   }
   
   override fun onSaveInstanceState(outState: Bundle)
   {
      super.onSaveInstanceState(outState)
      tracker?.onSaveInstanceState(outState);
   }
   
   override fun onBackPressed()
   {
      super.onBackPressed()
   }

   // ------------------------- MY METHODS -----------------------------
   
   // Setup recyclerview and data
   fun SetupView()
   {
      val recy: RecyclerView = findViewById(R.id.main_recyView);
      recy.layoutManager = GridLayoutManager(applicationContext, 1);
      // get data list from database
      kusdapter = KusAdapter(this); // passing this will be understand as activity context
      recy.adapter = kusdapter;
      WpointFactory.ReadAll.observe(this, Observer {
         kusdapter?.SetData(it);
      })
      
      // selection tracker
      tracker = SelectionTracker.Builder<String>(
            "MainFragPointList", recy, KusItemKeyTeller(kusdapter!!), KusItemLookup(recy), StorageStrategy.createStringStorage())
         .withSelectionPredicate(SelectionPredicates.createSelectAnything())
         .withOnItemActivatedListener{ item, e ->
            Log.d(tagg, "Item selected ${item.position} , key ${item.selectionKey}");
            false;// set this false so event will not stop here, true = event will be stopped here, aka consumed
         }
         .build();
      kusdapter!!.SetTracker(tracker!!);
      actModecb = KustextualCb(this, R.menu.main_actbar_menu, kusdapter!!);
   }
   
   @SuppressLint("InflateParams")
   private fun SetupFloatButt()
   {
      val fbutt : FloatingActionButton =findViewById(R.id.mainFab);
      fbutt.setOnClickListener { //Snackbar.make(it, "sup", Snackbar.LENGTH_LONG) .setAction("Action", null).show();
   
         fragMan?.commit {
            setReorderingAllowed(true);
            add<AddWebPointFragment>(R.id.main_fragContainerView);
            addToBackStack(actiStack); // this will handle back button press to pop off fragment
         }
      }
   }
   
   // call start action mode
   fun StartTextualMode()
   {
      if(actMode == null){
         actMode =  startSupportActionMode(actModecb);
         kusdapter?.SetActModeState(true);
      }
   }
   
   fun StopTextualMode()
   {
      actMode?.finish();
      actMode = null;
      kusdapter?.SetActModeState(false);
   }
   
   fun DeleteMultiWpoin(items : List<Wpoint>)
   {
      WpointFactory.DeleteMulti(items);
      kusdapter?.notifyDataSetChanged();
   }
   
   fun DeleteWpoin(wp : Wpoint)
   {
      Toast.makeText(applicationContext, "Delete is called ${wp.Name} - ${wp.Url}", Toast.LENGTH_SHORT).show();
      WpointFactory.DeletePoint(wp);
      kusdapter?.notifyDataSetChanged();
   }
   
   fun AddPointNDelete(delee: Boolean, oldpos:Int, item: Wpoint)
   {
      Log.i(tagg, "Calling add and deleting $delee")
      if(delee)
      {
         var oldPoint = kusdapter?.GetAdapterData()?.get(oldpos);
         WpointFactory.DeletePoint(oldPoint!!);
         WpointFactory.AddPoint(item);
      } else
      {
         WpointFactory.AddPoint(item);
      }
      kusdapter?.notifyDataSetChanged();
   }
   
}


/**
      All code for getting button and implement intent

      val smabutt = findViewById<Button>(R.id.sell_myapp);
      smabutt.setOnClickListener {
      val inte = Intent(this, WebPages::class.java);
      inte.putExtra("urltoopen", getString(R.string.sellmyapp_url));
      inte.putExtra("urlname", getString(R.string.sell_myapp));
      startActivity(inte);
      }
 */

/**
.withOnItemActivatedListener { item, e ->
//Log.d(tagg, "Item selected ${item.position} , key ${item.selectionKey}");
val cv: View? = recy.findChildViewUnder(e.x, e.y);
Log.d(tagg, "Childview clicked coor ${e.x} - ${e.y}- index -> ${recy.indexOfChild(cv!!)}");
val delegateArea = Rect();
val editbutt : ImageButton = cv.findViewById<ImageButton>(R.id.edit_button).apply{
isEnabled = true;
setOnClickListener{
Log.d(tagg, "Edit button is clicked");
}
getHitRect(delegateArea);
}
delegateArea.top += 100;
delegateArea.bottom += 100;
Log.d(tagg, "delegateArea ${delegateArea.width()}- ${delegateArea.height()}, editbutton width ${editbutt.width} - ${editbutt.height}, parent width ${cv.width} - ${cv.height}");
(editbutt.parent as? View)?.apply {
touchDelegate = TouchDelegate(delegateArea, editbutt);
}
true;
}
      */