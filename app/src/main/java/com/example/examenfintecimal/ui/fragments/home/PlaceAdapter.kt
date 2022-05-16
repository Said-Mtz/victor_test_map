package com.example.examenfintecimal.ui.fragments.home


import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.examenfintecimal.R
import com.example.examenfintecimal.core.model.ModelApiItem
import com.example.examenfintecimal.databinding.ItemPlaceBinding

val TAG = PlaceAdapter::class.java.simpleName

class PlaceAdapter : ListAdapter<ModelApiItem, PlaceAdapter.PlaceViewHolder>(DiffCallback) {

    lateinit var onItemClickListener: (ModelApiItem) -> Unit

    companion object DiffCallback : DiffUtil.ItemCallback<ModelApiItem>() {

        override fun areItemsTheSame(oldItem: ModelApiItem, newItem: ModelApiItem): Boolean {
            return oldItem.streetName == newItem.streetName
        }

        override fun areContentsTheSame(oldItem: ModelApiItem, newItem: ModelApiItem): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceViewHolder {
        val mBinding = ItemPlaceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlaceViewHolder(mBinding)
    }

    override fun onBindViewHolder(holder: PlaceViewHolder, position: Int) {
        val place = getItem(position)
        return holder.render(place)
    }

    inner class PlaceViewHolder(private val mBinding: ItemPlaceBinding) :
        RecyclerView.ViewHolder(mBinding.root) {

        fun render(itemPlace: ModelApiItem) = with(mBinding) {
            with(itemPlace) {

                if (visited) {
                    txtStatusPlace.text = root.context.getString(R.string.txt_item_visit)
                    svgStatusCircle.setColorFilter(
                        ContextCompat.getColor(
                            root.context,
                            R.color.green_aqua
                        )
                    )
                } else {
                    txtStatusPlace.text = root.context.getString(R.string.txt_item_pending)
                    svgStatusCircle.setColorFilter(
                        ContextCompat.getColor(
                            root.context,
                            R.color.grey
                        )
                    )
                }

                txtStreetName.text = streetName
                txtSubUrb.text = suburb

                root.setOnClickListener {
                    if (::onItemClickListener.isInitialized) {
                        onItemClickListener(this)
                    } else {
                        Log.e(TAG, "onItemClickListener no initialized")
                    }
                }
            }
        }
    }
}