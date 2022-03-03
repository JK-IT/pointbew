package desoft.studio.webpoint

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Bitmap
import android.net.*
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.*
import android.webkit.*
import android.widget.FrameLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.android.material.bottomsheet.BottomSheetDialog

private const val tagg = "WEB PAGES ACTIVITY";

class WebPagesActivity : AppCompatActivity() {

    private var TAG= "-wpoint- ;=; WEB PAGES ACTIVITY ;=;"

    private var uihandler = Handler(Looper.getMainLooper());
    private lateinit var connecman : ConnectivityManager;
    private var currDefaNetwork : Network? = null;
    private var nowifiBotdia : BottomSheetDialog?=null;

    private lateinit var rootframlout : FrameLayout;

    private var webv : WebView? = null;
    private var spinner : ProgressBar? = null;
    private var currUrl : String? = "";
    private var originUrl : String? = null;
    private var failedToLoad : Boolean = false;


    @SuppressLint("SetJavaScriptEnabled")
    /**
    * *                 onCreate
    */
    override fun onCreate(savedInstanceState: Bundle?)
    {
        //Log.i(TAG, "onCreate: webpages created");
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)

        originUrl = intent.getStringExtra(webUrl).toString();

            //. set up connection manager
        connecman = getSystemService(ConnectivityManager::class.java);
        currDefaNetwork = connecman.activeNetwork;
        //. register network callback
        connecman.registerDefaultNetworkCallback(defNetCb, uihandler);
            //set show ads
        spinner = findViewById(R.id.spin_bar);
        MobileAds.initialize(this);
        var adquest = AdRequest.Builder().build();
        var adview : com.google.android.gms.ads.AdView = findViewById(R.id.webview_adview);
        adview.loadAd(adquest);
            //.support bar
        setSupportActionBar(findViewById(R.id.wv_toolbar));
        val actbar = supportActionBar;
        actbar?.apply {
            title = intent.getStringExtra(webName);
            setDisplayHomeAsUpEnabled(true);
            setDisplayShowHomeEnabled(true);
            setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_white);
        }
            //set up webview
        webv = findViewById<WebView>(R.id.webview);
        SetupWebview(webv!!);
        if(savedInstanceState == null){
            webv!!.post(Runnable {
                LoadWebpoint(webv!!, originUrl!!);
            })
        }
            //. root framelayout
        rootframlout = findViewById(R.id.wv_framelayout);

    }
    /**
    * *                 onStart
    */
    override fun onStart()
    {
        super.onStart();
    }
    
    override fun onSaveInstanceState(outState: Bundle)
    {
        webv?.saveState(outState);
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

    override fun onStop() {
        //. unregister network call back to stop leaking
        connecman.unregisterNetworkCallback(defNetCb);
        super.onStop();
    }

    override fun onResume() {
        super.onResume();
        webv?.onResume();
    }
    /**
    * *                         companion object
    */
    companion object{
        public const val webUrl = "WEB URL";
        public const val webName = "WEB NAME";
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
    // + --------->>-------->>--------->>*** -->>----------->>>>

    /**
    * *             NETWORK DEFAULT CALLBACK
    */
    private val defNetCb = object: ConnectivityManager.NetworkCallback(){
        override fun onAvailable(network: Network) {
            //Log.i(TAG, "onAvailable: DEAFAULT NETWORK IS AVAI");
            currDefaNetwork = network;
            if(nowifiBotdia!=null && nowifiBotdia?.isShowing == true) {
                nowifiBotdia?.dismiss();
            }
            webv?.reload();
        }

        override fun onLost(network: Network) {
            nowifiBotdia = BottomSheetDialog(this@WebPagesActivity);
            nowifiBotdia?.apply {
                setContentView(R.layout.dialog_bottom_nowifi);
            }
            currDefaNetwork = null;
            nowifiBotdia?.show();
        }
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
            useWideViewPort = false;
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
        v.loadUrl(tarurl);
    }

    /**
     * *    CLASS MYWEBVIEWCLI
     */
    inner class MywebviewCli : WebViewClient()
    {
        override fun onPageStarted(v: WebView?, url: String?, favicon: Bitmap?) {
            spinner!!.visibility = View.VISIBLE;
            failedToLoad = false;
        }

        public override fun onPageFinished(view: WebView?, url: String?) {
            spinner!!.visibility = View.GONE;
            currUrl = url;
        }

        override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
            return super.shouldOverrideUrlLoading(view, request)
        }
    }

    inner class MyChromeCli: WebChromeClient()
    {
        private var customView: View? = null;
        private var customViewCback: WebChromeClient.CustomViewCallback? = null;

        var fullayrams = WindowManager.LayoutParams().also {
            //it.flags = WindowManager.LayoutParams.FLAG_FULLSCREEN;
            it.width = WindowManager.LayoutParams.MATCH_PARENT;
            it.height = WindowManager.LayoutParams.MATCH_PARENT;
        }

        override fun onShowCustomView(paview: View?, callback: CustomViewCallback?)
        {
            //Log.i(TAG, "onShowCustomView: Custom view will be in fullscreen and custom view is null = ${customView == null}");
            if(customView!= null)
            {
                onHideCustomView();
                return;
            }
            customView = paview;
            rootframlout.addView(customView, fullayrams);
            customViewCback = callback;
            var wic = ViewCompat.getWindowInsetsController(rootframlout);
            //Log.d(TAG, "onShowCustomView: view compat is null = ${wic == null}");
            wic?.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE;
            wic?.hide(WindowInsetsCompat.Type.systemBars());
        }

        override fun onHideCustomView()
        {
            rootframlout.removeView(customView);
            customView = null;
            customViewCback?.onCustomViewHidden();
            customViewCback = null;
            var wic = ViewCompat.getWindowInsetsController(rootframlout);
            //Log.d(TAG, "on Hide customview: view compat is null = ${wic == null}");
            wic?.show(WindowInsetsCompat.Type.systemBars());
        }
    }

}