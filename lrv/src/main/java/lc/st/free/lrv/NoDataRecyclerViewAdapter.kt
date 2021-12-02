package lc.st.free.lrv

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.recyclerview.widget.RecyclerView
import lc.st.free.lrv.databinding.NoDataBinding

/**
 * Base class for a {@link RecyclerView} adapter being able to:
 * - display a progress while underlying data is being fetched
 * - display an error message with the possibility to re-fetch
 * - display a message in case when no data is available
 * - display the data
 */
abstract class NoDataRecyclerViewAdapter<T : RecyclerView.ViewHolder>() :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    /**
     * Specifies the view modes this adapter can handle.
     */
    enum class Mode(val itemViewType: Int) {
        /** Loading mode. Recycler view will show a progress **/
        LOADING(1),
        /** No data mode. RecyclerView will show the no-data message **/
        NO_DATA(1),
        /** Error mode. RecyclerView will show the error message **/
        ERROR(1),
        /** Data mode. RecyclerView will show the actual data **/
        DATA(2)
    }

    /** Current mode of this adapter. */
    var mode: Mode = Mode.LOADING
        /**
         * Changes the mode of this adapter. Switching modes will trigger a invocation of #notifyDataSetChanged.
         */
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            if (this.mode == value) {
                return
            }
            field = value
            notifyDataSetChanged()
        }

    private val config: Config by lazy {
        buildConfig()
    }

    /**
     * Factory method for this adapters {@link Config}.
     * @return the configuration this adapter will use.
     */
    abstract fun buildConfig(): Config

    final override fun getItemCount(): Int {
        return when (mode) {
            Mode.LOADING, Mode.NO_DATA, Mode.ERROR -> 1
            Mode.DATA -> getDataItemCount()
        }
    }

    final override fun getItemViewType(position: Int): Int {
        return if (mode == Mode.DATA) getDataItemViewType(position) else mode.itemViewType
    }

    final override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        return when (viewType) {
            Mode.NO_DATA.itemViewType, Mode.LOADING.itemViewType, Mode.ERROR.itemViewType -> NoDataVH(
                NoDataBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
            // Call templage method
            else -> onCreateDataViewHolder(parent, viewType)
        }
    }

    final override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val noDataVH = holder as? NoDataVH
        if (noDataVH != null) {
            noDataVH.binding.config = config
            noDataVH.binding.mode = mode
        } else {
            // Call template method
            onBindDataViewHolder(holder as T, position)
        }
    }

    /**
     * Template method which creates the ViewHolder for the data mode.
     */
    abstract fun onCreateDataViewHolder(parent: ViewGroup, viewType: Int): T

    /**
     * Template method which binds the data mode ViewHolder. Has the same semantic like #onBindViewHolder.
     */
    abstract fun onBindDataViewHolder(holder: T, position: Int)

    /**
     * Template method for retrieving the data mode item count. Has the same semantic like #getItemCount
     */
    abstract fun getDataItemCount(): Int

    /**
     * Template method for retrieving the data mode item count. Has the same semantic like #getItemViewType
     */
    abstract fun getDataItemViewType(position: Int): Int

    class NoDataVH(val binding: NoDataBinding) : RecyclerView.ViewHolder(binding.root)

    /**
     * Configuration used by this adapter.
     * @param loadingText text resource which will be displayed in the loading mode.
     * @param errorText text resource which will be displayed in the error mode.
     * @param errorImage image resource which will be displayed in the error mode.
     * @param noDataText text resource which will be displayed in the no-data mode.
     * @param noDataImage image resource which will be displayed in the no-data mode.
     * @param retryText text resource displayed by the retry button (available in the error and no-data mode)
     * @param retryAction executed when the retry button is clicked.
     */
    class Config(
        @StringRes val loadingText: Int,
        @StringRes val errorText: Int,
        @DrawableRes val errorImage: Int,
        @StringRes val noDataText: Int,
        @DrawableRes val noDataImage: Int,
        @StringRes val retryText: Int,
        val retryAction: () -> Unit
    )
}