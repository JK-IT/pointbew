package desoft.studio.webpoint

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class SplashActi : AppCompatActivity()
{
   override fun onCreate(savedInstanceState: Bundle?)
   {
      super.onCreate(savedInstanceState)
      //setContentView(R.layout.activity_splash)
      startActivity(Intent(this, MainActivity::class.java));
      finish();
   }
}