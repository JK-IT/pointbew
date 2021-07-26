package desoft.studio.webpoint

import android.content.Context
import android.content.Intent
import android.text.SpannableStringBuilder
import android.util.Patterns
import android.view.*
import android.widget.*
<<<<<<< pdro
=======
import androidx.annotation.NonNull
>>>>>>> 97250
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.selection.SelectionTracker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import desoft.studio.webpoint.data.Wpoint
<<<<<<< pdro
=======
import desoft.studio.webpoint.fragments.KusDiaFrag
>>>>>>> 97250

private const val tagg = "KUS ADAPTER";

class KusAdapter(private val ctx : Context, private var recy: RecyclerView) : RecyclerView.Adapter<KusAdapter.ViewHolder>()
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
        var v: View;
        v = LayoutInflater.from(parent.context)
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
        recy.scheduleLayoutAnimation();
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
            plugview = v.findViewById<TextView>(R.id.tv_itemNameView);
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
                    // pop up the add item dialog
                    val v = LayoutInflater.from(ctx).inflate(R.layout.frag_update_layout, null);
                    val point = dataSet[absoluteAdapterPosition];
                    var disfrag = KusDiaFrag(R.layout.frag_update_layout);
                    disfrag.SetViewHandler(object : KusDiaFrag.SetupView{
                        override fun SetViewUI(vi: View)
                        {
                            val nameinput : EditText = vi.findViewById(R.id.frag_update_name);
                            nameinput.text = SpannableStringBuilder(point.Name);
                            val urlinput : EditText = vi.findViewById(R.id.frag_update_url);
                            urlinput.text = SpannableStringBuilder(point.Url);
                            val update : Button = vi.findViewById(R.id.frag_update_button);
                            val cancel : Button = vi.findViewById(R.id.frag_cancel_button);
                            update.setOnClickListener(){
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
                                    Toast.makeText((ctx as MainActivity), "Successfully update the item", Toast.LENGTH_SHORT).show();
                                } else {
                                    Snackbar.make((ctx as MainActivity).window.decorView.findViewById(android.R.id.content), "Please Enter Valid Name and Url!", Snackbar.LENGTH_SHORT).setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE).show();
                                }
                                disfrag.dismiss();
                            }
                            cancel.setOnClickListener(){
                                disfrag.dismiss();
                            }
                        }
                    })
                    var ft = (ctx as MainActivity).supportFragmentManager.beginTransaction();
                    var prev = (ctx as MainActivity).supportFragmentManager.findFragmentByTag(
                        KusDiaFrag.tagg);
                    if(prev != null)
                    {
                        ft.remove(prev);
                    }
                    disfrag.show((ctx as MainActivity).supportFragmentManager, KusDiaFrag.tagg)
                }
            }
            deletebut = v.findViewById<ImageButton>(R.id.delete_button).apply {
                isEnabled = true;
                setOnClickListener{
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
        }
        
        fun BindData(dat: Wpoint, isActivated: Boolean = false)
        {
            var itemName  = dat.Name.lowercase();
            var words = itemName.split(" ");
            var ditedname = words.joinToString (separator = " "){word -> word.replaceFirstChar { it.uppercaseChar() }}
            plugview.text = "${ditedname}";
            urlview.text = "${dat.Url}";
            itemView.isActivated = isActivated;
        }
        
        fun GetItemDetails(): KusItemDetails<String>
        {
            return KusItemDetails(absoluteAdapterPosition, dataSet[absoluteAdapterPosition].Name);
        }
    }
    
    inner class KusSelectionObserver(track: SelectionTracker<String>) :
          SelectionTracker.SelectionObserver<String>()
    {
        override fun onItemStateChanged(key: String, selected: Boolean)
        {
            super.onItemStateChanged(key, selected)
            
            if (tracker?.selection?.size() == 1 && !isActionMode)
            {
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
            // getting key from this selection
            if(tracker?.hasSelection() == true)
            {
                var tracsiz = tracker?.selection?.size();
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
            super.onSelectionRefresh()
        }
    
        override fun onSelectionRestored()
        {
            super.onSelectionRestored()
        }
    }
}