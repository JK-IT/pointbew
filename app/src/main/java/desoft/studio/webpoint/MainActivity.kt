package desoft.studio.webpoint

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toolbar
import androidx.activity.viewModels
import androidx.appcompat.view.ActionMode
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
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

   private val pointdb : WpointVM by viewModels();
   private var mInterAds : InterstitialAd? = null;
   
   override fun onCreate(savedInstanceState: Bundle?) {

      super.onCreate(savedInstanceState);
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
      // . set up action bar
      var tb = findViewById<androidx.appcompat.widget.Toolbar>(R.id.main_toolbar);
      setSupportActionBar(tb);
   }

   override fun onResume() {
      super.onResume();
   }
   override fun onBackPressed()
   {
      super.onBackPressed()
   }
}