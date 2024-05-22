package com.azerva.crudfirebase.ui

import android.util.Log
import android.widget.EditText
import android.widget.LinearLayout
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.azerva.crudfirebase.core.ErrorType
import com.azerva.crudfirebase.model.Student
import com.google.firebase.FirebaseException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val mainUseCase: MainUseCase
) : ViewModel() {

    var errorType : ErrorType? = null

    private var _viewState = MutableStateFlow(MainViewState())
    val viewState : StateFlow<MainViewState> get() = _viewState

    private var _getStudentList = MutableLiveData<List<Student>>()
    val getStudentList : LiveData<List<Student>> get() = _getStudentList

    private val _showToast = MutableLiveData(false)
    val showToast : LiveData<Boolean> get() = _showToast

    private val _cleanForm = MutableLiveData(false)
    val cleanForm : LiveData<Boolean> get() = _cleanForm

    private var _getStudent = MutableLiveData<Student>()
    val getStudent : LiveData<Student> get() = _getStudent

    private var _getStudentId = MutableLiveData<String>()
    val getStudentId : LiveData<String> get() = _getStudentId

    init {
        viewAddButton()
    }

    private fun viewAddButton(){
        _viewState.value = MainViewState(isButtonAddVisible = true)
    }

    fun getStudentList() {
        viewModelScope.launch {
            mainUseCase.getStudentList().collect { students ->
                _getStudentList.value = students
            }
        }
    }

    fun saveStudent(student: Student){
        if (student.isNotEmpty()){
            onSaveStudent(student)
        }else{
            showToast(ErrorType.EMPTY_FIELDS)
        }
    }

    private fun onSaveStudent(student: Student){
        viewModelScope.launch {
            _viewState.value = MainViewState(
                isLoading = true, isButtonAddVisible = true)
            try {
                val isSaved = mainUseCase.saveItem(student)
                if (isSaved){
                    showToast(ErrorType.SAVE_STUDENT)
                    _cleanForm.value = true
                }else{
                    showToast(ErrorType.ERROR_SAVE)
                }
            }catch (e : FirebaseException){
                showToast(ErrorType.ERROR_SAVE_EXCEPTION)

            }finally {
                _viewState.value = MainViewState(isLoading = false
                    , isButtonAddVisible = true)
            }

        }


    }
    fun updateStudent(student: Student){
        if (student.isNotEmpty()){
            onUpdateStudent(student)
        }else{
            showToast(ErrorType.EMPTY_FIELDS)
        }
    }

    private fun onUpdateStudent(student: Student){
        viewModelScope.launch {
            _viewState.value = MainViewState(isLoading = true)
            try {
                val isUpdated = mainUseCase.onUpdateStudent(student, getStudentId.value.toString())

                if (isUpdated){
                    showToast(ErrorType.UPDATE_STUDENT)

                }else{
                    updateOptionButtons()
                    showToast(ErrorType.ERROR_UPDATE)

                }
            }catch (e : FirebaseException){
                showToast(ErrorType.ERROR_UPDATE_EXCEPTION)
                updateOptionButtons()

            }finally {
                _viewState.value = MainViewState(isLoading = false, isButtonAddVisible = true)
            }
        }
    }

    fun deleteStudent(){
        onDeleteStudent()
    }

    private fun onDeleteStudent(){
        viewModelScope.launch {
            _viewState.value = MainViewState(isLoading = true)
            try {
                val isDelete = mainUseCase.onDeleteStudent(getStudentId.value.toString())

                if (isDelete){
                    showToast(ErrorType.DELETE_STUDENT)

                }else{
                    updateOptionButtons()
                    showToast(ErrorType.ERROR_DELETE)

                }
            }catch (e : FirebaseException){
                showToast(ErrorType.ERROR_DELETE_EXCEPTION)
                updateOptionButtons()

            }finally {
                _viewState.value = MainViewState(isLoading = false, isButtonAddVisible = true)
            }
        }
    }


    private fun showToast(errorType: ErrorType){
        this.errorType = errorType
        _showToast.value = true
    }

    fun cleanForm(linearLayout: LinearLayout){
        _viewState.value = MainViewState(isButtonAddVisible = true)
        for (i in 0 until linearLayout.childCount){
            val child = linearLayout.getChildAt(i)
            if (child is EditText){
                child.text.clear()
            }
        }
    }

    fun onViewDataStudent(student: Student){
        _getStudent.value = student
        updateOptionButtons()
    }

    fun onCancelViewData(){
        viewAddButton()
    }

    private fun updateOptionButtons(){
        _viewState.value = MainViewState(
            isButtonUpdateVisible = true,
            isButtonDeleteVisible = true,
            isButtonCancelVisible = true,
        )
    }

    fun getId(studentId : String){
        _getStudentId.value = studentId
    }


}