package com.example.medsreminder.ui.dashboard

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.medsreminder.R
import com.example.medsreminder.databinding.DashboardRecyclerViewItemBinding
import com.example.medsreminder.model.MedicineStatusEnum
import com.example.medsreminder.model.Taking
import java.text.DecimalFormat
import java.time.LocalDateTime

class TakingsAdapter() : ListAdapter<Taking, TakingsAdapter.TakingViewHolder>(diffCallback) {

    class TakingViewHolder(
        private var binding: DashboardRecyclerViewItemBinding
    ) : ViewHolder(binding.root) {

        private var currentTaking: Taking? = null
        private val decimalFormat = DecimalFormat("#.#")


        fun bind(taking: Taking) {
            currentTaking = taking
            val resources = itemView.resources
            binding.dashboardRecyclerViewItemMedNameText.text = taking.medicineName
            binding.dashboardRecyclerViewItemTakeTimeText.text =
                resources.getString(
                    R.string.dashboard_RV_item_hour_sr,
                    LocalDateTime.parse(taking.date).hour,
                    LocalDateTime.parse(taking.date).minute
                )
            binding.dashboardRecyclerViewItemPillNumberText.text =
                resources.getString(
                    R.string.dashboard_RV_item_pill_number_sr,
                    decimalFormat.format(taking.pillNumber)
                )
            binding.dashboardRecyclerViewItemStatusText.text = when (taking.status) {
                MedicineStatusEnum.AFTER.name -> resources.getString(R.string.rv_item_status_after_meals_sr)
                MedicineStatusEnum.BEFORE.name -> resources.getString(R.string.rv_item_status_before_meals_sr)
                MedicineStatusEnum.COMPLETED.name -> resources.getString(R.string.rv_item_status_completed_sr)
                MedicineStatusEnum.DURING.name -> resources.getString(R.string.rv_item_status_during_meals_sr)
                MedicineStatusEnum.SKIPPED.name -> resources.getString(R.string.rv_item_status_skipped_sr)
                else -> ""
            }

        }


        companion object {
            fun create(parent: ViewGroup): TakingViewHolder {
                return TakingViewHolder(
                    DashboardRecyclerViewItemBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TakingViewHolder {
        return TakingViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: TakingViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current)
    }

    companion object {

        private val diffCallback = object : DiffUtil.ItemCallback<Taking>() {

            override fun areItemsTheSame(oldItem: Taking, newItem: Taking): Boolean {
                return oldItem.medicineName == newItem.medicineName && oldItem.date == newItem.date
            }

            override fun areContentsTheSame(oldItem: Taking, newItem: Taking): Boolean {
                return oldItem == newItem
            }
        }

    }

}