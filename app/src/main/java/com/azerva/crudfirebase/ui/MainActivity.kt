package com.azerva.crudfirebase.ui

import android.os.Bundle
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.azerva.crudfirebase.R
import com.azerva.crudfirebase.core.ErrorType
import com.azerva.crudfirebase.core.StudentAdapter
import com.azerva.crudfirebase.core.toast
import com.azerva.crudfirebase.databinding.ActivityMainBinding
import com.azerva.crudfirebase.model.Student
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel : MainViewModel by viewModels()

    private lateinit var studentAdapter : StudentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.progressCircular.isIndeterminate = true

        setup()
    }

    private fun setup() {
        observers()
        listeners()
        initRecyclerView()
        viewModel.getStudentList()
    }

    private fun listeners() {
        binding.apply {
            btnCancel.setOnClickListener { viewModel.onCancelViewData()
                cleanDataForm()
            }

            btnAdd.setOnClickListener {
                viewModel.saveStudent(
                    Student(
                        name = etName.text.toString(),
                        surname = etSurname.text.toString(),
                        course = etCourse.text.toString(),
                        age = etAge.text.toString()
                    )
                )
            }

            btnUpdate.setOnClickListener {
                viewModel.updateStudent(
                    Student(
                        name = etName.text.toString(),
                        surname = etSurname.text.toString(),
                        course = etCourse.text.toString(),
                        age = etAge.text.toString()
                    )
                )
                cleanDataForm()
            }

            btnDelete.setOnClickListener {
                viewModel.deleteStudent()
                cleanDataForm() }
        }
    }

    private fun observers() {
        lifecycleScope.launch{
            viewModel.viewState.collect{viewSate->
                updateUI(viewSate)
            }
        }

        viewModel.showToast.observe(this@MainActivity){showToast->
            if (showToast)
                showToasts()
        }

        viewModel.cleanForm.observe(this@MainActivity){cleanForm->
            if (cleanForm) cleanDataForm()
        }

        viewModel.getStudentList.observe(this@MainActivity){students->
            studentAdapter.studentList = students.toMutableList()
        }

        viewModel.getStudent.observe(this@MainActivity){student->
            getStudentData(student)
        }



    }



    private fun getStudentData(student: Student) {
        binding.apply {
            etName.setText(student.name)
            etSurname.setText(student.surname)
            etCourse.setText(student.course)
            etAge.setText(student.age)
        }
    }

    private fun cleanDataForm() {
        viewModel.cleanForm(binding.formData)
    }

    private fun showToasts() {
        when(viewModel.errorType){
            ErrorType.ERROR_SAVE-> toast("Error saving student")
            ErrorType.ERROR_UPDATE-> toast("Error Updating student")
            ErrorType.ERROR_DELETE-> toast("Error Deleting student")
            ErrorType.ERROR_SAVE_EXCEPTION-> toast("Error saving student, check connection")
            ErrorType.ERROR_UPDATE_EXCEPTION-> toast("Error Updating student, check connection")
            ErrorType.ERROR_DELETE_EXCEPTION-> toast("Error Deleting student, check connection")
            ErrorType.SAVE_STUDENT-> toast("Save Success")
            ErrorType.UPDATE_STUDENT-> toast("Update Success")
            ErrorType.DELETE_STUDENT-> toast("Delete Success")
            ErrorType.EMPTY_FIELDS-> toast("Error!! Fields can´t be empty")

            else -> return
        }
    }

    private fun updateUI(viewSate: MainViewState) {
        binding.apply {
            progressCircular.isVisible = viewSate.isLoading
            btnAdd.isVisible = viewSate.isButtonAddVisible
            btnUpdate.isVisible = viewSate.isButtonUpdateVisible
            btnDelete.isVisible = viewSate.isButtonDeleteVisible
            btnCancel.isVisible = viewSate.isButtonCancelVisible
        }
    }

    private fun initRecyclerView(){
        studentAdapter = StudentAdapter(viewModel)

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = studentAdapter

        }

    }




}