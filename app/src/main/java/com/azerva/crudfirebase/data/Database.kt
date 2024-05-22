package com.azerva.crudfirebase.data

import com.google.firebase.database.FirebaseDatabase
import javax.inject.Inject

class Database @Inject constructor() {

    private val instance :  FirebaseDatabase = FirebaseDatabase.getInstance()
    val database = instance.getReference("students")
}