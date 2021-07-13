package desoft.studio.webpoint

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.text.SpannableStringBuilder
import android.util.Log
import android.util.Patterns
import android.view.*
import android.widget.*
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.selection.SelectionTracker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import desoft.studio.webpoint.data.Wpoint
import desoft.studio.webpoint.data.WpointVM
import desoft.studio.webpoint.listeners.KusTextChangedHandler

private const val tagg = "KUS ADAPTER";

class KusAdapter(private val ctx : Context) : RecyclerView.Adapter<KusAdapter.ViewHolder>()
{
    
    //--------------- Things belong to Adapter
    var tracker: SelectionTracker<String>? = null;
    var dataSet: List<Wpoint> = ArrayList<Wpoint>();
    // this list will be cleared by contextual mode on Finished
    var selectedSet: MutableSet<Wpoint> =   mutableSetOf<Wpoint>();
    var isActionMode: Boolean = false;
    
    init
    {
        setHasStableIds(true); // confirm that each item on the list has stable id
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
    {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.recyview_item_layout, parent, false);
        return this.ViewHolder(v, parent.context);
    }
    
    override fun onBindViewHolder(holder: ViewHolder, position: Int)
    {
        val data = dataSet[position]; // -> Wpoint
        tracker?.let {
            holder.BindData(data,
                  it.selection.contains(data.Name)); // can use isSelected as shortcut
        }
    }
    
    override fun getItemCount(): Int
    {
        return dataSet.size;
    }
    
    override fun getItemId(position: Int): Long
    {
        return position.toLong();
    }
    
    public fun SetTracker(track: SelectionTracker<String>)
    {
        tracker = track; // set up observer
        tracker!!.addObserver(KusSelectionObserver(track));
    }
    
    public fun GetTracker(): SelectionTracker<String>
    {
        return tracker!!;
    }
    
    public fun SetData(indata: List<Wpoint>)
    {
        this.dataSet = indata;
        notifyDataSetChanged();
    }
    
    public fun GetAdapterData(): List<Wpoint>
    {
        return dataSet;
    }
    
    fun SetActModeState(started: Boolean)
    {
        isActionMode = started;
    }
    
    // ------------ View Holder
    inner class ViewHolder(val v: View, val ctx: Context?) : RecyclerView.ViewHolder(v)
    {
        val plugview: TextView;
        val urlview : TextView;
        val openbut: ImageButton;
        val editbut: ImageButton;
        val deletebut: ImageButton;
        init
        {
            
            plugview = v.findViewById<TextView>(R.id.tv_itemNameView).apply {
                setOnClickListener{
                    Log.i(tagg, "Open the link");
                }
            }
            urlview = v.findViewById<TextView>(R.id.tv_urlview);
            openbut = v.findViewById<ImageButton>(R.id.open_button).apply {
                setOnClickListener {
                    var intent = Intent(ctx, WebPagesActivity::class.java);
                    intent.putExtra(WebPagesActivity.webName, plugview.text.toString());
                    intent.putExtra(WebPagesActivity.webUrl, GetAdapterData()[absoluteAdapterPosition].Url);
                    ctx?.startActivity(intent);
                }
                
            }
            editbut = v.findViewById<ImageButton>(R.id.edit_button).apply {
                isEnabled = true;
                setOnClickListener{
                    Log.i(tagg, "Edit button is clicked");
                    // pop up the add item dialog
                    val v = LayoutInflater.from(ctx).inflate(R.layout.frag_update_layout, null);
                    val point = dataSet[absoluteAdapterPosition];
                    val nameinput : EditText = v.findViewById(R.id.frag_update_name);
                    nameinput.text = SpannableStringBuilder(point.Name);
                    val urlinput : EditText = v.findViewById(R.id.frag_update_url);
                    urlinput.text = SpannableStringBuilder(point.Url);
                    MaterialAlertDialogBuilder(ctx!!)
                        .setView(v)
                        .setCancelable(true)
                        .setPositiveButton("Update"){
                            dialog, _ ->
                            if(nameinput.text.toString().isNotBlank() && urlinput.text.toString().isNotBlank() && Patterns.WEB_URL.matcher(urlinput.text.toString()).matches())
                            {
                                var updatepoint = Wpoint(nameinput.text.toString().uppercase(),urlinput.text.toString() );
                                if(dataSet.indexOfFirst { it.Name.contains(nameinput.text)} != -1) // find the duplicate key
                                {
                                    (ctx as MainActivity).AddPointNDelete(false, 0, updatepoint);
                                }
                                else { // the new point not existed
                                    (ctx as MainActivity).AddPointNDelete(true, absoluteAdapterPosition, updatepoint);
                                }
                                Toast.makeText(ctx, "Successfully update the item", Toast.LENGTH_SHORT).show();
                            } else
                            {
                                Snackbar.make(this, "Please Enter Valid Name and Url!", Snackbar.LENGTH_SHORT).setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE).show();
                            }
                            dialog.dismiss();
                        }
                        .setNegativeButton("Cancel"){
                            dialog,_ -> dialog.dismiss();
                        }.create().show();
                }
            }
            deletebut = v.findViewById<ImageButton>(R.id.delete_button).apply {
                isEnabled = true;
                setOnClickListener{
                    Log.i(tagg, "Delete button is clicked");
                    MaterialAlertDialogBuilder(ctx!!).setTitle("Delete")
                        .setMessage("Are you sure deleting this item?")
                        .setPositiveButton("Yes"){
                            dis, _ ->
                            val point = dataSet[absoluteAdapterPosition];
                            (ctx as MainActivity).DeleteWpoin(point);
                            dis.dismiss();
                        }.setNegativeButton("No"){
                            dis, _ ->
                            dis.dismiss()
                        }.setCancelable(true).create().show();
                }
            }
            
            
            /**
             * u don't need this cuz, u will implement click on SelectionTracker.Builder
             * v.setOnClickListener(View.OnClickListener(){
            Log.d(tag, "Getting click on adapter position $absoluteAdapterPosition and item id ${getItemId(absoluteAdapterPosition)}");
             */
            
        }
        
        fun BindData(dat: Wpoint, isActivated: Boolean = false)
        { //Log.i(tag, "Bind view holder is called inside Bind data and item is selected $isActivated");
            var itemName  = dat.Name.lowercase();
            var words = itemName.split(" ");
            var ditedname = words.joinToString (separator = " "){word -> word.replaceFirstChar { it.uppercaseChar() }}
            plugview.text = "${ditedname}"; // itemView == v aka View
            urlview.text = "${dat.Url}";
            itemView.isActivated = isActivated;
        }
        
        fun GetItemDetails(): KusItemDetails<String>
        {
            return KusItemDetails(absoluteAdapterPosition, dataSet[absoluteAdapterPosition].Name);
        }
    }
    
    // Selection Observer
    // this class hold methods which will be executed from tracker.
    // to get info about the selection or items, you need to use the tracker that u call "addObserver"
    // aka reference to outter tracker
    inner class KusSelectionObserver(track: SelectionTracker<String>) :
          SelectionTracker.SelectionObserver<String>()
    {
        override fun onItemStateChanged(key: String, selected: Boolean)
        {
            super.onItemStateChanged(key, selected)
            
            if (tracker?.selection?.size() == 1 && !isActionMode)
            {
                Log.i(tagg, "Observer : onItemStateChanged- $key is selected $selected - multi-selection started - one item selected so starting contextual mode");
                (ctx as MainActivity).StartTextualMode();
            }
            else if (tracker?.selection?.size() == 0 && isActionMode)
            {
                //Log.i(tagg, "Observer : no item is selected so stopping contextual mode");
                (ctx as MainActivity).StopTextualMode();
            }
    }

        override fun onSelectionChanged()
        {
            Log.i(tagg, "Observer : onSelectionChanged ${tracker?.selection.toString()}");
            // getting key from this selection
            if(tracker?.hasSelection() == true)
            {
                var tracsiz = tracker?.selection?.size();
                Log.i(tagg, "Observer : onSelectionChanged - tracker size ${tracsiz}");
                var traciter = tracker?.selection?.iterator();
                var temp  = mutableSetOf<Wpoint>();
                if (tracsiz != null)
                {
                    while(tracsiz >0 )
                    {
                        
                        var naky = traciter?.next();
                        var pos = GetAdapterData().indexOfFirst { it.Name == naky };
                        temp.add(GetAdapterData()[pos]);
                        //Log.i(tagg, "Adding to selectedSet ${selectedSet.size}");
                        tracsiz--;
                    }
                    selectedSet = temp;
                }
            }
            super.onSelectionChanged()
        }

        override fun onSelectionRefresh()
        {
            Log.i(tagg, "Selection Observer; on Selection REFRESH");
            super.onSelectionRefresh()
        }
    
        override fun onSelectionRestored()
        {
            Log.d(tagg, "Selection Observer; on Selection RESTORE");
            super.onSelectionRestored()
        }
        
    }

    

    /**
     * Implement Item Touch Listener for view
     */
    // this will delegate the event to child using the boundary of touch
    // we can do this for textview too
    fun KusItemClickHandler(recy: RecyclerView, @NonNull e: MotionEvent)
    {
        val cv: View? = recy.findChildViewUnder(e.x, e.y);
        Log.d(tagg, "Childview clicked coor ${e.x} - ${e.y}- index -> ${recy.indexOfChild(cv!!)}");
        val delegateArea = Rect();
        val editbutt : ImageButton = cv.findViewById<ImageButton>(R.id.edit_button).apply{
            isEnabled = true;
            setOnClickListener{
                Log.i(tagg, "Edit button is clicked");
            }
            getHitRect(delegateArea);
        }
        val delebutt : ImageButton = cv.findViewById<ImageButton>(R.id.delete_button).apply{
            isEnabled = true;
            setOnClickListener{
                Log.i(tagg, "Delete button is clicked");
            }
            getHitRect(delegateArea);
        }
        delegateArea.top += 10;
        delegateArea.bottom += 10;
        //(editbutt.parent as? View)?.apply {
        cv.apply {
            touchDelegate = TouchDelegate(delegateArea, delebutt);
            touchDelegate = TouchDelegate(delegateArea, editbutt);
        }
    }
}