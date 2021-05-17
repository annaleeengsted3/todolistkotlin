package com.example.todolistkotlin

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todolistkotlin.adapters.TaskAdapter
import com.example.todolistkotlin.data.Repository
import com.example.todolistkotlin.data.Task
import com.example.todolistkotlin.ui.PreferenceHandler
import kotlinx.android.synthetic.main.content_main.*
import com.google.firebase.FirebaseApp


class MainActivity : AppCompatActivity() {

    private val RESULT_CODE_PREFERENCES = 1

    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter:
            RecyclerView.Adapter<TaskAdapter.ViewHolder>? = null
private var isNight: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        isNight = PreferenceHandler.isNightMode(this)
        if(isNight) {
            setTheme(R.style.nightTheme)
        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        FirebaseApp.initializeApp(applicationContext)


        //From Android documentation
        val spinner: Spinner = findViewById(R.id.priority_spinner)
        ArrayAdapter.createFromResource(
            this,
            R.array.priorities_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }


//whenever the data in the repo changes, this code is executed:
        Repository.getData().observe(this, Observer {
            updateUI()
        })

   addTask.setOnClickListener{
       val taskText= taskText.text.toString()
       val spinnerPriority= spinner.getSelectedItem().toString();
       var taskPriority = 1
       when(spinnerPriority){
           "High" -> taskPriority= 3
           "Medium" -> taskPriority= 2
           "Low" -> taskPriority= 1
       }
       val addedTask = Task(taskText, taskPriority)
        Repository.addTask(addedTask)
   }



    }




    fun updateUI() {
        layoutManager = LinearLayoutManager(this)
        task_recyclerview.layoutManager = layoutManager
        adapter = TaskAdapter(Repository.tasks, resources, Repository)
        task_recyclerview.adapter = adapter

    }




    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_sort->{
                Repository.tasks.sortByDescending { it.priority }

                adapter?.notifyDataSetChanged()
            }
            R.id.action_deleteAll -> {
                areYouSure()
                return true
            }
            R.id.action_share -> {
                shareText()
                return true
            }
            R.id.action_settings -> {
                val intent = Intent(this, SettingsActivity::class.java)
                startActivityForResult(intent, RESULT_CODE_PREFERENCES)
                return true
            }

        }
        return super.onOptionsItemSelected(item)
    }

    fun areYouSure(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.areyousure)
        builder.setMessage(R.string.areyousure_des)
        builder.setIcon(R.drawable.ic_baseline_delete_forever_24)
        builder.setPositiveButton("Yes") { dialogInterface, which ->
            Repository.deleteAll()
        }
        builder.setNeutralButton("Cancel") { dialogInterface, which ->

        }
        val alertDialog: AlertDialog = builder.create()
        alertDialog.show()
    }


    private fun shareText() {
        var allTaskTitles = "To Do List:"
        for(i in Repository.tasks.indices){
            var priority: String = ""
            when(Repository.tasks[i].priority){
                1 -> priority = "Low"
                2 -> priority = "Medium"
                3 -> priority = "High"
            }
          allTaskTitles =  allTaskTitles.plus("\n").plus("${i+1}:").plus(Repository.tasks[i].title).plus(", Priority: ").plus(priority)
        }

        val sharingIntent = Intent(Intent.ACTION_SEND)
        sharingIntent.type = "text/plain" //MIME-TYPE
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Shared Data")
        sharingIntent.putExtra(Intent.EXTRA_TEXT, allTaskTitles)
        startActivity(Intent.createChooser(sharingIntent, "Share Using"))
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == RESULT_CODE_PREFERENCES)
        //back from settings
        {
            recreate()
        }
        super.onActivityResult(requestCode, resultCode, data)
    }


}