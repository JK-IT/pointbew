package desoft.studio.webpoint

import android.app.Application
import desoft.studio.webpoint.data.WpointDatabase

class Wpappli : Application()
{
    lateinit var appdb : WpointDatabase;
    override fun onCreate() {
        super.onCreate();
        appdb = WpointDatabase.GetDatabase(this);
    }
}