package desoft.studio.webpoint

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import com.google.common.util.concurrent.ListenableFuture
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import desoft.studio.webpoint.fragments.AddPointDialogFrag
import desoft.studio.webpoint.fragments.MainFrag
import java.lang.Exception

class SkanActivity : AppCompatActivity() {

    private val TAG: String = "-wpoint- <;;> SKAN ACTIVITY <;;>";

    private var fromWhere : String? = null;

    private val camPermCheckLauncher = KF_CHECK_CAMERA_PERM();
    private lateinit var camProviderFuture: ListenableFuture<ProcessCameraProvider>;
    private lateinit var camProvider: ProcessCameraProvider;
    private var viewFinder : PreviewView?=null;
    private var kamera : Camera? = null;

    private lateinit var barcodeScanner : BarcodeScanner;
    private var imgAnalysis: ImageAnalysis?=null;

    private var webinte:Intent?=null; //
    /**
    * *             onCreate
    */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(window, false);
        setContentView(R.layout.activity_skan);

        if(intent != null ) {
            //Log.i(TAG, "onCreate: get this from intent ${intent.getStringExtra(fromWhereKey)}");
            fromWhere = intent.getStringExtra(fromWhereKey);
        }

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_IMMERSIVE or View.SYSTEM_UI_FLAG_LAYOUT_STABLE;

        //. set up webpages inte
        webinte = Intent(this, WebPagesActivity::class.java);
        // set up camera surface
        viewFinder = findViewById<PreviewView>(R.id.skan_cam_preview);
        viewFinder?.apply {
            implementationMode = PreviewView.ImplementationMode.PERFORMANCE;
            scaleType = PreviewView.ScaleType.FILL_CENTER;
        }
        camPermCheckLauncher.launch(android.Manifest.permission.CAMERA);
        barcodeScanner = BarcodeScanning.getClient(BarcodeScannerOptions.Builder()
            .setBarcodeFormats(Barcode.FORMAT_QR_CODE, Barcode.FORMAT_AZTEC)
            .setExecutor(ContextCompat.getMainExecutor(this)).build());
        KF_CAMERA_INIT();
    }

    override fun onStop() {
        imgAnalysis?.clearAnalyzer();
        super.onStop();
    }
    // + --------->>-------->>--------->>*** -->>----------->>>>

    /**
    * *                 KF_CAMERA_INIT
    */
    @SuppressLint("UnsafeOptInUsageError")
    private fun KF_CAMERA_INIT()
    {
        camProviderFuture = ProcessCameraProvider.getInstance(this);
        camProviderFuture.addListener(Runnable {
            camProvider = camProviderFuture.get();


            // .  preview use case
            var preve : Preview = Preview.Builder().build();
            var camSelec : CameraSelector = CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_BACK).build();
            preve.setSurfaceProvider(viewFinder?.surfaceProvider);

            //. image analyzer use case
            imgAnalysis = ImageAnalysis.Builder().setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build();
            imgAnalysis !!.setAnalyzer(ContextCompat.getMainExecutor(this), ImageAnalysis.Analyzer { imgproxy ->
                var mediimg = imgproxy.image;
                if(mediimg != null) {
                    val img = InputImage.fromMediaImage(mediimg, imgproxy.imageInfo.rotationDegrees);
                    barcodeScanner.process(img)
                        .addOnSuccessListener { lfbc ->
                            for (bco in lfbc) {
                                Log.w(TAG, "KF_CAMERA_INIT: ${bco.rawValue} ${bco.valueType} ${bco.displayValue}");
                                if(bco.valueType == Barcode.TYPE_URL) {
                                    var disval = bco.url;
                                    if(fromWhere != null && fromWhere.equals(MainFrag.fromMainFrag)) {
                                        webinte!!.putExtra(WebPagesActivity.webUrl, disval?.url);
                                        Log.d(TAG, "KF_CAMERA_INIT: Start web pages activity ");
                                        startActivity(webinte);
                                    }
                                    else if (fromWhere != null && fromWhere.equals(AddPointDialogFrag.fromAddpointFrag)) {
                                        var inte = Intent().also {
                                            it.putExtra(AddPointDialogFrag.urlkey, disval?.url);
                                        }
                                        setResult(AddPointDialogFrag.addFragSheetResuCode, inte);
                                    }

                                    imgproxy.close();
                                    finish();
                                    //break;
                                }
                            }
                            imgproxy.close();
                        }
                        .addOnFailureListener {
                            Log.w(TAG, "KF_CAMERA_INIT: Failed to scan bar code", it.cause);
                            imgproxy.close();
                        }
                } else {
                    Log.i(TAG, "KF_CAMERA_INIT: Image Media is nuLL");
                    imgproxy.close();
                }
            });
            /*var vipo = ViewPort.Builder(Rational(300, 300), window.decorView.rotation.toInt());
            var usegrop = UseCaseGroup.Builder()
                .addUseCase(preve).addUseCase(imgAnalysis!!).setViewPort(vipo.build()).build();*/
            try {
                camProvider.unbindAll();

                kamera = camProvider.bindToLifecycle(this, camSelec, preve, imgAnalysis);

            } catch (exc: Exception) {
                exc.printStackTrace();
            }
        }, ContextCompat.getMainExecutor(this));
    }

    /**
    * *             KF_CHECK_CAMERA_PERM
    */
    private fun KF_CHECK_CAMERA_PERM(): ActivityResultLauncher<String>
    {
        return registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if(it == false) {

            }
        }
    }

    companion object {
        val fromWhereKey = "From Where Intent Key";
    }
}