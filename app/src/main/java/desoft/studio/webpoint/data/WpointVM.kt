package desoft.studio.webpoint.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.selection.Selection
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WpointVM(appli: Application) : AndroidViewModel(appli)
{
   private val repo:WpointRepo;
   
   val ReadAll: LiveData<List<Wpoint>>;
   
   var isActionMode: LiveData<Boolean>;
   
   init
   {
      val pointDao = WpointDatabase.GetDatabase(appli).WpointDao;
      repo = WpointRepo(pointDao);
      ReadAll = repo.ReadAll;
      isActionMode = MutableLiveData<Boolean>();
   }
   
   fun AddPoint(point: Wpoint)
   {
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
}