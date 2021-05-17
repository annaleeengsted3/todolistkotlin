package com.example.todolistkotlin.data

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase


object Repository {
    private lateinit var db: FirebaseFirestore
    var tasks = mutableListOf<Task>()

    //listener to changes that we can then use in the Activity
    private var taskListener = MutableLiveData<MutableList<Task>>()


    fun getData(): MutableLiveData<MutableList<Task>> {
        db = Firebase.firestore

        if (tasks.isEmpty())
            readDataFromFirebase()
        taskListener.value = tasks //we inform the listener we have new data
        return taskListener
    }

    private fun readDataFromFirebase()
    {

        db.collection("tasks").get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    Log.d("Repository", "${document.id} => ${document.data}")
                    val task = document.toObject<Task>()
                    task.id = document.id  //set the ID in the product class
                    tasks.add(task)
                }
                taskListener.value = tasks //notify our listener we have new data
            }
            .addOnFailureListener { exception ->
                Log.d("Repository", "Error getting documents: ", exception)
            }

    }


    fun addTask(addedTask: Task) {
       db.collection("tasks").add(addedTask).addOnSuccessListener { documentReference->
           addedTask.id = documentReference.id
           tasks.add(addedTask)
           taskListener.value = tasks
       }.addOnFailureListener { e -> Log.w("Error", "Error adding document", e) }

    }

    fun updateChecked(index: Int, isUnchecked: Boolean){
        val task = tasks[index]
        if (isUnchecked){
            db.collection("tasks").document(task.id)
                    .update("done", true).addOnFailureListener { e -> Log.w("Error", "Error deleting document", e) }
            tasks[index].done = true
        } else if(!isUnchecked){
            db.collection("tasks").document(task.id)
                    .update("done", false).addOnFailureListener { e -> Log.w("Error", "Error deleting document", e) }
            tasks[index].done = false
        }

        taskListener.value = tasks
    }


    fun deleteTask(index: Int){
        val task = tasks[index]
        db.collection("tasks").document(task.id).delete().addOnSuccessListener {
            Log.d("Snapshot","DocumentSnapshot with id: ${task.id} successfully deleted!")
            tasks.removeAt(index) //removes it from the list
            taskListener.value = tasks
        }
            .addOnFailureListener { e -> Log.w("Error", "Error deleting document", e) }
    }


    fun deleteAll(){
        val batch = db.batch()
        for (task in tasks) {
            val ref = db.collection("tasks").document(task.id)
            batch.delete(ref)
        }
        batch.commit().addOnCompleteListener {
            tasks.clear()
            taskListener.value = tasks
        }
    }

}