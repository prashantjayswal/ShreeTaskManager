package com.example.shreetaskmanager

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import java.util.*

class TaskAdapter(
    private val items: MutableList<TaskManager.Task>,
    private val onChecked: (TaskManager.Task) -> Unit,
    private val onLong: (TaskManager.Task) -> Unit,
    private val onOptions: (TaskManager.Task, View) -> Unit
) : RecyclerView.Adapter<TaskAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvStartTime: TextView = view.findViewById(R.id.tvStartTime)
        val tvEndTime: TextView = view.findViewById(R.id.tvEndTime)
        val cardTask: ConstraintLayout = view.findViewById(R.id.cardTask)
        val tvStatus: TextView = view.findViewById(R.id.tvStatus)
        val progressBar: ProgressBar = view.findViewById(R.id.progressBar)
        val tvProgressPercent: TextView = view.findViewById(R.id.tvProgressPercent)
        val icon: ImageView = view.findViewById(R.id.iconType)
        val title: TextView = view.findViewById(R.id.textTitle)
        val subtitle: TextView = view.findViewById(R.id.textSubtitle)
        val btnOptions: View = view.findViewById(R.id.btnOptions)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_task, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val task = items[position]
        holder.title.text = task.title
        holder.subtitle.text = task.notes ?: "No description"
        
        // Strike out completed task
        if (task.completed || task.status == TaskManager.TaskStatus.COMPLETED) {
            holder.title.paintFlags = holder.title.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            holder.title.alpha = 0.5f
        } else {
            holder.title.paintFlags = holder.title.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            holder.title.alpha = 1.0f
        }

        val progress = if (task.completed) 100 else (position * 30 % 101)
        holder.progressBar.progress = progress
        holder.tvProgressPercent.text = String.format(Locale.getDefault(), "%d%%", progress)
        holder.tvStatus.text = task.status.name.lowercase().capitalize()

        val bgRes = when (position % 3) {
            0 -> R.drawable.bg_task_card_green
            1 -> R.drawable.bg_task_card_blue
            else -> R.drawable.bg_task_card_purple
        }
        holder.cardTask.setBackgroundResource(bgRes)

        val sdf = java.text.SimpleDateFormat("hh:mm a", Locale.getDefault())
        
        // Start Time
        if (task.startTimeMillis > 0) {
            holder.tvStartTime.text = sdf.format(Date(task.startTimeMillis)).lowercase()
        } else {
            holder.tvStartTime.text = "--:--"
        }
        
        // End Time
        if (task.endTimeMillis > 0) {
            holder.tvEndTime.text = sdf.format(Date(task.endTimeMillis)).lowercase()
        } else {
            holder.tvEndTime.text = "--:--"
        }

        val iconRes = when (task.type) {
            TaskManager.ReminderType.CALL -> R.drawable.ic_call
            TaskManager.ReminderType.WORK -> R.drawable.ic_work
            TaskManager.ReminderType.STUDY -> R.drawable.ic_study
            else -> R.drawable.ic_task
        }
        holder.icon.setImageResource(iconRes)

        holder.btnOptions.setOnClickListener {
            onOptions(task, it)
        }

        holder.itemView.setOnLongClickListener {
            onLong(task)
            true
        }
    }

    override fun getItemCount(): Int = items.size

    fun update(newItems: List<TaskManager.Task>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }
}
