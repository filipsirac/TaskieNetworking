package hr.ferit.brunozoric.taskie.ui.adapters

import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.RecyclerView
import hr.ferit.brunozoric.taskie.model.BackendTask
import hr.ferit.brunozoric.taskie.model.PriorityColor
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_task.view.*

class TaskHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {

    fun bindData(task: BackendTask, onItemSelected: (BackendTask) -> Unit, onItemLongClick: (BackendTask) -> Boolean) {

        containerView.setOnClickListener { onItemSelected(task) }
        containerView.setOnLongClickListener { onItemLongClick(task) }

        containerView.taskTitle.text = task.title

        val drawable = containerView.taskPriority.drawable
        val wrapDrawable = DrawableCompat.wrap(drawable)

        val priorityColor = when (task.taskPriority) {
            1 -> PriorityColor.LOW
            2 -> PriorityColor.MEDIUM
            else -> PriorityColor.HIGH
        }
        DrawableCompat.setTint(wrapDrawable, ContextCompat.getColor(containerView.context, priorityColor.getColor()))
    }
}