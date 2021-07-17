package desoft.studio.webpoint

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_view_test.*

class ViewTest_Activity : AppCompatActivity()
{
   override fun onCreate(savedInstanceState: Bundle?)
   {
      super.onCreate(savedInstanceState)
      setContentView(R.layout.activity_view_test)
      setSupportActionBar(main_toolbar);
   }
}