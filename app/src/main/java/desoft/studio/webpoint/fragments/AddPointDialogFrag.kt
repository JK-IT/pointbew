package desoft.studio.webpoint.fragments

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import desoft.studio.webpoint.R
import desoft.studio.webpoint.data.Wpoint

class AddPointDialogFrag :  BottomSheetDialogFragment ()
{
    private val TAG = "-wpoint- BOTTOM SHEET DIALOG FRAGMENT";

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var v = inflater.inflate(R.layout.add_bottom_dialog, container, false);
        return v;
    }
    private lateinit var nalout : TextInputLayout;
    private lateinit var name : TextInputEditText;
    private lateinit var urlout : TextInputLayout;
    private lateinit var url : TextInputEditText;
    private lateinit var addbtn : Button;
    private lateinit var cancelbtn : Button;

    override fun onViewCreated(v: View, savedInstanceState: Bundle?) {
        nalout = v.findViewById(R.id.add_dia_name_lout);
        name = v.findViewById(R.id.add_dia_name);
        urlout = v.findViewById(R.id.add_dia_url_lout);
        url = v.findViewById(R.id.add_dia_url);
        addbtn = v.findViewById(R.id.add_dia_addbtn);
        cancelbtn = v.findViewById(R.id.add_dia_cancelbtn);
        cancelbtn.setOnClickListener {
            dismissAllowingStateLoss();
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
    companion object {
        val namekey = "WEB NAME KEY";
        val urlkey = "WEB URL KEY";
        val addPointKey = "WEB POINT REQUEST KEY";
        val addBundleKey = "WEB POINT BUNDLE KEY";
        val fragtag = "WEB PONINT FRAG TAG";
    }
}