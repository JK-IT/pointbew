package desoft.studio.webpoint

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
import desoft.studio.webpoint.data.Wpoint
import desoft.studio.webpoint.data.WpointVM


import java.lang.Exception
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private const val tagg = "ADD POINT FRAGMENT";

/**
 * A simple [Fragment] subclass.
 * Use the [AddWebPointFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

class AddWebPointFragment() :Fragment()
{
   // TODO: Rename and change types of parameters
   private var param1: String? = null
   private var param2: String? = null
   
   /*-------------------------------------------*/
   
   
   private var addButt: Button? = null;
   private var cancelButt: Button? = null;
   private val vmodel : WpointVM by activityViewModels();
   
   override fun onCreate(savedInstanceState: Bundle?)
   {
      super.onCreate(savedInstanceState)
      arguments?.let {
         param1 = it.getString(ARG_PARAM1)
         param2 = it.getString(ARG_PARAM2)
      }
   }
   override fun onCreateView(
      inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?
   ): View?
   {
      // Inflate the layout for this fragment
      return inflater.inflate(R.layout.frag_add_new_webpoint, container, false)
   }
   

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
            //todo: add more validation here
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
            /*val imm = context?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager;
            imm.hideSoftInputFromWindow(v.windowToken,0 )*/
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
         /*val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager;
         imm.hideSoftInputFromWindow(v.windowToken,0 )*/
         parentFragmentManager.popBackStack();
      }
   }
   
   companion object
   {
      /**
       * Use this factory method to create a new instance of
       * this fragment using the provided parameters.
       *
       * @param param1 Parameter 1.
       * @param param2 Parameter 2.
       * @return A new instance of fragment AddWebPointFrag.
       */
      // TODO: Rename and change types and number of parameters
      @JvmStatic
      fun newInstance() =
         AddWebPointFragment()
            /*.apply {
            arguments = Bundle().apply {
               putString(ARG_PARAM1, param1)
               putString(ARG_PARAM2, param2)
            }*/
         
   }
   
   
}