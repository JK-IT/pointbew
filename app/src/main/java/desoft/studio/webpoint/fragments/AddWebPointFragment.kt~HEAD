package desoft.studio.webpoint.fragments

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.transition.TransitionInflater
import android.util.Log
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import desoft.studio.webpoint.R
import desoft.studio.webpoint.data.Wpoint
import desoft.studio.webpoint.data.WpointVM


import java.lang.Exception
import java.util.*
class AddWebPointFragment() :Fragment()
{

   /*-------------------------------------------*/
<<<<<<< pdro:app/src/main/java/desoft/studio/webpoint/AddWebPointFragment.kt
   
=======
>>>>>>> 97250:app/src/main/java/desoft/studio/webpoint/fragments/AddWebPointFragment.kt
   private var addButt: Button? = null;
   private var cancelButt: Button? = null;
   private val vmodel : WpointVM by activityViewModels();
   
   override fun onCreate(savedInstanceState: Bundle?)
   {
      super.onCreate(savedInstanceState)
<<<<<<< pdro:app/src/main/java/desoft/studio/webpoint/AddWebPointFragment.kt
=======
      arguments?.let {      };
      enterTransition = TransitionInflater.from(requireContext()).inflateTransition(R.transition.slide_right);
>>>>>>> 97250:app/src/main/java/desoft/studio/webpoint/fragments/AddWebPointFragment.kt
   }

   override fun onCreateView(      inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
   {
      // Inflate the layout for this fragment
      return inflater.inflate(R.layout.frag_add_new_webpoint, container, false)
   }
<<<<<<< pdro:app/src/main/java/desoft/studio/webpoint/AddWebPointFragment.kt
   
=======

>>>>>>> 97250:app/src/main/java/desoft/studio/webpoint/fragments/AddWebPointFragment.kt
   override fun onViewCreated(view: View, savedInstanceState: Bundle?)
   {
      super.onViewCreated(view, savedInstanceState);
      SetupButton(view);
   }

   private fun SetupButton(v: View)
   {
      addButt = v.findViewById(R.id.frag_addAddButton);
      addButt?.isEnabled = false;
      cancelButt = v.findViewById(R.id.frag_addCancelButton);
      val urlinput : EditText = v.findViewById(R.id.frag_add_urlInput);
      val nameinput: EditText = v.findViewById(R.id.frag_add_NameInput);
      
      nameinput.addTextChangedListener(afterTextChanged = {
         if(nameinput.text.toString().isBlank())
         {
            if(addButt!!.isEnabled)  addButt?.isEnabled =false;
            nameinput.error = "Invalid name";
         } else {
            if(Patterns.WEB_URL.matcher(urlinput.text.toString()).matches())
               addButt?.isEnabled = true;
         }
      })
      urlinput.addTextChangedListener(object: TextWatcher
      {
         override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int){}
         
         override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
         override fun afterTextChanged(s: Editable?)
         {
            if(!(Patterns.WEB_URL.matcher(s.toString()).matches()))
            {
               urlinput.error = "Invalid Url";
               addButt?.isEnabled = false;
            } else
            {
               if( nameinput.text.toString().isBlank())
               {
                  nameinput.error = "Invalid Name";
                  addButt?.isEnabled = false;
               }
               else  addButt?.isEnabled = true;
            }
         }
      })

      addButt!!.setOnClickListener {
         try
         {
            val point = Wpoint(nameinput.text.toString().uppercase(), urlinput.text.toString());
            vmodel.AddPoint(point);
            parentFragmentManager.popBackStack();
            Toast.makeText(context, "Successfully Added", Toast.LENGTH_SHORT).show();
         }
         catch (e: Exception)
         {
            e.printStackTrace();
         }
      }
      
      cancelButt!!.setOnClickListener{
         parentFragmentManager.popBackStack();
      }
   }
   
   companion object
   {
      @JvmStatic
      fun newInstance() =
         AddWebPointFragment();
   }
   
   
}