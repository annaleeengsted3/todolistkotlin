package com.example.todolistkotlin.data

import com.google.firebase.firestore.Exclude

data class Task(var title:String = "", var priority:Int = 1, var done:Boolean = false, @get:Exclude var id: String = "") {

}
