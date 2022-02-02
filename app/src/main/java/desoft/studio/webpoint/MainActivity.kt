package desoft.studio.webpoint

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.view.ActionMode
import androidx.recyclerview.selection.SelectionPredicates
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StorageStrategy
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.material.floatingactionbutton.FloatingActionButton
import desoft.studio.webpoint.data.Wpoint
import desoft.studio.webpoint.data.WpointVM
import kotlinx.coroutines.launch


private const val actiStack = "Main Activity Stack";

class MainActivity : AppCompatActivity() {

   private val TAG = "-wpoint- ==> MAIN ACTIVITY <==";
   private lateinit var navtroller : NavController;

   var kusdapter: KusAdapter? = null;
   private val pointdb : WpointVM by viewModels();
   private var tracker: SelectionTracker<String>? = null;
   private var fragview: View? = null;
   private var actMode : ActionMode? = null;
   private lateinit var actModecb : KustextualCb;
   private var mInterAds : InterstitialAd? = null;

   private val isActionModeWatcher = Observer<Boolean>{ value ->
      if(!value)
      {
         if (actMode != null) actMode = null;
      }
   }
   
   override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)
      setContentView(R.layout.activity_main);
      lifecycleScope.launch{
         pointdb.ReadPoints();
         //. set up navigation graph
         navtroller = (supportFragmentManager.findFragmentById(R.id.main_nav_host) as NavHostFragment).navController;
      }
      //. set up the  ad banner
      MobileAds.initialize(this);
      var adReq = AdRequest.Builder().build();
      var adview : com.google.android.gms.ads.AdView = findViewById(R.id.webview_adview);
      adview.loadAd(adReq);
      //. set up interads
      InterstitialAd.load(this, "ca-app-pub-3254181174406329/3766516003", adReq, object : InterstitialAdLoadCallback(){
         override fun onAdLoaded(p0: InterstitialAd) {
            mInterAds = p0;
         }
         override fun onAdFailedToLoad(p0: LoadAdError) {
            mInterAds = null;
         }
      })

      // init setup
      /*fragMan = supportFragmentManager;
      //fragview = findViewById(R.id.main_fragContainerView);
      setSupportActionBar(findViewById(R.id.main_toolbar));
      var intoview:TextView = findViewById(R.id.main_empty_data_view);
      var recyview : RecyclerView = findViewById(R.id.main_recyView);
      WpointFactory.isActionMode.observe(this, isActionModeWatcher);
      kusdapter = KusAdapter(this, recyview);
      
      WpointFactory.ReadAll.observe(this, Observer {
         kusdapter?.SetData(it);
         if(kusdapter?.GetAdapterData()?.isNotEmpty() == true)
         {
           intoview.visibility = View.GONE;
            recyview.visibility = View.VISIBLE;
         } else {
           intoview.visibility = View.VISIBLE;
            recyview.visibility = View.GONE;
         }
      })
      SetupRecyView(recyview);
      if(savedInstanceState != null)
      {
         tracker?.onRestoreInstanceState(savedInstanceState);
      }
      SetupFloatButt()*/
   }

   override fun onResume() {
      super.onResume();
      if(showInter)
      {
         mInterAds?.show(this);
         showInter = false;
      }

   }

   /*override fun onSaveInstanceState(outState: Bundle)
   {
      super.onSaveInstanceState(outState)
      tracker?.onSaveInstanceState(outState);
   }*/
   
   override fun onBackPressed()
   {
      super.onBackPressed()
   }
   // Setup recyclerview and data
   fun SetupRecyView(recy:RecyclerView)
   {
      recy.layoutManager = GridLayoutManager(applicationContext, 1);
      recy.adapter = kusdapter;
      // selection tracker
      tracker = SelectionTracker.Builder<String>(
            "MainFragPointList", recy, KusItemKeyTeller(kusdapter!!), KusItemLookup(recy), StorageStrategy.createStringStorage())
         .withSelectionPredicate(SelectionPredicates.createSelectAnything())
         .withOnItemActivatedListener{ item, e ->
            false;// set this false so event will not stop here, true = event will be stopped here, aka consumed
         }
         .build();
      kusdapter!!.SetTracker(tracker!!);
      actModecb = KustextualCb(this, R.menu.main_actbar_menu, kusdapter!!);
   }
   
   @SuppressLint("InflateParams")
   private fun SetupFloatButt()
   {
      val fbutt : FloatingActionButton =findViewById(R.id.frag_main_fab);
      fbutt.setOnClickListener {
         /*fragMan?.commit {
            setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            setReorderingAllowed(true);
            add<AddPointFrag>(R.id.main_fragContainerView);
            addToBackStack(actiStack); // this will handle back button press to pop off fragment
         }*/
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
      pointdb.DeleteMulti(items);
      kusdapter?.notifyDataSetChanged();
   }
   
   fun AddPointNDelete(delee: Boolean, oldpos:Int, item: Wpoint)
   {
      if(delee)
      {
         var oldPoint = kusdapter?.GetAdapterData()?.get(oldpos);
         pointdb.DeletePoint(oldPoint!!);
         pointdb.AddPoint(item);
      } else
      {
         pointdb.AddPoint(item);
      }
      kusdapter?.notifyDataSetChanged();
   }
   companion object{
      var showInter : Boolean = false;
   }
}