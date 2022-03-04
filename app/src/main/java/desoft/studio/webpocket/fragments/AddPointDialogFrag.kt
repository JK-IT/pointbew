package desoft.studio.webpocket.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.setFragmentResult
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import desoft.studio.webpocket.R
import desoft.studio.webpocket.SkanActivity
import desoft.studio.webpocket.data.Wpoint

class AddPointDialogFrag :  BottomSheetDialogFragment ()
{
    private val TAG = "-wpoint- BOTTOM SHEET DIALOG FRAGMENT";
    private val skanlauncher = KF_LAUNCH_SKAN_FOR_URL();

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
    }

    // *                onCreateView
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var v = inflater.inflate(R.layout.dialog_bottom_addpoin, container, false);
        return v;
    }
    private lateinit var nalout : TextInputLayout;
    private lateinit var name : TextInputEditText;
    private lateinit var urlout : TextInputLayout;
    private lateinit var url : TextInputEditText;
    private lateinit var addbtn : Button;
    private lateinit var cancelbtn : Button;

    // *                     onViewCreated
    override fun onViewCreated(v: View, savedInstanceState: Bundle?) {
        //. set behaviour
        var beha = (dialog as BottomSheetDialog).behavior;
        beha?.let {
            it.state = BottomSheetBehavior.STATE_EXPANDED;
            it.isFitToContents = true;
        }

        //.set view function
        nalout = v.findViewById(R.id.add_dia_name_lout);
        name = v.findViewById(R.id.add_dia_name);
        urlout = v.findViewById(R.id.add_dia_url_lout);
        url = v.findViewById(R.id.add_dia_url);
        addbtn = v.findViewById(R.id.add_dia_addbtn);
        cancelbtn = v.findViewById(R.id.add_dia_cancelbtn);
        cancelbtn.setOnClickListener {
            dismissAllowingStateLoss();
        }

        urlout.apply {
            setEndIconActivated(true);
            setEndIconOnClickListener {
                //Toast.makeText(requireContext(), "Scan me", Toast.LENGTH_SHORT).show();
                var webinte = Intent(requireContext(), SkanActivity::class.java);
                webinte.putExtra(SkanActivity.fromWhereKey, fromAddpointFrag);
                skanlauncher.launch(webinte);
            }
        }
        addbtn.setOnClickListener {
            if(name.text.isNullOrBlank()) {
                nalout.error = "Required Field";
                return@setOnClickListener;
            } else {
                nalout.error = null;
            }
            if(url.text.isNullOrBlank()) {
                urlout.error = "Required Field";
                return@setOnClickListener;
            } else {

                if(Patterns.WEB_URL.matcher(url.text.toString()).matches()) {
                    urlout.error = null;
                } else {
                    urlout.error = "Please enter a valid web address";
                    return@setOnClickListener;
                }
            }
            var poi = Wpoint(name.text.toString(), url.text.toString());
            var bdl = Bundle().also {
                it.putParcelable(addBundleKey, poi);
            }
            setFragmentResult(addPointKey, bdl);
            dismiss();
        }

    }

    //  *                   onSaveInstanceState
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState);
    }


    // + --------->>-------->>--------->>*** -->>----------->>>>

    /**
    * *                 KF_LAUNCH_SKAN_FOR_URL
    */
    private fun KF_LAUNCH_SKAN_FOR_URL() : ActivityResultLauncher<Intent>
    {
        return registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            //Log.w(TAG, "KF_LAUNCH_SKAN_FOR_URL: Getting result from launching skan activity");
            if(it.resultCode == AddPointDialogFrag.addFragSheetResuCode) {
                var data = it.data;
                if(data != null) {
                    //Log.i(TAG, "KF_LAUNCH_SKAN_FOR_URL: Receive this result from skan ${data.getStringExtra( urlkey)}");
                    url.setText(data.getStringExtra(urlkey));
                }
            }
        }
    }

    // + --------->>-------->>--------->>*** -->>----------->>>>
    companion object {
        val namekey = "WEB NAME KEY";
        val urlkey = "WEB URL KEY";
        val addPointKey = "WEB POINT REQUEST KEY";
        val addBundleKey = "WEB POINT BUNDLE KEY";
        val fragtag = "WEB PONINT FRAG TAG";
        val fromAddpointFrag = "From ADD POINT FRAG";
        val addFragSheetResuCode  = 1;

        fun Instance(inpa : String) : AddPointDialogFrag {
            return AddPointDialogFrag().also {
                it.arguments = Bundle().also {
                    it.putString(namekey, inpa);
                };
            }
        }
    }
}