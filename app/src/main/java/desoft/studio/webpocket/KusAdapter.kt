package desoft.studio.webpocket

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.*
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.selection.SelectionTracker
import com.google.android.material.bottomsheet.BottomSheetDialog
import desoft.studio.webpocket.data.Wpoint
import desoft.studio.webpocket.data.WpointVM
import java.util.*

private const val tagg = "KUS ADAPTER";

class KusAdapter(private val ctx : Context, var invm : WpointVM) : RecyclerView.Adapter<KusAdapter.ViewHolder>()
{
    private val TAG = "-wpoint- KUS ADAPTER";
    //--------------- Things belong to Adapter
    private var recy : RecyclerView? = null;
    var tracker: SelectionTracker<String>? = null;
    var dataSet: SortedSet<Wpoint> = sortedSetOf();
    // this list will be cleared by contextual mode on Finished
    var selectedSet: MutableSet<Wpoint> =   mutableSetOf<Wpoint>();
    var isActionMode: Boolean = false;
    
    init
    {
        setHasStableIds(true); // confirm that each item on the list has stable id
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        recy = recyclerView;
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
        val data = dataSet.toList()[position]; // -> Wpoint
        Log.i(TAG, "onBind: Data gonna bind ${data} - POSITION $position");
        holder.BindData(data, false);
        /*tracker?.let {
            holder.BindData(data,
                  it.selection.contains(data.Name)); // can use isSelected as shortcut
        }*/
    }
    
    override fun getItemCount(): Int
    {
        return dataSet.size;
    }
    
    override fun getItemId(position: Int): Long
    {
        return position.toLong();
    }

    public fun GetTracker(): SelectionTracker<String>
    {
        return tracker!!;
    }
    /**
    * var temp1 = common elements
     * var temp2 = elements from indata not existing in temp1
     * adding temp2 to dataSet, notify elements adding
    */
    public fun SetData(indata: SortedSet<Wpoint>)
    {
        dataSet = indata;
        recy?.recycledViewPool?.clear();
        notifyDataSetChanged();
        recy?.scheduleLayoutAnimation();
    }
    
    public fun GetAdapterData(): List<Wpoint>
    {
        return dataSet.toList();
    }
    
    fun SetActModeState(started: Boolean)
    {
        isActionMode = started;
    }

    // + --------->>-------->>--------->>*** -->>----------->>>>
    // *------------ View Holder
    inner class ViewHolder(val v: View, val ctx: Context?) : RecyclerView.ViewHolder(v)
    {
        val plugview: TextView;
        val urlview : TextView;
        val openbut: ImageButton;
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

            deletebut = v.findViewById<ImageButton>(R.id.delete_button).apply {
                isEnabled = true;
                setOnClickListener{
                    var btdia = BottomSheetDialog(ctx!!);
                    btdia.apply {
                        setContentView(R.layout.dialog_delete);
                    }
                    var ybtn = btdia?.findViewById<Button>(R.id.dia_delete_yes);
                    ybtn?.setOnClickListener {
                        val point = dataSet.toList()[absoluteAdapterPosition];
                        invm.DeletePoint(point);
                        btdia.dismiss();
                    }
                    var nbtn = btdia?.findViewById<Button>(R.id.dia_delete_cancel);
                    nbtn?.setOnClickListener {
                        btdia.dismiss();
                    }
                    btdia.show();
                }
            }
        }
        
        fun BindData(dat: Wpoint, isActivated: Boolean = false)
        {
            var itemName  = dat.Name.lowercase();
            var words = itemName.split(" ");
            var ditedname = words.joinToString (separator = " "){word -> word.replaceFirstChar { it.uppercaseChar() }}
            Log.d(TAG, "BindData: item to bind ${dat}");
            plugview.text = dat.Name //"${ditedname}";
            urlview.text = "${dat.Url}";
            itemView.isActivated = isActivated;
        }
        
        fun GetItemDetails(): KusItemDetails<String>
        {
            return KusItemDetails(absoluteAdapterPosition, dataSet.toList()[absoluteAdapterPosition].Name);
        }
    }
    // + --------->>-------->>--------->>*** -->>----------->>>>

}