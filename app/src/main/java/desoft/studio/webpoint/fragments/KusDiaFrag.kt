package desoft.studio.webpoint.fragments

import android.app.Dialog
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment

class KusDiaFrag(var viewid : Int) : DialogFragment()
{
   private lateinit var viewsetup : SetupView;
   
   fun SetViewHandler(insetup : SetupView){
      viewsetup = insetup;
   }
   
   
   
   override fun onCreateDialog(savedInstanceState: Bundle?): Dialog
   {
      return super.onCreateDialog(savedInstanceState)
   }
   
   override fun onCreate(savedInstanceState: Bundle?)
   {
      super.onCreate(savedInstanceState);
      
   }
   
   override fun onStart()
   {
      super.onStart()
      var width = (resources.displayMetrics.widthPixels).toInt();
      dialog!!.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT);
      dialog!!.window?.setWindowAnimations(android.R.style.Animation_Dialog);
   }
   
   override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                             savedInstanceState: Bundle?): View?
   {
      val vi :View = inflater.inflate(viewid, container, false);
      return vi;
   }
   
   override fun onViewCreated(v: View, savedInstanceState: Bundle?)
   {
      viewsetup.SetViewUI(v)
   }
   
   override fun onConfigurationChanged(newConfig: Configuration)
   {
      super.onConfigurationChanged(newConfig)
      dialog!!.window?.setLayout((resources.displayMetrics.widthPixels), ViewGroup.LayoutParams.WRAP_CONTENT);
   }
   
   init
   {
   
   }
   
   companion object{
      const val tagg = "Kus Dialog Fragment";
   }
   
   public interface SetupView
   {
      fun SetViewUI(v:View);
   }
}

