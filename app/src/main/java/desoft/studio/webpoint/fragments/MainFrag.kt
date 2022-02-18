package desoft.studio.webpoint.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
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
import desoft.studio.webpoint.SkanActivity
import desoft.studio.webpoint.data.Wpoint
import desoft.studio.webpoint.data.WpointVM
import kotlinx.coroutines.launch

class MainFrag : Fragment()
{
    private val TAG = "-wpoint- =;= MAIN FRAGMENT =;=";
    private val pointdb : WpointVM by activityViewModels<WpointVM>();
    private lateinit var navtroller : NavController;
    private var scannerlauncher  = KF_SKAN_LAUNCHER_RESU();

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
        setHasOptionsMenu(true);
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

/*        (activity)?.setActionBar(v.findViewById(R.id.frag_main_toolbar))
        myBar = (activity)?.actionBar !!;*/


        emptyPromt = v.findViewById(R.id.frag_main_empty_data_view);
        emptyPromt?.visibility = View.VISIBLE;

        recyview = v.findViewById(R.id.frag_main_recyView);
        recyview?.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false);
        recyview?.adapter = recydapter;

        (v.findViewById<FloatingActionButton>(R.id.frag_main_fab)).setOnClickListener{
            //navtroller.navigate(R.id.action_mainFrag_to_addPointFrag);
            AddPointDialogFrag().show(childFragmentManager, AddPointDialogFrag.fragtag);
            //startActivity(Intent( requireActivity(),  SkanActivity::class.java));
        }

    }

    /**
    * *             onStart
    */
    override fun onStart() {
        super.onStart();
        Log.i(TAG, "onStart: ");
        if(recydapter.itemCount != 0) {
            emptyPromt?.visibility = View.GONE;
        } else {
            emptyPromt?.visibility = View.VISIBLE;
        }
    }
    /**
    * *             onSaveInstanceState
    */
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState);
    }

    /**
    * *                 onCreateOptionsMenu
    */
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.frag_dash_board_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    /**
    * *                 onOptionsItemSelected
    */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.menu_qr_scan ->{
                scannerlauncher.launch(Intent(requireActivity(), SkanActivity::class.java).also {
                    it.putExtra(SkanActivity.fromWhereKey, fromMainFrag);
                }  );
                true;
            }
            else -> super.onOptionsItemSelected(item);
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

    /**
    * *             KF_SKAN_LAUNCHER_RESU
    */
    private fun KF_SKAN_LAUNCHER_RESU(): ActivityResultLauncher<Intent> {
        return registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if(it.resultCode == mainFragResuCode) {
                //Toast.makeText(requireContext(), "Hem no turning", Toast.LENGTH_SHORT).show();
            }
        }
    }
    companion object {
        val mainFragResuCode = 2;
        val fromMainFrag = "From Main Fragment";
    }
}