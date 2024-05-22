package com.azerva.crudfirebase.ui

import android.util.Log
import com.azerva.crudfirebase.data.Database
import com.azerva.crudfirebase.model.Student
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class MainUseCase @Inject constructor(
    private val db : Database
){


    suspend fun saveItem(student: Student) = runCatching {
        val nodeReference = db.database.push()
        val node = nodeReference.key!!

        val newStudent = Student.studentToMap(student = student, id = node)
        db.database.child(node)
            .setValue(newStudent)
            .await()

    }.isSuccess

    suspend fun onUpdateStudent(student: Student, id : String) = runCatching{
        val updateStudent = Student.studentToMap(student, id)
        db.database.child(id)
            .setValue(updateStudent)
            .await()
    }.isSuccess

    suspend fun onDeleteStudent(id : String) = runCatching{
        db.database.child(id)
            .removeValue()
            .await()
    }.isSuccess

    suspend fun getStudentList() : Flow<List<Student>> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val students = snapshot.children.mapNotNull { it.getValue(Student::class.java) }
                trySend(students).isSuccess
            }
            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }
        db.database.addValueEventListener(listener)

        awaitClose {
            db.database.removeEventListener(listener)
        }
    }

}