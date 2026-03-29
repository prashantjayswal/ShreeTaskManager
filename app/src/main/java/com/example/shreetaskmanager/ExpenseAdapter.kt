package com.example.shreetaskmanager

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*

class ExpenseAdapter(
    private val items: MutableList<ExpenseManager.Record>,
    private val onItemClick: ((ExpenseManager.Record) -> Unit)? = null
) : RecyclerView.Adapter<ExpenseAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val amount: TextView = view.findViewById(R.id.textAmount)
        val description: TextView = view.findViewById(R.id.textDescription)
        val type: TextView = view.findViewById(R.id.textType)
        val date: TextView = view.findViewById(R.id.textDate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_expense, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val record = items[position]
        holder.amount.text = "₹${record.amount}"
        holder.description.text = record.description ?: "No description"
        
        // Show type and category
        holder.type.text = "${if (record.isInvestment) "Investment" else "Expense"} - ${record.category}"
        
        // Show Date and Time
        val sdf = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
        holder.date.text = sdf.format(Date(record.dateMillis))
        
        val color = if (record.isInvestment) 0xFF4CAF50.toInt() else 0xFFF44336.toInt()
        holder.amount.setTextColor(color)

        holder.itemView.setOnClickListener {
            onItemClick?.invoke(record)
        }
    }

    override fun getItemCount(): Int = items.size

    fun update(newItems: List<ExpenseManager.Record>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }
}
