package desoft.studio.webpoint.fragments

import android.annotation.SuppressLint
import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import desoft.studio.webpoint.KusAdapter
import desoft.studio.webpoint.R
import desoft.studio.webpoint.data.Wpoint
import desoft.studio.webpoint.data.WpointVM
import kotlinx.coroutines.launch

class MainFrag : Fragment()
{
    private val TAG = "-wpoint- =;= MAIN FRAGMENT =;=";
    private val pointdb : WpointVM by activityViewModels<WpointVM>();
    private lateinit var navtroller : NavController;

    private lateinit var myBar : android.app.ActionBar;
    private var emptyPromt : TextView?= null;
    private lateinit var recyview : RecyclerView;
    private lateinit var recydapter : KusAdapter;
    /**
    * *                             onCreate
    */
    @SuppressLint("UnsafeRepeatOnLifecycleDetector")
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate");
        recydapter = KusAdapter(activity?.applicationContext!!, pointdb);
        activity?.let {
            pointdb.livePoint.observe(it, object: Observer<List<Wpoint>?>{
                override fun onChanged(t: List<Wpoint>?) {
                    t?.let {
                        KF_UPDATE_UI(t);
                    }
                }
            })
        }
    }
    /**
    * *                             onCreateView
    */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {
        return inflater.inflate(R.layout.frag_main, container, false);
    }

    /**
    * **                            onViewCreated
    */
    override fun onViewCreated(v: View, savedInstanceState: Bundle?)
    {
        navtroller=v.findNavController();
        (v.findViewById<FloatingActionButton>(R.id.frag_main_fab)).setOnClickListener{
            navtroller.navigate(R.id.action_mainFrag_to_addPointFrag);
        }
        (activity)?.setActionBar(v.findViewById(R.id.frag_main_toolbar))
        myBar = (activity)?.actionBar !!;

        emptyPromt = v.findViewById(R.id.frag_main_empty_data_view);
        emptyPromt?.visibility = View.VISIBLE;

        recyview = v.findViewById(R.id.frag_main_recyView);
        recyview.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false);
        recyview.adapter = recydapter;

    }

    override fun onStart() {
        super.onStart();
        Log.i(TAG, "onStart: ");
        if(recydapter.itemCount != 0) {
            emptyPromt?.visibility = View.GONE;
        } else {
            emptyPromt?.visibility = View.VISIBLE;
        }
    }
    // + --------->>-------->>--------->>*** -->>----------->>>>

    /**
    * *                         KF_UPDATE_UI
    */
    private fun KF_UPDATE_UI(inwp : List<Wpoint>?)
    {
        if(inwp != null && inwp?.size != 0) {
            Log.i(TAG, "KF_UPDATE_UI: WPOINT IS NON NULL ${inwp.size}");
            lifecycleScope.launch{
                emptyPromt?.visibility = View.GONE;
                recydapter.SetData(inwp!!);
            }
        } else {
            emptyPromt?.visibility = View.VISIBLE;
        }
    }

}