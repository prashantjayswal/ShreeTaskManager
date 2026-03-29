package com.example.shreetaskmanager

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*

class DateAdapter(
    private val dates: List<Long>,
    private var selectedDate: Long,
    private val onDateSelected: (Long) -> Unit
) : RecyclerView.Adapter<DateAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvDayNum: TextView = view.findViewById(R.id.tvDayNum)
        val tvDayName: TextView = view.findViewById(R.id.tvDayName)
        val container: View = view as View
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_date, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dateMillis = dates[position]
        val calendar = Calendar.getInstance().apply { timeInMillis = dateMillis }
        
        holder.tvDayNum.text = calendar.get(Calendar.DAY_OF_MONTH).toString()
        holder.tvDayName.text = SimpleDateFormat("EEE", Locale.getDefault()).format(calendar.time)

        val isSelected = isSameDay(dateMillis, selectedDate)
        holder.container.background = ContextCompat.getDrawable(
            holder.itemView.context,
            if (isSelected) R.drawable.bg_date_selected else R.drawable.bg_date_unselected
        )
        
        holder.tvDayNum.setTextColor(ContextCompat.getColor(holder.itemView.context, 
            if (isSelected) R.color.primaryDarkColor else R.color.white))
        holder.tvDayName.setTextColor(ContextCompat.getColor(holder.itemView.context, 
            if (isSelected) R.color.primaryDarkColor else R.color.textSecondary))

        holder.itemView.setOnClickListener {
            selectedDate = dateMillis
            notifyDataSetChanged()
            onDateSelected(dateMillis)
        }
    }

    override fun getItemCount(): Int = dates.size

    private fun isSameDay(d1: Long, d2: Long): Boolean {
        val cal1 = Calendar.getInstance().apply { timeInMillis = d1 }
        val cal2 = Calendar.getInstance().apply { timeInMillis = d2 }
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
               cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)
    }
}
