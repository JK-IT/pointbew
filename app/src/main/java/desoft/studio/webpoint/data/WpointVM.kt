package desoft.studio.webpoint.data

import android.app.Application
import androidx.lifecycle.*
import androidx.recyclerview.selection.Selection
import desoft.studio.webpoint.sharedStore.KusAdsManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WpointVM(appli: Application) : AndroidViewModel(appli)
{
   private val repo:WpointRepo;
   private val adsrepo : KusAdsManager;

   val ReadAll: LiveData<List<Wpoint>>;
   
   var isActionMode: LiveData<Boolean>;
   
   init
   {
      val pointDao = WpointDatabase.GetDatabase(appli).WpointDao;
      repo = WpointRepo(pointDao);
      adsrepo = KusAdsManager(appli);
      ReadAll = repo.ReadAll;
      isActionMode = MutableLiveData<Boolean>();
   }
   
   fun AddPoint(point: Wpoint)
   {
      // dispatcher.io == run in background thread
      viewModelScope.launch(Dispatchers.IO) { repo.AddPoint(point) };
   }

   fun DeletePoint(point: Wpoint)
   {
      viewModelScope.launch ( Dispatchers.IO ){ repo.DeletePoint(point)};
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