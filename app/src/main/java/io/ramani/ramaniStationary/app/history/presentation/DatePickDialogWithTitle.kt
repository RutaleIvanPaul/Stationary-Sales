package io.ramani.ramaniStationary.app.history.presentation

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.widget.CalendarView
import io.ramani.ramaniStationary.R
import kotlinx.android.synthetic.main.layout_date_pick_popup_with_title.*
import java.util.*

class DatePickDialogWithTitle(context: Context) : Dialog(context), View.OnClickListener {

    interface DatePickDialogCallback {
        fun dateSelected(date: Date?)
    }

    // Controls
//    private var tvTitle: TextView? = null
//    private var btnClose: ImageView? = null
//    private var btnOk: Button? = null
//    private  var btnCancel:Button? = null
    private var datePicker: CalendarView? = null

    // Variables
    private var selectedDate: Date? = null
    private var datePickDialogCallback: DatePickDialogCallback? = null

     constructor(
        context: Context,
        titleResId: Int,
        allowAllDate: Boolean,
        date: Date?,
        callback: DatePickDialogCallback?
    ): this(context) {
//        super(context)
        setContentView(R.layout.layout_date_pick_popup_with_title)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setCancelable(false)
        selectedDate = date
        datePickDialogCallback = callback
//        tvTitle = findViewById<TextView>(R.id.tvTitle)
        tvTitle!!.setText(titleResId)
//        btnClose = findViewById<ImageView>(R.id.btnClose)
        btnClose!!.setOnClickListener(this)
//        datePicker = findViewById<CalendarView>(R.id.datePicker)
//        btnOk = findViewById<Button>(R.id.btnOk)
        btnOk!!.setOnClickListener(this)
//        btnCancel = findViewById<Button>(R.id.btnCancel)
        btnCancel.setOnClickListener(this)
        if (selectedDate == null) selectedDate = Date()
        datePicker!!.date = selectedDate!!.time
        datePicker!!.setOnDateChangeListener { calendarView: CalendarView?, year: Int, month: Int, dayOfMonth: Int ->
            updateDate(
                year,
                month,
                dayOfMonth
            )
        }
        if (!allowAllDate) datePicker!!.minDate = Date().time
    }

    private fun updateDate(year: Int, month: Int, day: Int) {
        val cal = Calendar.getInstance()
        cal[Calendar.YEAR] = year
        cal[Calendar.MONTH] = month
        cal[Calendar.DAY_OF_MONTH] = day
        selectedDate = cal.time
    }

    @SuppressLint("NonConstantResourceId")
    override fun onClick(v: View) {
        val date = Date()
        when (v.id) {
            R.id.btnCancel -> dismiss()
            R.id.btnOk -> {
                if (datePickDialogCallback != null) {
                    datePickDialogCallback!!.dateSelected(selectedDate)
                    dismiss()
                }
            }
            R.id.btnClose -> {
                dismiss()
            }
            else -> {}
        }
    }
}