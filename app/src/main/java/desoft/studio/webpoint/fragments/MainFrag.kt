package desoft.studio.webpoint.fragments

import android.annotation.SuppressLint
import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
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
    private var recyview : RecyclerView?=null;
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
        childFragmentManager.setFragmentResultListener(AddPointDialogFrag.addPointKey, this) { key, bdl ->
            var wpoi = bdl.get(AddPointDialogFrag.addBundleKey) as Wpoint;
            Log.d(TAG, "Get Result by adding point $wpoi");
            pointdb.AddPoint(wpoi);
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

        (activity)?.setActionBar(v.findViewById(R.id.frag_main_toolbar))
        myBar = (activity)?.actionBar !!;

        emptyPromt = v.findViewById(R.id.frag_main_empty_data_view);
        emptyPromt?.visibility = View.VISIBLE;

        recyview = v.findViewById(R.id.frag_main_recyView);
        recyview?.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false);
        recyview?.adapter = recydapter;

        (v.findViewById<FloatingActionButton>(R.id.frag_main_fab)).setOnClickListener{
            //navtroller.navigate(R.id.action_mainFrag_to_addPointFrag);
            AddPointDialogFrag().show(childFragmentManager, AddPointDialogFrag.fragtag);
        }
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
        lifecycleScope.launch{
            if(inwp?.size == 0 ) {
                recydapter.dataSet.clear();
                recyview?.recycledViewPool?.clear();
                recydapter.notifyDataSetChanged();
                emptyPromt?.visibility = View.VISIBLE;
            }
            else {
                var temp = recydapter.dataSet;
                var intemp = inwp?.toSortedSet();
                if (intemp != null) {
                    if(temp.size <= intemp.size) {
                        temp.retainAll(intemp); //-> get common
                        intemp.removeAll(temp);
                        for(ele in intemp) {
                            temp.add(ele);
                        }
                        recydapter.SetData(temp);
                    }
                    else if (temp.size > intemp.size){
                        recydapter.SetData(intemp);
                    }
                }
                emptyPromt?.visibility = View.GONE;
            }
        }
    }
}