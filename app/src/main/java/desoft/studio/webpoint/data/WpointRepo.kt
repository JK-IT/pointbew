package desoft.studio.webpoint.data

import androidx.lifecycle.LiveData

class WpointRepo(private val dao: Webpoint_Dao)
{
   val ReadAll : LiveData<List<Wpoint>> = dao.ReadAll();
   
   suspend fun AddPoint(point:Wpoint)
   {
      dao.AddPoint(point);
   }
   
   suspend fun DeletePoint(point: Wpoint)
   {
      dao.DeleteItem(point);
   }
   
   suspend fun DeleteMulti(polist : List<Wpoint>)
   {
      dao.DeleteMulti(polist);
   }
}