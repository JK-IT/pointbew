package desoft.studio.webpocket

import android.app.Application
import desoft.studio.webpocket.data.WpointDatabase

class Wpappli : Application()
{
    lateinit var appdb : WpointDatabase;
    override fun onCreate() {
        super.onCreate();
        appdb = WpointDatabase.GetDatabase(this);
    }
}