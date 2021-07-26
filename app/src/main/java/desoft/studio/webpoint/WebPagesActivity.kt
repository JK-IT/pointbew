package desoft.studio.webpoint

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.Insets
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.FrameLayout
import android.widget.Toast
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.android.material.dialog.MaterialAlertDialogBuilder

private const val tagg = "WEB PAGES ACTIVITY";

class WebPagesActivity : AppCompatActivity() {

    var webv : WebView? = null;
    var spinner : ProgressBar? = null;
    var tarUrl : String? = null;
    var failedToLoad : Boolean = false;

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_web_view)
        //set show ads
        MainActivity.showInter = true;
        spinner = findViewById(R.id.spin_bar);
        MobileAds.initialize(this);
        var adquest = AdRequest.Builder().build();
        var adview : com.google.android.gms.ads.AdView = findViewById(R.id.main_adview);
        adview.loadAd(adquest);

        setSupportActionBar(findViewById(R.id.wv_toolbar));
        val actbar = supportActionBar;
        
        actbar?.title = intent.getStringExtra(webName);
        actbar?.setDisplayHomeAsUpEnabled(true);
<<<<<<< HEAD
<<<<<<< pdro
=======
=======
>>>>>>> main
        actbar?.setDisplayShowHomeEnabled(true);

>>>>>>> 97250
        //set up webview
        webv = findViewById<WebView>(R.id.webview);
        SetupWebview(webv!!);
        if(savedInstanceState == null){
            webv!!.post(Runnable {
                tarUrl = intent.getStringExtra(webUrl);
                LoadWebpoint(webv!!, tarUrl!!);
            })
        }
    }
    
    override fun onConfigurationChanged(newConfig: Configuration)
    {
        super.onConfigurationChanged(newConfig)
    }
    
    override fun onSaveInstanceState(outState: Bundle)
    {
        super.onSaveInstanceState(outState)
        webv?.saveState(outState);
    }
    
    override fun onRestoreInstanceState(savedInstanceState: Bundle)
    {
        super.onRestoreInstanceState(savedInstanceState)
        webv?.restoreState(savedInstanceState);
    }
    
    companion object{
        public const val webUrl = "WEB URL";
        public const val webName = "WEB NAME";
    }
    
    override fun onCreateOptionsMenu(menu: Menu?): Boolean
    {
        var menuInflater = menuInflater;
        menuInflater.inflate(R.menu.webview_toolbar_menu, menu);
        return super.onCreateOptionsMenu(menu)
    }
    
    override fun onOptionsItemSelected(item: MenuItem): Boolean
    {
       when(item.itemId)
       {
           R.id.webview_menu_reload -> {
               if(failedToLoad)
               {
                   LoadWebpoint(webv!!, tarUrl!!);
               } else
               {
                   webv?.reload();
               }
           }
           R.id.webview_menu_open_browser->{
               var urluri : Uri = Uri.parse(webv?.url);
               if(urluri != null)
               {
                   startActivity(Intent(Intent.ACTION_VIEW, urluri));
               }
           }
           android.R.id.home->{
               Log.d(tagg, "Home Button is clicked");
               finish();
               return true;
           }
       }
       return super.onOptionsItemSelected(item);
    }
    
    override fun onBackPressed() {
        if(webv!!.canGoBack())
        {
            webv!!.goBack();
        } else
        {
            finish();
        }
    }

    override fun finish() {
        webv!!.clearCache(true);
        webv!!.clearHistory();
        super.finish()
    }
    
    /**
     *  ================================
     */
    @SuppressLint("SetJavaScriptEnabled")
    private fun SetupWebview(v: WebView)
    {
        v.settings.javaScriptEnabled = true;
        v.settings.domStorageEnabled = true;
        v.settings.useWideViewPort = true;
        v.settings.loadWithOverviewMode = true;
        v.webViewClient = MywebviewCli();
        v.webChromeClient = MyChromeCli();
    }
    
    private fun LoadWebpoint(v:WebView ,tarurl: String)
    {
        // checking for connection
        var cm:ConnectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager;
        Toast.makeText(this, "Loading $tarUrl", Toast.LENGTH_SHORT).show();
        val netObj = cm.activeNetwork;
        if(netObj != null)
        {
            val actnw = cm.getNetworkCapabilities(netObj);
            if(actnw!= null && actnw.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET))
            {
                v.loadUrl(tarurl);
            }
        }
        else
        {
            failedToLoad = true;
            MaterialAlertDialogBuilder(this).setTitle("No Connection")
                .setIcon(R.drawable.ic_baseline_wifi_off_black_24)
                .setMessage(R.string.no_connection).setNeutralButton("Ok"){
                    dia, _ ->
                    dia.dismiss();
                }.setCancelable(true).show();
        }
        // end checking for connection
    }
    
    /**
     * CUSTOM WEB VIEW CLIENT CLASS
     */
    inner class MywebviewCli : WebViewClient()
    {
        override fun onPageStarted(v: WebView?, url: String?, favicon: Bitmap?) {
            spinner!!.visibility = View.VISIBLE;
            failedToLoad = false;
            //v!!.visibility = View.INVISIBLE;
            super.onPageStarted(v, url, favicon)
        }
        
        public override fun onPageFinished(view: WebView?, url: String?) {
            spinner!!.visibility = View.GONE;
            //view!!.visibility = View.VISIBLE;
            super.onPageFinished(view, url)
        }
    }
    
    inner class MyChromeCli: WebChromeClient()
    {
        private var customView: View? = null;
        private var customViewCback: WebChromeClient.CustomViewCallback? = null;
        private var oriSysUIVisibility: Int? = null;
    
        override fun getDefaultVideoPoster(): Bitmap?
        {
            return super.getDefaultVideoPoster()
        }
    
        override fun onShowCustomView(paview: View?, callback: CustomViewCallback?)
        {
            super.onShowCustomView(paview, callback);
            Log.i(tagg, "Showing custom view");
            if(customView!= null)
            {
                onHideCustomView();
                return;
            }
            customView = paview;
            oriSysUIVisibility = this@WebPagesActivity.window.decorView.systemUiVisibility;
            customViewCback = callback;
            (this@WebPagesActivity.window.decorView as FrameLayout).addView(customView, FrameLayout.LayoutParams(-1, -1));
            this@WebPagesActivity.window.decorView.systemUiVisibility = 3846;
        }
    
        override fun onHideCustomView()
        {

            Log.i(tagg, "Hiding custom view");
            (this@WebPagesActivity.window.decorView as FrameLayout).removeView(customView);
            customView = null;
            this@WebPagesActivity.window.decorView.systemUiVisibility = oriSysUIVisibility!!;
            customViewCback?.onCustomViewHidden();
            customViewCback = null;
            super.onHideCustomView();
        }
    }
    
}