package desoft.studio.webpoint

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Bitmap
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.*
import android.webkit.*
import android.widget.FrameLayout
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.view.updatePadding
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.android.material.dialog.MaterialAlertDialogBuilder

private const val tagg = "WEB PAGES ACTIVITY";

class WebPagesActivity : AppCompatActivity() {

    private var TAG= "-wpoint- ;=; WEB PAGES ACTIVITY ;=;"


    private var uihandler = Handler(Looper.getMainLooper());
    private var fullscreenflag : Int = View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;

    private var webv : WebView? = null;
    private var spinner : ProgressBar? = null;
    private var originalUrl : String = "";
    private var currUrl : String? = "";
    private var uiFullPlaying: Boolean = false;
    private var tarUrl : String? = null;
    private var failedToLoad : Boolean = false;

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tarUrl = intent.getStringExtra(webUrl).toString();

        @Suppress("DEPRECATION")
        if(android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
            window.decorView.apply {
                //systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LOW_PROFILE;
            }
        }

        setContentView(R.layout.activity_web_view)
        //set show ads
        spinner = findViewById(R.id.spin_bar);
        MobileAds.initialize(this);
        var adquest = AdRequest.Builder().build();
        var adview : com.google.android.gms.ads.AdView = findViewById(R.id.webview_adview);
        adview.loadAd(adquest);

        if(savedInstanceState != null ) {
            uiFullPlaying = savedInstanceState.getBoolean(webFullPlaying, false);
        }

        setSupportActionBar(findViewById(R.id.wv_toolbar));
        val actbar = supportActionBar;

        actbar?.apply {
            title = intent.getStringExtra(webName);
            setDisplayHomeAsUpEnabled(true);
            setDisplayShowHomeEnabled(true);
            setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_white);
        }
        KF_REGISTER_UI_CHANGED_WATCHER();
        //set up webview
        webv = findViewById<WebView>(R.id.webview);
        SetupWebview(webv!!);
        if(savedInstanceState == null){
            webv!!.post(Runnable {
                LoadWebpoint(webv!!, tarUrl!!);
            })
        }
    }
    
    override fun onConfigurationChanged(newConfig: Configuration)
    {
        super.onConfigurationChanged(newConfig);
        Log.d(TAG, "onConfigurationChanged: = ${newConfig}");
        /*if(uiFullPlaying) {
            var cuvi = (webv?.webChromeClient as MyChromeCli).GetCustomView();
            if(cuvi != null ) {
                Log.d(TAG, "onConfigurationChanged: CUSTOM VIEW IS NULL = ${cuvi == null}");

            }
        }*/
    }
    
    override fun onSaveInstanceState(outState: Bundle)
    {
        webv?.saveState(outState);
        outState.putBoolean(webFullPlaying, uiFullPlaying);
        super.onSaveInstanceState(outState)
    }
    
    override fun onRestoreInstanceState(savedInstanceState: Bundle)
    {
        super.onRestoreInstanceState(savedInstanceState);
        webv?.restoreState(savedInstanceState);
    }

    override fun onPause() {
        webv?.onPause();
        super.onPause();
    }

    override fun onResume() {
        super.onResume();
        webv?.onResume();
    }
    
    companion object{
        public const val webUrl = "WEB URL";
        public const val webName = "WEB NAME";
        public const val webFullPlaying = "UI WEB FULL SCREEN PLAYING";
    }

    // + --------->>-------->>--------->>*** -->>----------->>>>
    /**
    * *                         onCreateOptionsMenu
    */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean
    {
        var menuInflater = menuInflater;
        menuInflater.inflate(R.menu.webview_toolbar_menu, menu);
        return super.onCreateOptionsMenu(menu)
    }
    /**
    * *                     onOptionsItemSelected
    */
    override fun onOptionsItemSelected(item: MenuItem): Boolean
    {
       when(item.itemId)
       {
           R.id.webview_menu_reload -> {
               /*if(failedToLoad)
               {
                   LoadWebpoint(webv!!,tarUrl!!);
               } else
               {
                   webv?.reload();
               }*/
               webv?.reload();
           }
           R.id.webview_menu_open_browser->{
               var urluri : Uri = Uri.parse(webv?.url);
               if(urluri != null)
               {
                   startActivity(Intent(Intent.ACTION_VIEW, urluri));
               }
           }
           android.R.id.home->{
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

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean
    {
        if(keyCode == KeyEvent.KEYCODE_BACK && webv?.canGoBack() == true) {
            webv?.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun finish() {
        webv!!.clearCache(true);
        webv!!.clearHistory();
        super.finish()
    }
    
// + --------->>-------->>--------->>*** -->>----------->>>>


    /**
    * *                         SetupWebview
    */
    private fun SetupWebview(v: WebView)
    {
        webv?.overScrollMode = WebView.OVER_SCROLL_IF_CONTENT_SCROLLS;
        v.settings.apply{
            javaScriptEnabled = true;
            domStorageEnabled = true;
            useWideViewPort = true;
            loadWithOverviewMode = true;
            setSupportZoom(true);
            layoutAlgorithm = WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING;
            loadsImagesAutomatically =  true;
            mediaPlaybackRequiresUserGesture = true;
        }
        webv?.evaluateJavascript("document.body.style.paddingBottom = '20px';", null);
        webv?.webViewClient = MywebviewCli();
        webv?.webChromeClient = MyChromeCli();
    }

    /**
    * *                 LoadWebpoint
    */
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
    // + --------->>-------->>--------->>*** -->>----------->>>>

    /**
    * *                 KF_REGISTER_UI_CHANGED_WATCHER
    */
    private fun KF_REGISTER_UI_CHANGED_WATCHER()
    {
        window.decorView.setOnSystemUiVisibilityChangeListener { uivisi ->
            if(uiFullPlaying) {
                uihandler.postDelayed(object : Runnable{
                    override fun run() {
                        Log.i(TAG, "Change ui back to full screen");
                        window.decorView.systemUiVisibility = fullscreenflag;
                    }
                }, 1500)
            }
            if(uivisi and View.SYSTEM_UI_FLAG_FULLSCREEN == 0) {
                Log.i(TAG, "KF_REGISTER_UI_CHANGED_WATCHER: Flag full screen is NOT on");
            } else {
                Log.i(TAG, "KF_REGISTER_UI_CHANGED_WATCHER: Flag full screen is ON");
            }
            if(uivisi and View.SYSTEM_UI_FLAG_HIDE_NAVIGATION == 0) {
                Log.i(TAG, "KF_REGISTER_UI_CHANGED_WATCHER: Flag Hide navigation is NOT on ");
            } else {
                Log.i(TAG, "KF_REGISTER_UI_CHANGED_WATCHER: Flag Hide navigation is ON ");
            }
            if(uivisi and View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION == 0) {
                Log.i(TAG, "KF_REGISTER_UI_CHANGED_WATCHER: Flag layout hide navigation is NOT on");
            } else {
                Log.i(TAG, "KF_REGISTER_UI_CHANGED_WATCHER: Flag layout hide navigation is ON");
            }
            if(uivisi and (View.SYSTEM_UI_FLAG_LAYOUT_STABLE) == 0) {
                Log.d(TAG, "KF_REGISTER_UI_CHANGED_WATCHER: Flag layout stable is NOT on");
            } else {
                Log.d(TAG, "KF_REGISTER_UI_CHANGED_WATCHER: Flag layout stable is ON");
            }
        }
    }

    // + --------->>-------->>--------->>*** -->>----------->>>>
    /**
     * *CUSTOM WEB VIEW CLIENT CLASS
     */
    inner class MywebviewCli : WebViewClient()
    {
        override fun onPageStarted(v: WebView?, url: String?, favicon: Bitmap?) {
            spinner!!.visibility = View.VISIBLE;
            failedToLoad = false;
            //v!!.visibility = View.INVISIBLE;
            //super.onPageStarted(v, url, favicon)
        }
        
        public override fun onPageFinished(view: WebView?, url: String?) {
            spinner!!.visibility = View.GONE;
            view?.scrollTo(0, 0)
            currUrl = url;
            view?.loadUrl("javascript:(function(){ document.body.style.paddingBottom = '10px'})();");
            //view!!.visibility = View.VISIBLE;
            //super.onPageFinished(view, url)
        }

        override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
            return super.shouldOverrideUrlLoading(view, request)
        }
    }
    
    inner class MyChromeCli: WebChromeClient()
    {
        private var customView: View? = null;
        private var customViewCback: WebChromeClient.CustomViewCallback? = null;
        private var oriSysUIVisibility: Int? = null;

        var fullayrams = WindowManager.LayoutParams().also {
            it.flags = WindowManager.LayoutParams.FLAG_FULLSCREEN;
            it.width = WindowManager.LayoutParams.MATCH_PARENT;
            it.height = WindowManager.LayoutParams.MATCH_PARENT;
        }

        override fun getDefaultVideoPoster(): Bitmap?
        {
            return super.getDefaultVideoPoster()
        }

        fun GetCustomView () : View?
        {
            return customView;
        }
    
        override fun onShowCustomView(paview: View?, callback: CustomViewCallback?)
        {
            Log.i(TAG, "onShowCustomView: Custom view will be in fullscreen and view is null = ${customView == null}");
            uiFullPlaying = true;
            //super.onShowCustomView(paview, callback);
            if(customView!= null)
            {
                onHideCustomView();
                return;
            }
            customView = paview;
            (this@WebPagesActivity.window.decorView as FrameLayout).addView(customView, fullayrams);
            oriSysUIVisibility = this@WebPagesActivity.window.decorView.systemUiVisibility;
            customViewCback = callback;
            //this@WebPagesActivity.window.decorView.systemUiVisibility = 3846;
            this@WebPagesActivity.window.decorView.systemUiVisibility = fullscreenflag;

        }
    
        override fun onHideCustomView()
        {
            Log.i(TAG, "onHideCustomView: Custom view is hide");
            uiFullPlaying = false;
            (this@WebPagesActivity.window.decorView as FrameLayout).removeView(customView);
            customView = null;
            this@WebPagesActivity.window.decorView.systemUiVisibility = oriSysUIVisibility!!;
            customViewCback?.onCustomViewHidden();
            customViewCback = null;
            super.onHideCustomView();
        }

        override fun onProgressChanged(view: WebView?, newProgress: Int) {
            //super.onProgressChanged(view, newProgress);
            //Log.d(TAG, "onProgressChanged: page load $newProgress");

        }

        override fun onConsoleMessage(consoleMessage: ConsoleMessage?): Boolean {
            Log.i(TAG, "onConsoleMessage: msg = ${consoleMessage?.message()};")
            return super.onConsoleMessage(consoleMessage)
        }
    }
    
}