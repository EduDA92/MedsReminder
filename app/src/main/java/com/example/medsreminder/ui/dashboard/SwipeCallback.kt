package com.example.medsreminder.ui.dashboard

import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Rect
import android.util.TypedValue
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.medsreminder.R
import com.example.medsreminder.model.MedicineStatusEnum
import com.example.medsreminder.model.Taking
import kotlin.math.roundToInt

class SwipeCallback(
    private val adapter: TakingsAdapter,
    private val resources: Resources,
    private val updateTaking: (Taking, MedicineStatusEnum) -> Unit
) :
    ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position = viewHolder.bindingAdapterPosition
        val taking = adapter.currentList[position]

        when (direction) {
            ItemTouchHelper.RIGHT -> updateTaking(taking, MedicineStatusEnum.COMPLETED)
            ItemTouchHelper.LEFT -> updateTaking(taking, MedicineStatusEnum.SKIPPED)
        }

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

        val dismissIcon = ResourcesCompat.getDrawable(resources, R.drawable.outline_cancel_24, null)
        val doneIcon = ResourcesCompat.getDrawable(resources, R.drawable.round_done_outline_24, null)


        if (doneIcon != null) {
            doneIcon.bounds = Rect(
                16.dp,
                viewHolder.itemView.top + 24.dp,
                16.dp + doneIcon.intrinsicWidth,
                viewHolder.itemView.top + doneIcon.intrinsicHeight + 24.dp
            )
        }

        if (dismissIcon != null) {
            dismissIcon.bounds = Rect(
                viewHolder.itemView.right - 16.dp - dismissIcon.intrinsicWidth,
                viewHolder.itemView.top + 24.dp,
                viewHolder.itemView.right - 16.dp,
                viewHolder.itemView.top + dismissIcon.intrinsicHeight + 24.dp
            )
        }


        when{
            dX > 0 -> doneIcon?.draw(c)
            dX < 0 -> dismissIcon?.draw(c)
        }

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)

    }

    private val Int.dp
        get() = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            toFloat(), resources.displayMetrics
        ).roundToInt()
}