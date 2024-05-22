package com.azerva.crudfirebase.ui

data class MainViewState(
    val isLoading : Boolean = false,
    val isNameValid : Boolean = false,
    val isSurnameValid : Boolean = false,
    val isCourseValid : Boolean = false,
    val isAgeValid : Boolean = false,
    val isButtonAddVisible : Boolean = false,
    val isButtonUpdateVisible : Boolean = false,
    val isButtonDeleteVisible : Boolean = false,
    val isButtonCancelVisible : Boolean = false,

){

}
