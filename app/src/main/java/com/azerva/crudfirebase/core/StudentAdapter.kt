package com.azerva.crudfirebase.core

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.azerva.crudfirebase.R
import com.azerva.crudfirebase.databinding.ItemStudentListBinding
import com.azerva.crudfirebase.model.Student
import com.azerva.crudfirebase.ui.MainViewModel

@SuppressLint("NotifyDataSetChanged")

class StudentAdapter (private val mainViewModel : MainViewModel) : RecyclerView.Adapter<StudentAdapter.ViewHolder>() {

    internal var studentList = mutableListOf<Student>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class ViewHolder (itemView : View) : RecyclerView.ViewHolder(itemView){
        private val binding = ItemStudentListBinding.bind(itemView)

        fun render(student: Student) = with(binding){

            tvName.text = student.name
            tvSurname.text = student.surname
            tvCourse.text = student.course
            tvAge.text = student.age

            if (adapterPosition %2 == 1){
                studentLayout.setBackgroundColor(ContextCompat.getColor(itemView.context, R.color.orange_light))
            }

            itemViewObject.setOnClickListener {
                mainViewModel.onViewDataStudent(student)
                mainViewModel.getId(student.id.toString())
            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_student_list, parent, false))
    }

    override fun getItemCount(): Int = studentList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.render(studentList[position])
    }
}