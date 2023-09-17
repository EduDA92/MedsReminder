package com.example.medsreminder.extensions

import android.view.View
import androidx.annotation.StringRes
import androidx.fragment.app.FragmentManager
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.datepicker.MaterialDatePicker.InputMode
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat

fun FragmentManager.showDatepicker(
    @StringRes titleText: Int,
    selection: Long,
    tag: String,
    positiveButtonClickListener: MaterialPickerOnPositiveButtonClickListener<Long>
) {
    val builder = MaterialDatePicker.Builder.datePicker()
        .setTitleText(titleText)
        .setSelection(selection)
        .build()

    builder.addOnPositiveButtonClickListener(positiveButtonClickListener)

    builder.show(this, tag)
}

fun FragmentManager.showTimepicker(
    @StringRes titleText: Int,
    @TimeFormat timeFormat: Int,
    @InputMode inputMode: Int,
    tag: String,
    positiveButtonClickListener: View.OnClickListener
) {
    val builder = MaterialTimePicker.Builder()
        .setTitleText(titleText)
        .setTimeFormat(timeFormat)
        .setInputMode(inputMode)
        .build()

    builder.addOnPositiveButtonClickListener(positiveButtonClickListener)
    builder.show(this, tag)
}