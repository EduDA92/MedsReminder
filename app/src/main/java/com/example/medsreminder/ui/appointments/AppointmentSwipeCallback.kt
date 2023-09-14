package com.example.medsreminder.ui.appointments

import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Rect
import android.util.TypedValue
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.medsreminder.R
import com.example.medsreminder.model.Appointment
import kotlin.math.roundToInt

class AppointmentSwipeCallback(
    private val adapter: AppointmentsAdapter,
    private val resources: Resources,
    private val deleteAppointment: (Appointment) -> Unit
): ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT)  {

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position = viewHolder.bindingAdapterPosition
        val appointment = adapter.currentList[position]

        deleteAppointment(appointment)
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {

        val deleteIcon = ResourcesCompat.getDrawable(resources, R.drawable.round_delete_outline_24, null)

        if (deleteIcon != null) {
            deleteIcon.bounds = Rect(
                viewHolder.itemView.right - 16.dp - deleteIcon.intrinsicWidth,
                viewHolder.itemView.top + 24.dp,
                viewHolder.itemView.right - 16.dp,
                viewHolder.itemView.top + deleteIcon.intrinsicHeight + 24.dp
            )
        }

        when{
            dX < 0 -> deleteIcon?.draw(c)
        }

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }

    private val Int.dp
        get() = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            toFloat(), resources.displayMetrics
        ).roundToInt()
}