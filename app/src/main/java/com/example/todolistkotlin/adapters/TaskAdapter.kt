package com.example.todolistkotlin.adapters


import android.content.Context
import android.content.res.Resources
import android.view.*
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.todolistkotlin.R
import com.example.todolistkotlin.data.Repository
import com.example.todolistkotlin.data.Task
import kotlinx.android.synthetic.main.list_element.view.*


class TaskAdapter(var tasks: MutableList<Task>, var resources: Resources, var repository: Repository
) :
    RecyclerView.Adapter<TaskAdapter.ViewHolder>() {


    private lateinit var context: Context
val HIGH = 3
val MEDIUM = 2
val LOW = 1


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskAdapter.ViewHolder {

        val v = LayoutInflater.from(parent.context).inflate(R.layout.list_element, parent, false)
        context = parent.context

        return ViewHolder(v)

    }

    override fun onBindViewHolder(holder: TaskAdapter.ViewHolder, position: Int) {

        holder.bindItems(tasks[position], context, resources)
    }

    override fun getItemCount(): Int {

        return tasks.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindItems(
                task: Task,
                context: Context,
                resources: Resources

        ) {
            //convert task id ints to corresponding strings
            itemView.list_element_taskTitle.text = task.title
            when(task.priority){
                HIGH -> itemView.list_element_priority.text = "High"
                MEDIUM -> itemView.list_element_priority.text = "Medium"
                LOW -> itemView.list_element_priority.text = "Low"
            }



            //makes sure the checkbox is ticked if the task is done
            when(task.done){
                true -> itemView.checkbox.isChecked = true
                false -> itemView.checkbox.isChecked = false
            }



            //on checkbox check
            itemView.checkbox.setOnClickListener {
                if (itemView.checkbox.isChecked) {
                    val toast = Toast.makeText(context,
                            "Well done!",
                            Toast.LENGTH_SHORT)

                    toast.show()
                    val position = adapterPosition
                    Repository.updateChecked(position, true)

                } else {
                    Repository.updateChecked(position, false)

                }
            }

            //on delete indiv. task:
            itemView.deleteTask.setOnClickListener { v: View ->
                val position = adapterPosition
                Repository.deleteTask(position)
                notifyItemRemoved(position)

            }

        }

    }
}
