package com.azerva.crudfirebase.model

import java.io.Serializable

data class Student(
    val id : String? = null,
    val name: String = "",
    val surname: String = "",
    val course: String = "",
    val age: String = ""

): Serializable{

    companion object{
        fun studentToMap(student: Student, id: String) : Map<String , Any>{
            return mapOf(
                "id" to id,
                "name" to student.name,
                "surname" to student.surname,
                "course" to student.course,
                "age" to student.age,
            )

        }
    }

    fun isNotEmpty() = name.isNotEmpty() && surname.isNotEmpty() && course.isNotEmpty () && age.isNotEmpty()

}
