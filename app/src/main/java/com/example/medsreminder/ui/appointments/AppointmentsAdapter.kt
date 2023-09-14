package com.example.medsreminder.ui.appointments

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.medsreminder.R
import com.example.medsreminder.databinding.AppointmentsRecyclerViewItemBinding
import com.example.medsreminder.model.Appointment
import java.time.LocalDateTime

class AppointmentsAdapter : ListAdapter<Appointment, AppointmentsAdapter.AppointmentViewHolder>(
    diffCallback
) {

    class AppointmentViewHolder(private var binding: AppointmentsRecyclerViewItemBinding) :
        ViewHolder(binding.root) {

        private var currentAppointment: Appointment? = null

        fun bind(appointment: Appointment) {
            val resources = itemView.resources
            val date = LocalDateTime.parse(appointment.date)
            currentAppointment = appointment
            binding.appointmentsRecyclerViewItemTypeText.text = appointment.type
            binding.appointmentsRecyclerViewItemLocationText.text = appointment.location
            binding.appointmentsRecyclerViewItemDateText.text = resources.getString(
                R.string.appointment_RV_item_date_sr,
                date.dayOfMonth,
                date.month
            )
            binding.appointmentsRecyclerViewItemAppointmentTimeText.text =
                resources.getString(R.string.appointment_RV_item_hour_sr, date.hour, date.minute)

        }

        companion object {
            fun create(parent: ViewGroup): AppointmentViewHolder {
                return AppointmentViewHolder(
                    AppointmentsRecyclerViewItemBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppointmentViewHolder {
        return AppointmentViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: AppointmentViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current)
    }

    companion object {

        private val diffCallback = object : DiffUtil.ItemCallback<Appointment>() {

            override fun areItemsTheSame(oldItem: Appointment, newItem: Appointment): Boolean {
                return oldItem.type == newItem.type && oldItem.date == newItem.date
            }

            override fun areContentsTheSame(oldItem: Appointment, newItem: Appointment): Boolean {
                return oldItem == newItem
            }
        }

    }
}