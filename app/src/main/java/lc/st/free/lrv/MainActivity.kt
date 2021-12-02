package lc.st.free.lrv

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import lc.st.free.lrv.contacts.ContactsAdapter
import lc.st.free.lrv.app.R


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<RecyclerView>(R.id.recycler).apply {
            adapter = ContactsAdapter(lifecycleScope).also {
                lifecycleScope.launch {
                    it.load()
                }
            }
            layoutManager = LinearLayoutManager(context)
        }
    }
}