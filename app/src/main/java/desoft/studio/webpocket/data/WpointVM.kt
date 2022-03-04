package desoft.studio.webpocket.data

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import desoft.studio.webpocket.Wpappli
import desoft.studio.webpocket.sharedStore.KusAdsManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class WpointVM(appli: Application) : AndroidViewModel(appli)
{
   private val TAG = "-wpoint- View Model";
   val pointDao = (appli as Wpappli).appdb.WpointDao;
   private val repo:WpointRepo = WpointRepo(pointDao);
   private val adsrepo : KusAdsManager = KusAdsManager(appli);

   var livePoint: MutableLiveData<List<Wpoint>?> = MutableLiveData<List<Wpoint>?>();
   
   var isActionMode: LiveData<Boolean> = MutableLiveData<Boolean>();
   
   init
   {
      Log.i(TAG, "View model init");
   }

   fun ReadPoints()
   {
      viewModelScope.launch() {
          livePoint.value = repo.ReadAll();
      }
   }

   fun AddPoint(point: Wpoint)
   {
      viewModelScope.launch {
         withContext(Dispatchers.IO) {
            repo.AddPoint(point);
         }
         var temp = (ArrayList<Wpoint>(livePoint.value));
         temp.add(point);
         livePoint.value = temp;
      };
   }

   fun DeletePoint(point: Wpoint)
   {
      viewModelScope.launch (  ){
         withContext(Dispatchers.IO) {
            repo.DeletePoint(point);
         }
         var temp = (ArrayList<Wpoint>(livePoint.value));
         temp.remove(point);
         livePoint.value = temp;
      };
   }
   
   fun DeleteMulti(polist : List<Wpoint>)
   {
      viewModelScope.launch (Dispatchers.Default) { repo.DeleteMulti(polist) };
   }

   fun SetInterAds(value: Boolean)
   {
      viewModelScope.launch(Dispatchers.Default) {
         adsrepo.SetInterAds(value);
      }
   }

   val ReadShowInterAds = adsrepo.showInterAds.asLiveData();
}