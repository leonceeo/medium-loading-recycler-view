package lc.st.free.lrv.contacts

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.delay
import lc.st.free.lrv.NoDataRecyclerViewAdapter
import lc.st.free.lrv.R
import lc.st.free.lrv.databinding.ContactBinding
import java.util.*

class Contact(val name: String, val email: String)

class ContactsAdapter(val scope: LifecycleCoroutineScope) : NoDataRecyclerViewAdapter<ContactsAdapter.ContactsVH>() {

    private val modes = Stack<Mode>().apply {
        add(Mode.NO_DATA)
        add(Mode.ERROR)
    }

    override fun buildConfig(): Config {
        return Config(
            loadingText = R.string.loading_contacts,
            errorText = R.string.error_while_loading_contacts,
            errorImage = R.drawable.angel,
            noDataText = R.string.no_contacts_available,
            noDataImage = R.drawable.open_box,
            retryText = R.string.try_again,
            retryAction = {
                scope.launchWhenStarted {
                    load()
                }
            })
    }

    class ContactsVH(val binding: ContactBinding) : RecyclerView.ViewHolder(binding.root)

    private val contacts = arrayOf(
        Contact("Quick Fox", "quick.fox@foxes.com"),
        Contact("Lame Duck", "lame.duck@ducks.com")
    )

    override fun onCreateDataViewHolder(parent: ViewGroup, viewType: Int): ContactsVH {
        return ContactsVH(
            ContactBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindDataViewHolder(holder: ContactsVH, position: Int) {
        holder.binding.contact = contacts[position]
    }

    suspend fun load() {
        mode = Mode.LOADING
        try {
            // Wait 5 seconds in order to simulate slow data fetching
            delay(5_000)
        } finally {
            if (!modes.isEmpty()) {
                mode = modes.pop()
            } else {
                mode = Mode.DATA
            }
        }
    }

    override fun getDataItemCount(): Int {
        return contacts.size
    }

    override fun getDataItemViewType(position: Int): Int {
        return Mode.DATA.itemViewType
    }


}