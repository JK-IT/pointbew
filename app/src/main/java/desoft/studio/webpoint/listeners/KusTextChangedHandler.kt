package desoft.studio.webpoint.listeners

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import android.widget.EditText

public class KusTextChangedHandler(var inputele : EditText, var typetocheck : String) : TextWatcher
{
   override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int)
   {
   
   }
   
   override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int)
   {
   
   }
   
   override fun afterTextChanged(s: Editable?)
   {
      Log.d("TextChange Listener", "${s.toString()} need to be checked")
      if(typetocheck.contentEquals("text"))
      if(inputele.text.toString().isBlank())
      {
         inputele.error = "Invalid name";
      }
      else if(typetocheck.contentEquals("url"))
      {
         if(!(Patterns.WEB_URL.matcher(s.toString()).matches()))
         {
            inputele.error = "Invalid Url";
         }
      }
   }
   
}