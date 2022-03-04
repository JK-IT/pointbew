package desoft.studio.webpocket

import androidx.recyclerview.selection.ItemKeyProvider

private const val tag = "KUST ITEM KEY TELLER";
class KusItemKeyTeller(val kusdapter: KusAdapter): ItemKeyProvider<String>(SCOPE_CACHED)
{

   val tag : String = "Kus_ItemTeller";
   override fun getKey(position: Int): String?
   {
      return kusdapter.GetAdapterData()[position].Name;
   }

   override fun getPosition(key: String): Int
   {
      return kusdapter.GetAdapterData().indexOfFirst { it.Name == key };
   }
}