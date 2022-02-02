package desoft.studio.webpoint.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.transition.TransitionInflater
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.google.android.material.textfield.TextInputLayout
import desoft.studio.webpoint.R
import desoft.studio.webpoint.data.Wpoint
import desoft.studio.webpoint.data.WpointVM
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


import java.lang.Exception

class AddPointFrag() :Fragment()
{
   private val TAG = "-wpoint- =;;= ADD POINT FRAGMENT =;;=";
   private lateinit var navtroller :NavController;
   /*-------------------------------------------*/
   private lateinit var addButt: Button;
   private lateinit var cancelButt: Button;
   private val vmodel : WpointVM by activityViewModels();
   
   override fun onCreate(savedInstanceState: Bundle?)
   {
      super.onCreate(savedInstanceState)
      arguments?.let {      };
      enterTransition = TransitionInflater.from(requireContext()).inflateTransition(R.transition.slide_right);
   }

   override fun onCreateView(      inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
   {
      // Inflate the layout for this fragment
      return inflater.inflate(R.layout.frag_add_new_webpoint, container, false)
   }

   override fun onViewCreated(view: View, savedInstanceState: Bundle?)
   {
      super.onViewCreated(view, savedInstanceState);
      navtroller = view.findNavController();
      SetupButton(view);
   }

   private fun SetupButton(v: View)
   {
      addButt = v.findViewById(R.id.frag_addAddButton);
      cancelButt = v.findViewById(R.id.frag_addCancelButton);
      val urlinput : EditText = v.findViewById(R.id.frag_add_urlInput);
      val nameinput: EditText = v.findViewById(R.id.frag_add_NameInput);

      addButt.setOnClickListener {
         if(nameinput.text.isNullOrBlank()) {
            (v.findViewById<TextInputLayout>(R.id.frag_add_name_lout)).error = "This field is required";
            return@setOnClickListener
         } else {
            (v.findViewById<TextInputLayout>(R.id.frag_add_name_lout)).error = null;
         }
         if(urlinput.text.isNullOrBlank()) {
            (v.findViewById<TextInputLayout>(R.id.frag_add_url_lout)).error = "This field is required";
            return@setOnClickListener;
         }
         else if(Patterns.WEB_URL.matcher(urlinput.text).matches() == false) {
            (v.findViewById<TextInputLayout>(R.id.frag_add_url_lout)).error = "Invalid url";
            return@setOnClickListener;
         }
         else {
            (v.findViewById<TextInputLayout>(R.id.frag_add_url_lout)).error = null;
         }
         val point = Wpoint(nameinput.text.toString().uppercase(), urlinput.text.toString());
         lifecycleScope.launch {
            withContext(Dispatchers.Main) {
               vmodel.AddPoint(point);
            }
            Toast.makeText(context, "Successfully Added", Toast.LENGTH_SHORT).show();
            navtroller.navigate(R.id.action_addPointFrag_to_mainFrag);
         }
      }
      cancelButt!!.setOnClickListener{
         navtroller.navigate(R.id.action_addPointFrag_to_mainFrag);
      }
   }
   
   companion object
   {
      @JvmStatic
      fun newInstance() =
         AddPointFrag();
   }
   
   
}